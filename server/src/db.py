from flask_restful import Resource
import mysql.connector
import os
from flask import request
import json
from loguru import logger

logger.add('logs/db.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="db", colorize=True, level='DEBUG')


def init():
    try:
        conn = mysql.connector.connect(
            host = os.getenv('db_host'),
            port = os.getenv('db_port'),
            user = os.getenv('db_user'),
            password = os.getenv('db_password'),
            database = os.getenv('db_database')
        )
        return conn
    except Exception as ex:
        logger.error("Exception | init: "+str(ex))
        return "failed"


class create_db(Resource):
    def get(self):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, fname VARCHAR(255), lname VARCHAR(255), email VARCHAR(255) UNIQUE, number VARCHAR(15), childname VARCHAR(255), childage VARCHAR(3), password VARCHAR(50), reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                cur.execute("CREATE TABLE IF NOT EXISTS scores (user_id VARCHAR(5), game VARCHAR(25), score VARCHAR(5), PRIMARY KEY (user_id, game));")
                cur.execute("CREATE TABLE IF NOT EXISTS dyscalculia_score (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(20), correct INT(2), wrong INT(2), duration INT(5), accuracy VARCHAR(10), points INT(5), cuurent_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                cur.execute("CREATE TABLE IF NOT EXISTS dysgraphia_score (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(20), duration INT(5), accuracy VARCHAR(10), letter_word VARCHAR(10), points INT(5), cuurent_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                cur.execute("CREATE TABLE IF NOT EXISTS dyslexia_easy_score (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(20), duration INT(5), accuracy VARCHAR(10), points INT(5), cuurent_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                cur.execute("CREATE TABLE IF NOT EXISTS dyslexia_hard_score (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(20), duration INT(5), accuracy VARCHAR(10), points INT(5), cuurent_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                conn.commit()
                cur.close()
                conn.close()
                return "DB Created Successfully!"
            else:
                logger.error("DB Connection Failed!")
                return "Error | DB Connection Failed!"
        except Exception as ex:
            logger.error("Exception | create_db: "+str(ex))
            return "Error | Exception: "+str(ex)


class clear_db(Resource):
    def get(self):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("DROP TABLE IF EXISTS users;")
                cur.execute("DROP TABLE IF EXISTS scores;")
                cur.execute("DROP TABLE IF EXISTS dyscalculia_score;")
                cur.execute("DROP TABLE IF EXISTS dysgraphia_score;")
                cur.execute("DROP TABLE IF EXISTS dyslexia_easy_score;")
                cur.execute("DROP TABLE IF EXISTS dyslexia_hard_score;")
                conn.commit()
                cur.close()
                conn.close()
                return "DB Cleared Successfully!"
            else:
                logger.error("DB Connection Failed!")
                return "Error | DB Connection Failed!"
        except Exception as ex:
            logger.error("Exception | clear_db: "+str(ex))
            return "Error | Exception: "+str(ex)


class ExecuteQuery():
    def execute(query):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute(query)
                conn.commit()
                cur.close()
                conn.close()
                return "success"
            else:
                logger.error("DB Connection Failed!")
                return "Error | DB Connection Failed!"
        except Exception as ex:
            logger.error("Exception | ExecuteQuery: "+str(ex))
            return "Error | Exception: "+str(ex)


class ExecuteSelectQuery():
    def execute(query):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute(query)
                select = cur.fetchall()
                cur.close()
                conn.close()
                return select
            else:
                logger.error("DB Connection Failed!")
                return "Error | DB Connection Failed!"
        except Exception as ex:
            logger.error("Exception | ExecuteSelectQuery: "+str(ex))
            return "Error | Exception: "+str(ex)


class GetAllFromDB(Resource):
    def get(self):
        table = request.args.get('table')
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("SELECT * FROM "+str(table)+";")
                all = cur.fetchall()
                conn.close()
                cur.close()
                return str(all)
            else:
                logger.error("DB Connection Failed!")
                return "DB Connection Failed!"
        except Exception as ex:
            logger.error("Exception | GetAllFromDB: "+str(ex))
            return "Error | Exception: "+str(ex)



