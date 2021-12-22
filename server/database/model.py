from dotenv import load_dotenv
import os

from database.init import init

# load .env variables
load_dotenv()

# Create DB
def create():
    try:
        conn = init()

        if conn != "failed":
            cur = conn.cursor()
            cur.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, fname VARCHAR(255), lname VARCHAR(255), email VARCHAR(255), number VARCHAR(15), childname VARCHAR(255), childage VARCHAR(2), password VARCHAR(255));")
            conn.commit()
            cur.close()
            conn.close()
        return "DB Created Successfully!"
    except Exception as ex:
        print(str(ex))
        return "DB Creation Error!\n"+str(ex)