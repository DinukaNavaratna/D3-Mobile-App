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

from src.test.mfcc import compare
from src.scores import insert_q

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
            user_id = request.form.get('user_id')
            level = request.form.get('level')
            duration = request.form.get('duration')
            context = request.form.get('context')
            logger.debug("Context: "+context)

            f = request.files['file']
            ext = (f.filename).split('.')[-1]
            fileId = datetime.now().strftime('%Y%m-%d%H-%M%S-') + str(uuid4())
            filePath = "./clips/"+fileId+"."+ext
            f.save(filePath)
            track = AudioSegment.from_file(filePath,  format= 'm4a')
            track.export(filePath, format='wav')

            logger.info("Audio saved")

            query = ""
            points = 0
            accuracy = ""
            game = "dyslexia"
            if("EasyEasy" in level or "EasyMedium" in level or "EasyHard" in level):
                accuracy = "N/A"
                query = ""
                if("_treatment" in level):
                    game = "dyslexia_easy_treatment"
                else:
                    game = "dyslexia_easy"
                return jsonify({"success":"true", "message":"{\"accuracy\":\""+str(accuracy)+"\"}"})
            elif("HardEasy" in level or "HardMedium" in level or "HardHard" in level):
                accuracy = compare(filePath, "src/Recordings/"+context+".wav")[:8]
                points = int(float(accuracy)/10)
                query = "INSERT INTO dyslexia_hard_score (user_id, level, duration, accuracy, points) VALUES ('"+str(user_id)+"', '"+str(level)+"', "+str(duration)+", '"+str(accuracy)+"', "+str(points)+");"
                if("_treatment" in level):
                    game = "dyslexia_hard_treatment"
                else:
                    game = "dyslexia_hard"

            msg, response = insert_q(query, user_id, game, str(points))
            logger.info("Dyslexia DB: "+str(msg)+" | "+str(response))

            logger.debug("Accuracy: "+str(accuracy))
            return jsonify({"success":"true", "message":"{\"accuracy\":\""+str(accuracy)+"\"}"})
        except Exception as e:
            exc_type, exc_obj, exc_tb = sys.exc_info()
            logger.error("Exception | upload_audio: "+str(e)+"\nType: "+str(exc_type)+"\nLine: "+str(exc_tb.tb_lineno))
            return jsonify({"success":"false", "message":"server error"})


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