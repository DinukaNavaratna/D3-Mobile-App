from .users import register, login
from .db import create_db, clear_db, GetAllFromDB
from .audio import upload_audio, train_model, analyze
from .scores import update_scores, get_scores
#from .test.analyze_audio import correlation, resemblyzer, audiocompare
from .test.mfcc import compare

def initialize_routes(api):
    api.add_resource(register, "/register")
    api.add_resource(login, "/login")
    api.add_resource(create_db, "/create_db")
    api.add_resource(clear_db, "/clear_db")
    api.add_resource(GetAllFromDB, "/GetAllFromDB")
    api.add_resource(update_scores, "/update_scores")
    api.add_resource(get_scores, "/get_scores")
    api.add_resource(train_model, "/train_model")
    api.add_resource(analyze, "/analyze")
    api.add_resource(upload_audio, "/upload_audio")
    #Testing on-going
    #api.add_resource(correlation, "/correlation")
    #api.add_resource(resemblyzer, "/resemblyzer")
    #api.add_resource(compare, "/mfcc")
    #api.add_resource(audiocompare, "/audiocompare")