from flask import Flask
from flask_restful import Api
from flask_cors import CORS
from dotenv import load_dotenv
import os
from loguru import logger

from src.routes import initialize_routes

logger.add('logs/app.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="__main__", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

# init server
app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})
api = Api(app)

# init api routes
initialize_routes(api)


# connection test route
@app.route("/", methods=["GET", "POST"])
def health():
    return "Welcome to ණැනස application local server..."


# run Server
if __name__ == "__main__":
    app.run(
        host = os.getenv("app_host"), 
        port = os.getenv("app_port"), 
        debug = os.getenv("app_debug")
    )