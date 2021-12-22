import mysql.connector
from dotenv import load_dotenv
import os

# load .env variables
load_dotenv()

# init DB
def init():
    try:
        conn = mysql.connector.connect(
            host = os.getenv('db_host'),
            port = os.getenv('db_post'),
            user = os.getenv('db_user'),
            password = os.getenv('db_password'),
            database = os.getenv('db_database')
        )
        return conn
    except Exception as ex:
        print(str(ex))
        return "failed"