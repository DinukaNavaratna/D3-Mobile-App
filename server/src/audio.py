from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
from datetime import datetime
from uuid import uuid4
from pydub import AudioSegment
from pydub.utils import make_chunks
from pathlib import Path
from .audio_processing.preprocess import preprocess_dataset
from .audio_processing.train import start_train
from .audio_processing.analyze import analyze_audio
import sys

logger.add('logs/audio.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="audio", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class train_model(Resource):
    def post(self):
        try:
            content = request.json
            option = content['option']
            if option == "full":
                if preprocess_dataset() == 1:
                    train_feedback = start_train()
                    if train_feedback[0] == 1:
                        return jsonify({"msg":"success", "Loss": train_feedback[1], "Accuracy": train_feedback[2]})
                    else:
                        logger.error("Failed | train_model: start_train() did not return 1")
                        return jsonify({"msg":"failed","error":"Training failed"})
                else:
                    logger.error("Failed | train_model: preprocess_dataset() did not return 1")
                    return jsonify({"msg":"failed","error":"Pre-processing failed"})
            elif option == "preprocess":
                if preprocess_dataset() == 1:
                    return jsonify({"msg":"success"})
                else:
                    logger.error("Failed | train_model: preprocess_dataset() did not return 1")
                    return jsonify({"msg":"failed","error":"Pre-processing failed"})
            elif option == "train":
                train_feedback = start_train()
                if train_feedback[0] == 1:
                    return jsonify({"msg":"success", "Loss": train_feedback[1], "Accuracy": train_feedback[2]})
                else:
                    logger.error("Failed | train_model: start_train() did not return 1")
                    return jsonify({"msg":"failed","error":"Training failed"})
            else:
                logger.error("Failed | train_model: Option not provided in the request")
                return jsonify({"msg":"failed","error":"Option not provided in the request"})
        except Exception as e:
            logger.error("Exception | train_model: "+str(e))
            return jsonify({"msg":"failed","error":str(e)})


class upload_audio(Resource):
    def post(self):
        try:
            logger.info("Request started processing")
            function = request.form.get('function')
            logger.info("Function: "+str(function))

            data = {}
            for key, value in request.form.items():
                if key.endswith('[]'):
                    data[key[:-2]] = request.form.getlist(key)
                else:
                    data[key] = value
            logger.info("Data: "+str(data)) 


            f = request.files['file']
            ext = (f.filename).split('.')[-1]
            fileId = datetime.now().strftime('%Y%m-%d%H-%M%S-') + str(uuid4())
            filePath = "./clips/"+fileId+"."+ext
            f.save(filePath)
            track = AudioSegment.from_file(filePath,  format= 'm4a')
            track.export(filePath, format='wav')

            logger.info("Audio saved")
            return jsonify({"success":"true", "message":"{\"accuracy\":\"50%\"}"})
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
            exc_type, exc_obj, exc_tb = sys.exc_info()
            logger.error("Exception | upload_audio: "+str(e)+"\nType: "+str(exc_type)+"\nLine: "+str(exc_tb.tb_lineno))
            return jsonify({"msg":"failed","error":str(e)})


class analyze(Resource):
    def post(self):
        try:
            content = request.json
            filename = content['filename']
            keyword = analyze_audio(filename)
            if "Error" in keyword:
                return jsonify({"msg":"failed", "error":str(keyword)})
            else:
                return jsonify({"msg":"success", "keyword":str(keyword)})
        except Exception as e:
            exc_type, exc_obj, exc_tb = sys.exc_info()
            logger.error("Exception | analyze: "+str(e)+"\nType: "+str(exc_type)+"\nLine: "+str(exc_tb.tb_lineno))
            return jsonify({"msg":"failed", "error":str(e)})