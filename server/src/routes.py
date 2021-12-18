from .db import init_db

def initialize_routes(api):
    api.add_resource(init_db, "/init_db")