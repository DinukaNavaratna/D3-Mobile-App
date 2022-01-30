from flask_restful import Resource
from flask import jsonify, request
from loguru import logger
from dotenv import load_dotenv
from loguru import logger

from src.db import ExecuteQuery, ExecuteSelectQuery

logger.add('logs/scores.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="scores", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class update_scores(Resource):
    def post(self):
        
        msg = ""
        response = ""

        try:
            content = request.json

            user_id = content['user_id']
            game = content['game']
            score = content['score']

            update_query = "UPDATE scores SET score='"+score+"' WHERE user_id='"+user_id+"' AND game='"+game+"';"
            query_response = ExecuteQuery.execute(update_query)

            if(query_response == "success"):
                msg = "success"
                response = "Score updated successfully!"
            else:
                msg = "failed"
                response = "Score update failed!"

            logger.info("Response | update_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | update_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            

class get_scores(Resource):
    def post(self):
        
        msg = ""
        response = ""
        dyscalculia = ""
        dysgraphia = ""
        dyslexia = ""

        try:
            content = request.json

            user_id = content['user_id']

            select_response = ExecuteSelectQuery.execute("SELECT score FROM scores WHERE user_id='"+user_id+"' ORDER BY game;")
                    
            if(len(select_response) > 0):
                dyscalculia = select_response[0][0]
                dysgraphia = select_response[1][0]
                dyslexia = select_response[2][0]
                msg = "success"
                response = "Scores retrieved!"
            else:
                msg = "failed"
                response = "Score retrieval failed!"

            logger.info("Response | get_scores: "+response)
            return jsonify({"msg":msg, "response":response, "dyscalculia":dyscalculia, "dysgraphia":dysgraphia, "dyslexia":dyslexia})
            
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | get_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            


