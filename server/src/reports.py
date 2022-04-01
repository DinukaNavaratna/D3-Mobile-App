from flask_restful import Resource
from flask import render_template, make_response
from loguru import logger
from dotenv import load_dotenv
import hashlib
from loguru import logger

from src.db import ExecuteQuery, ExecuteSelectQuery

logger.add('logs/reports.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="reports", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class test(Resource):
    def get(self, id):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('./index.html', id=id), 200, headers)
            

class dyscalculia(Resource):
    def get(self, id):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('./Dyscalculia.html', id=id), 200, headers)

class dysgraphia(Resource):
    def get(self, id):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('./Dysgraphia.html', id=id), 200, headers)

class dyslexia_easy(Resource):
    def get(self, id):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('./Dyslexia - Easy.html', id=id), 200, headers)

class dyslexia_hard(Resource):
    def get(self, id):
        headers = {'Content-Type': 'text/html'}
        return make_response(render_template('./Dyslexia - Hard.html', id=id), 200, headers)
