from flask_restful import Resource
import mysql.connector
import os
from flask import jsonify, request

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
        print("Exception DB init: "+str(ex))
        return "failed"


class create_db(Resource):
    def get(self):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, fname VARCHAR(255), lname VARCHAR(255), email VARCHAR(255) UNIQUE, degree VARCHAR(255), batch VARCHAR(255), student_id VARCHAR(255) UNIQUE, reg_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);")
                cur.execute("CREATE TABLE IF NOT EXISTS submissions (id INT AUTO_INCREMENT PRIMARY KEY, submission_id INT UNIQUE, student_id VARCHAR(255), submission_name VARCHAR(255), module_name VARCHAR(255), submission_method VARCHAR(255), submission_type VARCHAR(255), submission_status VARCHAR(255), due_date VARCHAR(255), submission_description VARCHAR(255), time_needed INT, time_completed INT DEFAULT 0, compleation_status VARCHAR(25) DEFAULT 'Pending');")
                cur.execute("CREATE TABLE IF NOT EXISTS app_usage (id INT AUTO_INCREMENT PRIMARY KEY, user_id VARCHAR(255), app_name VARCHAR(255), app_id VARCHAR(255), date_time VARCHAR(255), usage_time VARCHAR(255));")
                cur.execute("CREATE TABLE IF NOT EXISTS submissions_workload (id INT AUTO_INCREMENT PRIMARY KEY, submission_id INT, student_id VARCHAR(255), work_day VARCHAR(10), work_date VARCHAR(10), work_duration INT DEFAULT 30, worked_duration INT DEFAULT 0, work_start INT, work_end INT, user_set_duration INT DEFAULT 0)")
                cur.execute("CREATE TABLE IF NOT EXISTS weekly_free_time (week_day VARCHAR(20), free_time INT);")
                conn.commit()
                cur.close()
                conn.close()
                return "DB Created Successfully!"
            else:
                return "DB Connection Failed!"
        except Exception as ex:
            print("Exception: "+str(ex))
            return "Exception: "+str(ex)


class clear_db(Resource):
    def get(self):
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("DROP TABLE IF EXISTS users;")
                cur.execute("DROP TABLE IF EXISTS submissions;")
                cur.execute("DROP TABLE IF EXISTS app_usage;")
                cur.execute("DROP TABLE IF EXISTS submissions_workload;")
                conn.commit()
                cur.close()
                conn.close()
                return "DB Cleared Successfully!"
            else:
                return "DB Connection Failed!"
        except Exception as ex:
            print("Exception: "+str(ex))
            return "Exception: "+str(ex)


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
                return "DB Connection Failed!"
        except Exception as ex:
            print("Exception ExecuteQuery: "+str(ex))
            return "Exception ExecuteQuery: "+str(ex)


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
                return "DB Connection Failed!"
        except Exception as ex:
            print("Exception ExecuteSelectQuery: "+str(ex))
            return "Exception ExecuteSelectQuery: "+str(ex)


class GetAllFromDB(Resource):
    def get(self):
        table = request.args.get('table')
        try:
            conn = init()
            if conn != "failed":
                cur = conn.cursor()
                cur.execute("SELECT * FROM "+str(table)+";")
                users = cur.fetchall()
                conn.close()
                cur.close()
                return users
            else:
                return "DB Connection Failed!"
        except Exception as ex:
            print("Exception: "+str(ex))
            return "Exception: "+str(ex)