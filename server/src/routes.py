from .user import register, login

def initialize_routes(api):
    api.add_resource(register, "/register")
    api.add_resource(login, "/login")