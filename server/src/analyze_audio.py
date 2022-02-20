from flask import request
from loguru import logger
from dotenv import load_dotenv
from datetime import datetime
from uuid import uuid4
from pydub import AudioSegment
from pydub.utils import make_chunks

logger.add('logs/audio.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="audio", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

def asd():
    f = request.files['file']
    ext = (f.filename).split('.')[-1]
    fileId = datetime.now().strftime('%Y%m-%d%H-%M%S-') + str(uuid4())
    filePath = "clips/"+fileId+"."+ext
    f.save(filePath)
    myaudio = AudioSegment.from_file("clips/"+fileId+"."+ext , "wav") 
    chunk_length_ms = 5000 # pydub calculates in millisec
    chunks = make_chunks(myaudio, chunk_length_ms) #Make chunks of one sec

    #Export all of the individual chunks as wav files
    filenames = []
    for i, chunk in enumerate(chunks):
        chunk_name = "clips/"+fileId+"_{0}.wav".format(i)
        #print("exporting", chunk_name)
        chunk.export(chunk_name, format="wav")
        filenames.append(chunk_name)

    #response = '{"file_path": "'+filePath+'", "file_type": "'+f.content_type+'", "emotion": "'+emotion+'"}'
    response = '{"results": ['
    first = True
    for filename in filenames:
        emotion = str(analyze(model, lb, filePath))
        if first:
            response += '{"file_path": "'+filename+'", "file_type": "audio/wav", "emotion": "'+emotion+'"}'
            first = False
        else:
            response += ', {"file_path": "'+filename+'", "file_type": "audio/wav", "emotion": "'+emotion+'"}'
        
    response += ']}'
    return response


#-------------------------------------------------------------------------------------------------

def split_words():
    from pydub.silence import split_on_silence

    sound_file = AudioSegment.from_wav("a-z.wav")
    audio_chunks = split_on_silence(sound_file, 
        # must be silent for at least half a second
        min_silence_len=500,

        # consider it silent if quieter than -16 dBFS
        silence_thresh=-16
    )

    for i, chunk in enumerate(audio_chunks):

        out_file = ".//splitAudio//chunk{0}.wav".format(i)
        print("exporting", out_file)
        chunk.export(out_file, format="wav")


from flask_restful import Resource
class analyze(Resource):
    def get(self):
        import glob
        files = glob.glob("./clips/*")
        from .correlation import correlate
        correlate(files[0], files[1])