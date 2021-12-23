from flask import Flask, jsonify, request
from flask_restful import Api
from flask_cors import CORS
from dotenv import load_dotenv
import os

from src.routes import initialize_routes
from database.model import create

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


# connection test route
@app.route("/create_db", methods=["GET", "POST"])
def create_db():
    return create()


@app.route("/login", methods=["POST", "GET"])
def login():
    try:
        content = request.json
        print(str(content))
        email = content['email']
        psw = content['password']
        emails = email.split("@")
        if psw == emails[0]:
            return jsonify({"msg":"success","email":email,"username":psw+"0"})
        else:
            return jsonify({"msg":"failed"})
    except Exception as e:
        print(str(e))
        return jsonify({"msg":"failed"})


@app.route("/register", methods=["POST", "GET"])
def register():
    try:
        content = request.json
        print(str(content))
        email = content['email']
        psw = content['password']
        emails = email.split("@")
        return jsonify({"msg":"success","email":email,"username":emails[0]})
    except Exception as e:
        print(str(e))
        return jsonify({"msg":"failed"})



# run Server
if __name__ == "__main__":
    app.run(
        host = os.getenv("app_host"), 
        port = os.getenv("app_port"), 
        debug = os.getenv("app_debug")
    )