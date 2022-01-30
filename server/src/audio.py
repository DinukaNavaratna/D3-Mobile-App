from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
from datetime import datetime
from uuid import uuid4
from pydub import AudioSegment
from pydub.utils import make_chunks
from pathlib import Path

logger.add('logs/audio.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="audio", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class upload_audio(Resource):
    def post(self):
        try:
            logger.info("Request started processing")
            function = request.form.get('function')
            logger.info(request.form.get('function'))
            logger.info(request.form.get('filename'))
            logger.info(str(request.files))
            logger.info(str(request.get_data(parse_form_data=True)))
            logger.info(request.content_type)

            data = {}
            for key, value in request.form.items():
                if key.endswith('[]'):
                    data[key[:-2]] = request.form.getlist(key)
                else:
                    data[key] = value
            logger.info(str(data)) 

            f = request.files['uploadedfile']
            ext = (f.filename).split('.')[-1]
            fileId = datetime.now().strftime('%Y%m-%d%H-%M%S-') + str(uuid4())
            filePath = "./clips/"+fileId+"."+ext
            f.save(filePath)
            logger.info("Audio saved")
            filenames = [fileId+"."+ext]
            if(function == "sentence"):
                myaudio = AudioSegment.from_file(filePath, "wav")
                chunk_length_ms = 2000 # pydub calculates in millisec
                chunks = make_chunks(myaudio, chunk_length_ms) #Make chunks of one sec

                #Export all of the individual chunks as wav files
                for i, chunk in enumerate(chunks):
                    chunk_name = ("./clips/"+fileId+"_{0}."+ext).format(i)
                    #print("exporting", chunk_name)
                    chunk.export(chunk_name, format="wav")
                    filenames.append(chunk_name)
            
            #response = '{"results": ['
            #first = True
            #for filename in filenames:
            #    emotion = str(analyze(model, lb, filePath))
            #    if first:
            #        response += '{"file_path": "'+filename+'", "file_type": "audio/wav", "emotion": "'+emotion+'"}'
            #        first = False
            #    else:
            #        response += ', {"file_path": "'+filename+'", "file_type": "audio/wav", "emotion": "'+emotion+'"}'
            #    
            #response += ']}'
            #return response

            return jsonify({"msg":"success", "accuracy":str(filenames)})
        except Exception as e:
            logger.error("Exception | upload_audio: "+str(e))
            return jsonify({"msg":"failed","error":str(e)})

