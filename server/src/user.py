from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
import json

logger.add('logs/user.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="users", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class register(Resource):
    def get(self):
        try:
            content = request.json
            print(str(content))
            #fname = content['fname']
            #lname = content['lname']
            #email = content['email']
            #number = content['number']
            #childname = content['childname']
            #childage = content['childage']
            #psw = content['password']
            fname = request.args.get('fname')
            lname = request.args.get('lname')
            email = request.args.get('email')
            number = request.args.get('number')
            childname = request.args.get('childname')
            childage = request.args.get('childage')
            psw = request.args.get('password')
            if email == "" or psw == "" or not email or not psw:
                return jsonify({"msg":"failed", "error":"Empty email or password!"})
            emails = email.split("@")
            return jsonify({"msg":"success","email":email,"username":emails[0]})
        except Exception as e:
            print(str(e))
            return jsonify({"msg":"failed","error":str(e)})


class login(Resource):
    def get(self):
        try:
            content = request.json
            print(str(content))
            #email = content['email']
            #psw = content['password']
            email = request.args.get('email')
            psw = request.args.get('password')
            if email == "" or psw == "" or not email or not psw:
                return jsonify({"msg":"failed", "error":"Empty email or password!"})
            emails = email.split("@")
            if psw == emails[0]:
                return jsonify({"msg":"success","email":email,"username":psw+"0"})
            else:
                return jsonify({"msg":"failed", "error":"Wrong email or password"})
        except Exception as e:
            print(str(e))
            return jsonify({"msg":"failed", "error":str(e)})

