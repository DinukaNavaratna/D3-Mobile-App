from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
import hashlib
from loguru import logger

from src.db import ExecuteQuery, ExecuteSelectQuery

logger.add('logs/users.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="users", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class register(Resource):
    def post(self):
        
        msg = ""
        response =""

        try:
            content = request.json

            fname = content['fname']
            lname = content['lname']
            email = content['email']
            email = email.lower()
            phone = content['phone']
            childname = content['childname']
            childage = content['childage']
            password = content['password']
            encrypted_password = hashlib.md5(password.encode())
            password = encrypted_password.hexdigest()
            
            user_id = ""

            if email == "" or password == "" or not email or not password:
                msg = "failed"
                response = "Empty email or password!"
            else:
                insert_query = "INSERT IGNORE INTO users (fname, lname, email, number, childname, childage, password) VALUES ('"+fname+"', '"+lname+"', '"+email+"', '"+phone+"', '"+childname+"', '"+childage+"', '"+password+"');"
                query_response = ExecuteQuery.execute(insert_query)

                if(query_response == "success"):
                    select_response = ExecuteSelectQuery.execute("SELECT id FROM users WHERE email='"+email+"' AND password='"+password+"' LIMIT 1;")
                    
                    if(len(select_response) > 0):
                        user_id = select_response[0][0]
                        msg = "success"
                        response = "User found!"
                    else:
                        msg = "failed"
                        response = "User not found!"
                else:
                    msg = "failed"
                    response = "User insert failed!"

                response = query_response

            logger.info("Response | register: "+response)
            return jsonify({"msg":msg, "response":response, "user_id":user_id, "email":email, "username":fname})
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | register: "+response)
            return jsonify({"msg":msg, "response":response})
            


class login(Resource):
    def post(self):

        msg = ""
        response =""

        try:
            content = request.json

            email = content['email']
            email = email.lower()
            password = content['password']
            encrypted_password = hashlib.md5(password.encode())
            password = encrypted_password.hexdigest()
            
            user_id = ""
            fname = ""

            if email == "" or password == "" or not email or not password:
                msg = "failed"
                response = "Empty email or password!"
            else:
                select_response = ExecuteSelectQuery.execute("SELECT id, fname FROM users WHERE email='"+email+"' AND password='"+password+"' LIMIT 1;")
                
                if(len(select_response) > 0):
                    user_id = select_response[0][0]
                    fname = select_response[0][1]
                    msg = "success"
                    response = "User found!"
                else:
                    msg = "failed"
                    response = "User not found!"

            logger.info("Response | login: "+response)
            return jsonify({"msg":msg, "response":response, "user_id":user_id, "username":fname, "email":email})
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | register: "+response)
            return jsonify({"msg":msg, "response":response})


