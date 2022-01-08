from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
from datetime import datetime
from uuid import uuid4
import json

logger.add('logs/user.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="users", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class upload_audio(Resource):
    def get(self):
        try:
            f = request.files['file']
            ext = (f.filename).split('.')[-1]
            fileId = datetime.now().strftime('%Y%m-%d%H-%M%S-') + str(uuid4())
            filePath = "./clips/"+fileId+"."+ext
            f.save(filePath)
            return jsonify({"msg":"success", "accuracy":"0"})
        except Exception as e:
            print(str(e))
            return jsonify({"msg":"failed","error":str(e)})
