import mysql.connector
from dotenv import load_dotenv
import os

def init():
    db = mysql.connector.connect(
        host = os.getenv("db_host"),
        user = os.getenv("db_user"),
        password = os.getenv("db_password"),
        database = os.getenv("db_database"),
    )

    return db


def insert(table, attributes, values):
    db = init()
    db_cursor = db.cursor()
    db_cursor.execute("CREATE TABLE customers (name VARCHAR(255), address VARCHAR(255))")
