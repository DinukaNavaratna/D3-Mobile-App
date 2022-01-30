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
                #cur.execute("CREATE TABLE IF NOT EXISTS dyscalculia (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(1), data_id VARCHAR(5) UNIQUE, number VARCHAR(15), childname VARCHAR(255), childage VARCHAR(3), password VARCHAR(50), reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                #cur.execute("CREATE TABLE IF NOT EXISTS dysgraphia (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(1), data_id VARCHAR(5) UNIQUE, number VARCHAR(15), childname VARCHAR(255), childage VARCHAR(3), password VARCHAR(50), reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                #cur.execute("CREATE TABLE IF NOT EXISTS dyslexia (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(5), level VARCHAR(1), data_id VARCHAR(5) UNIQUE, number VARCHAR(15), childname VARCHAR(255), childage VARCHAR(3), password VARCHAR(50), reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                #cur.execute("CREATE TABLE IF NOT EXISTS reports (id INT AUTO_INCREMENT PRIMARY KEY, submission_id INT UNIQUE, student_id VARCHAR(255), submission_name VARCHAR(255), module_name VARCHAR(255), submission_method VARCHAR(255), submission_type VARCHAR(255), submission_status VARCHAR(255), due_date VARCHAR(255), submission_description VARCHAR(255), time_needed INT, time_completed INT DEFAULT 0, compleation_status VARCHAR(25) DEFAULT 'Pending');")
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
                cur.execute("DROP TABLE IF EXISTS reports;")
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



