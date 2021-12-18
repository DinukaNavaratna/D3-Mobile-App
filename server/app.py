from flask import Flask, jsonify
from flask_restful import Api
from flask_cors import CORS

from src.routes import initialize_routes
from dotenv import load_dotenv
import os

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
    return "Welcome to ණැනස application..."


# run Server
if __name__ == "__main__":
    app.run(
        host = os.getenv("HOST_NAME"), 
        port = os.getenv("PORT"), 
        debug = True
    )