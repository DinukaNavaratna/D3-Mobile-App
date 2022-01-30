import imp
from .users import register, login
from .db import create_db, clear_db, GetAllFromDB
from .audio import upload_audio
from .scores import update_scores, get_scores

def initialize_routes(api):
    api.add_resource(register, "/register")
    api.add_resource(login, "/login")
    api.add_resource(create_db, "/create_db")
    api.add_resource(clear_db, "/clear_db")
    api.add_resource(GetAllFromDB, "/GetAllFromDB")
    api.add_resource(upload_audio, "/upload_audio")
    api.add_resource(update_scores, "/update_scores")
    api.add_resource(get_scores, "/get_scores")