from flask_restful import Resource
from flask import jsonify, request, render_template, make_response
from loguru import logger
from dotenv import load_dotenv
from loguru import logger
from datetime import datetime
from time import strftime
from time import gmtime
import sys, os

from src.db import ExecuteQuery, ExecuteSelectQuery

logger.add('logs/scores.log', format='{time:YYYY-MM-DD at HH:mm:ss} | {level} | {message}', filter="scores", colorize=True, level='DEBUG')

# load .env variables
load_dotenv()

class update_scores(Resource):
    def post(self):
        msg = ""
        response = ""

        try:
            content = request.json

            user_id = content['user_id']
            game = content['game']
            score = content['score']

            update_query = "UPDATE scores SET score='"+score+"' WHERE user_id='"+user_id+"' AND game='"+game+"';"
            query_response = ExecuteQuery.execute(update_query)

            if(query_response == "success"):
                msg = "success"
                response = "Score updated successfully!"
            else:
                msg = "failed"
                response = "Score update failed!"

            logger.info("Response | update_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | update_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            

class get_scores(Resource):
    def post(self):
        
        msg = ""
        response = ""
        dyscalculia = ""
        dysgraphia = ""
        dyslexia = ""

        try:
            content = request.json
            user_id = content['user_id']
            select_response = ExecuteSelectQuery.execute("SELECT score FROM scores WHERE user_id='"+user_id+"' ORDER BY game;")
                
            if(len(select_response) > 0):
                dyscalculia = select_response[0][0]
                dysgraphia = select_response[1][0]
                dyslexia = select_response[2][0]
                msg = "success"
                response = "Scores retrieved!"
            else:
                msg = "failed"
                response = "Score retrieval failed!"

            logger.info("Response | get_scores: "+response)
            return jsonify({"msg":msg, "response":response, "dyscalculia":dyscalculia, "dysgraphia":dysgraphia, "dyslexia":dyslexia})
            
        except Exception as e:
            msg = "failed"
            response = str(e)
            logger.error("Exception | get_scores: "+response)
            return jsonify({"msg":msg, "response":response})
            

class get_reports(Resource):
    def get(self, user_id, report_type):

        date = datetime.today().strftime('%Y/%m/%d')
        headers = {'Content-Type': 'text/html'}
        try:
            if(report_type == "insertdb"):
                query_response = ExecuteQuery.execute("INSERT INTO users (fname, lname, email, number, childname, childage, password) VALUES ('Test 1', 'One', 'test1@nanasa.com', '0123456789', 'Child 1', '5', 'Testing1');")
                query_response = ExecuteQuery.execute("INSERT INTO dyscalculia_score (user_id, level, correct, wrong, duration, accuracy, points) VALUES ('1', 'Easy', 3, 2, 613, '86.2', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyscalculia_score (user_id, level, correct, wrong, duration, accuracy, points) VALUES ('1', 'Medium', 3, 2, 577, '35.7', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyscalculia_score (user_id, level, correct, wrong, duration, accuracy, points) VALUES ('1', 'Hard', 3, 2, 845, '54.8', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Easy', 300, '5.5', 'Easy_1', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Medium', 500, '23.5', 'Medium_1', 8);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Easy', 300, '64.4', 'Easy_2', 6);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Medium', 400, '82.3', 'Medium_2', 3);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Easy', 657, '12.7', 'Easy_3', 7);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Medium', 875, '22.2', 'Medium_3', 9);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Easy', 634, '92.5', 'Easy_4', 3);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Medium', 260, '42.9', 'Medium_4', 6);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Easy', 350, '75.8', 'Easy_5', 8);")
                query_response = ExecuteQuery.execute("INSERT INTO dysgraphia_score (user_id, level, duration, accuracy, letter_word, points) VALUES ('1', 'Medium', 847, '27.5', 'Medium_5', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_easy_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Easy', 222, '37.6', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_hard_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Easy', 8674, '56.3', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_easy_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Medium', 456, '83.4', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_hard_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Medium', 1235, '47.2', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_easy_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Hard', 782, '62.8', 5);")
                query_response = ExecuteQuery.execute("INSERT INTO dyslexia_hard_score (user_id, level, duration, accuracy, points) VALUES ('1', 'Hard', 666, '12.2', 5);")
                return "Queries inserted"

            user_response = ExecuteSelectQuery.execute("SELECT fname, lname, childname, childage FROM users WHERE id="+user_id+";")
            FirstName = user_response[0][0]
            LastName = user_response[0][1]
            ChildName = user_response[0][2]
            ChildAge = user_response[0][3]

            if(report_type == "dyscalculia"):
                report_response = ExecuteSelectQuery.execute("SELECT level, correct, wrong, duration, points FROM dyscalculia_score WHERE user_id='"+user_id+"';")
                logger.info(str(report_response))
                if not report_response:
                    logger.info("Empty")
                    return "Empty"
                else:
                    Totalpoints = 0
                    Totaltime = 0
                    Totalcorrect = 0
                    Totalwrong = 0
                    Status = ""
                    Stage = ""
                    Accuracy = 0
                    Easy_Totalpoints = 0
                    Easy_Totaltime = 0
                    Easy_Totalcorrect = 0
                    Easy_Totalwrong = 0
                    Easy_Accuracy = 0
                    Medium_Totalpoints = 0
                    Medium_Totaltime = 0
                    Medium_Totalcorrect = 0
                    Medium_Totalwrong = 0
                    Medium_Accuracy = 0
                    Hard_Totalpoints = 0
                    Hard_Totaltime = 0
                    Hard_Totalcorrect = 0
                    Hard_Totalwrong = 0
                    Hard_Accuracy = 0
                    for row in report_response:
                        Totalpoints += row[4]
                        Totaltime += row[3]
                        Totalcorrect += row[1]
                        Totalwrong += row[2]
                        if row[0] == "Easy":
                            Easy_Totalpoints = row[4]
                            Easy_Totaltime = row[3]
                            Easy_Totalcorrect = row[1]
                            Easy_Totalwrong = row[2]
                        elif row[0] == "Medium":
                            Medium_Totalpoints = row[4]
                            Medium_Totaltime = row[3]
                            Medium_Totalcorrect = row[1]
                            Medium_Totalwrong = row[2]
                        elif row[0] == "Hard":
                            Hard_Totalpoints = 0
                            Hard_Totaltime = row[3]
                            Hard_Totalcorrect = row[1]
                            Hard_Totalwrong = row[2]

                    count = Totalcorrect+Totalwrong
                    Easy = Easy_Totalcorrect+Easy_Totalwrong
                    Medium = Medium_Totalcorrect+Medium_Totalwrong
                    Hard = Hard_Totalcorrect+Hard_Totalwrong
                    if count != 0:
                        Accuracy = round(Totalcorrect/(count)*100, 2)
                    if Easy != 0:
                        Easy_Accuracy = round(Easy_Totalcorrect/(Easy)*100, 2)
                    if Medium != 0:
                        Medium_Accuracy = round(Medium_Totalcorrect/(Medium)*100, 2)
                    if Hard != 0:
                        Hard_Accuracy = round(Hard_Totalcorrect/(Hard)*100, 2)
                    
                    if Accuracy > 80:
                        Status = "Above 80"
                        Stage = "Above 80"
                    elif Accuracy > 60:
                        Status = "Above 60"
                        Stage = "Above 60"
                    elif Accuracy > 40:
                        Status = "Above 40"
                        Stage = "Above 40"
                    else:
                        Status = "Below 40"
                        Stage = "Below 40"

                    return make_response(render_template('./Dyscalculia.html', 
                    FirstName=FirstName, LastName=LastName, ChildName=ChildName, ChildAge=ChildAge, Date=date,
                    Totalpoints=Totalpoints, Totaltime=strftime("%H:%M:%S", gmtime(Totaltime)), Totalcorrect=Totalcorrect, Totalwrong=Totalwrong, Status=Status, Stage=Stage, Accuracy=Accuracy,
                    Easy_Totalpoints=Easy_Totalpoints, Easy_Totaltime=strftime("%H:%M:%S", gmtime(Easy_Totaltime)), Easy_Totalcorrect=Easy_Totalcorrect, Easy_Totalwrong=Easy_Totalwrong, Easy_Accuracy=Easy_Accuracy,
                    Medium_Totalpoints=Medium_Totalpoints, Medium_Totaltime=strftime("%H:%M:%S", gmtime(Medium_Totaltime)), Medium_Totalcorrect=Medium_Totalcorrect, Medium_Totalwrong=Medium_Totalwrong, Medium_Accuracy=Medium_Accuracy,
                    Hard_Totalpoints=Hard_Totalpoints, Hard_Totaltime=strftime("%H:%M:%S", gmtime(Hard_Totaltime)), Hard_Totalcorrect=Hard_Totalcorrect, Hard_Totalwrong=Hard_Totalwrong, Hard_Accuracy=Hard_Accuracy
                    ), 200, headers)

            elif(report_type == "dysgraphia"):
                report_response = ExecuteSelectQuery.execute("SELECT level, duration, accuracy, letter_word, points FROM dysgraphia_score WHERE user_id='"+user_id+"' ORDER BY level;")
                logger.info(str(report_response))
                if not report_response:
                    logger.info("Empty")
                    return "Empty"
                else:
                    Totalpoints = 0
                    Totaltime = 0
                    Status = ""
                    Stage = ""
                    Accuracy = 0
                    Easy_Accuracy = 0
                    Medium_Accuracy = 0
                    Easy_Accuracy_1 = 0
                    Medium_Accuracy_1 = 0
                    Easy_Accuracy_2 = 0
                    Medium_Accuracy_2 = 0
                    Easy_Accuracy_3 = 0
                    Medium_Accuracy_3 = 0
                    Easy_Accuracy_4 = 0
                    Medium_Accuracy_4 = 0
                    Easy_Accuracy_5 = 0
                    Medium_Accuracy_5 = 0
                    Easy = 0
                    Easy_1 = 0
                    Easy_2 = 0
                    Easy_3 = 0
                    Easy_4 = 0
                    Easy_5 = 0
                    Medium = 0
                    Medium_1 = 0
                    Medium_2 = 0
                    Medium_3 = 0
                    Medium_4 = 0
                    Medium_5 = 0
                    Easy_Time_1 = 0
                    Easy_Time_2 = 0
                    Easy_Time_3 = 0
                    Easy_Time_4 = 0
                    Easy_Time_5 = 0
                    Medium_Time_1 = 0
                    Medium_Time_2 = 0
                    Medium_Time_3 = 0
                    Medium_Time_4 = 0
                    Medium_Time_5 = 0
                    for row in report_response:
                        Totalpoints += row[4]
                        Totaltime += row[1]
                        Accuracy += float(row[2])

                        if row[0] == "Easy":
                            Easy_Accuracy += float(row[2])
                            Easy += 1
                        elif row[0] == "Medium":
                            Medium_Accuracy += float(row[2])
                            Medium += 1

                        if row[3] == "Easy_1":
                            Easy_Accuracy_1 += float(row[2])
                            Easy_Time_1 += row[1]
                            Easy_1 += 1
                        elif row[3] == "Easy_2":
                            Easy_Accuracy_2 += float(row[2])
                            Easy_Time_2 += row[1]
                            Easy_2 += 1
                        elif row[3] == "Easy_3":
                            Easy_Accuracy_3 += float(row[2])
                            Easy_Time_3 += row[1]
                            Easy_3 += 1
                        elif row[3] == "Easy_4":
                            Easy_Accuracy_4 += float(row[2])
                            Easy_Time_4 += row[1]
                            Easy_4 += 1
                        elif row[3] == "Easy_5":
                            Easy_Accuracy_5 += float(row[2])
                            Easy_Time_5 += row[1]
                            Easy_5 += 1
                        elif row[3] == "Medium_1":
                            Medium_Accuracy_1 = float(row[2])
                            Medium_Time_1 = row[1]
                            Medium_1 += 1
                        elif row[3] == "Medium_2":
                            Medium_Accuracy_2 += float(row[2])
                            Medium_Time_2 += row[1]
                            Medium_2 += 1
                        elif row[3] == "Medium_3":
                            Medium_Accuracy_3 += float(row[2])
                            Medium_Time_3 += row[1]
                            Medium_3 += 1
                        elif row[3] == "Medium_4":
                            Medium_Accuracy_4 += float(row[2])
                            Medium_Time_4 += row[1]
                            Medium_4 += 1
                        elif row[3] == "Medium_5":
                            Medium_Accuracy_5 += float(row[2])
                            Medium_Time_5 += row[1]
                            Medium_5 += 1

                    report_response_size = len(report_response)
                    if report_response_size != 0:
                        Accuracy = round(Accuracy/report_response_size, 2)
                    if Easy != 0:
                        Easy_Accuracy = round(Easy_Accuracy/Easy, 2)
                    if Medium != 0:
                        Medium_Accuracy = round(Medium_Accuracy/Medium, 2)
                    if Easy_1 != 0:
                        Easy_Accuracy_1 = round(Easy_Accuracy_1/Easy_1, 2)
                    if Medium_1 != 0:
                        Medium_Accuracy_1 = round(Medium_Accuracy_1/Medium_1, 2)
                    if Easy_2 != 0:
                        Easy_Accuracy_2 = round(Easy_Accuracy_2/Easy_2, 2)
                    if Medium_2 != 0:
                        Medium_Accuracy_2 = round(Medium_Accuracy_2/Medium_2, 2)
                    if Easy_3 != 0:
                        Easy_Accuracy_3 = round(Easy_Accuracy_3/Easy_3, 2)
                    if Medium_3 != 0:
                        Medium_Accuracy_3 = round(Medium_Accuracy_3/Medium_3, 2)
                    if Easy_4 != 0:
                        Easy_Accuracy_4 = round(Easy_Accuracy_4/Easy_4, 2)
                    if Medium_4 != 0:
                        Medium_Accuracy_4 = round(Medium_Accuracy_4/Medium_4, 2)
                    if Easy_5 != 0:
                        Easy_Accuracy_5 = round(Easy_Accuracy_5/Easy_5, 2)
                    if Medium_5 != 0:
                        Medium_Accuracy_5 = round(Medium_Accuracy_5/Medium_5, 2)
                    
                    if Accuracy > 80:
                        Status = "Above 80"
                        Stage = "Above 80"
                    elif Accuracy > 60:
                        Status = "Above 60"
                        Stage = "Above 60"
                    elif Accuracy > 40:
                        Status = "Above 40"
                        Stage = "Above 40"
                    else:
                        Status = "Below 40"
                        Stage = "Below 40"

                    return make_response(render_template('./Dysgraphia.html', 
                    FirstName=FirstName, LastName=LastName, ChildName=ChildName, ChildAge=ChildAge, Date=date,
                    Totalpoints=Totalpoints, Totaltime=strftime("%H:%M:%S", gmtime(Totaltime)), Status=Status, Stage=Stage, Accuracy=Accuracy, Easy_Accuracy=Easy_Accuracy, Medium_Accuracy=Medium_Accuracy,
                    Easy_Accuracy_1=Easy_Accuracy_1, Easy_Time_1=strftime("%H:%M:%S", gmtime(Easy_Time_1)), Easy_Accuracy_2=Easy_Accuracy_2, Easy_Time_2=strftime("%H:%M:%S", gmtime(Easy_Time_2)), Easy_Accuracy_3=Easy_Accuracy_3, Easy_Time_3=strftime("%H:%M:%S", gmtime(Easy_Time_3)), Easy_Accuracy_4=Easy_Accuracy_4, Easy_Time_4=strftime("%H:%M:%S", gmtime(Easy_Time_4)), Easy_Accuracy_5=Easy_Accuracy_5, Easy_Time_5=strftime("%H:%M:%S", gmtime(Easy_Time_5)),
                    Medium_Accuracy_1=Medium_Accuracy_1, Medium_Time_1=strftime("%H:%M:%S", gmtime(Medium_Time_1)), Medium_Accuracy_2=Medium_Accuracy_2, Medium_Time_2=strftime("%H:%M:%S", gmtime(Medium_Time_2)), Medium_Accuracy_3=Medium_Accuracy_3, Medium_Time_3=strftime("%H:%M:%S", gmtime(Medium_Time_3)), Medium_Accuracy_4=Medium_Accuracy_4, Medium_Time_4=strftime("%H:%M:%S", gmtime(Medium_Time_4)), Medium_Accuracy_5=Medium_Accuracy_5, Medium_Time_5=strftime("%H:%M:%S", gmtime(Medium_Time_5)), 
                    ), 200, headers)

            elif(report_type == "dyslexia_easy" or report_type == "dyslexia_hard"):
                report_response = ""
                template = ""
                if(report_type == "dyslexia_easy"):
                    report_response = ExecuteSelectQuery.execute("SELECT level, duration, accuracy, points FROM dyslexia_easy_score WHERE user_id='"+user_id+"';")
                    template = "./Dyslexia - Easy.html"
                elif(report_type == "dyslexia_hard"):
                    report_response = ExecuteSelectQuery.execute("SELECT level, duration, accuracy, points FROM dyslexia_hard_score WHERE user_id='"+user_id+"';")
                    template = "./Dyslexia - Hard.html"
                logger.info(str(report_response))
                if not report_response:
                    logger.info("Empty")
                    return "Empty"
                else:
                    Totalpoints = 0
                    Totaltime = 0
                    Status = ""
                    Stage = ""
                    Accuracy = 0
                    Easy = 0
                    Easy_Accuracy = 0
                    Easy_Time = 0
                    Easy_Points = 0
                    Medium = 0
                    Medium_Accuracy = 0
                    Medium_Time = 0
                    Medium_Points = 0
                    Hard = 0
                    Hard_Accuracy = 0
                    Hard_Time = 0
                    Hard_Points = 0

                    for row in report_response:
                        Totalpoints += row[3]
                        Totaltime += row[1]
                        Accuracy += float(row[2])

                        if row[0] == "Easy":
                            Easy_Points += row[3]
                            Easy_Time += row[1]
                            Easy_Accuracy += float(row[2])
                            Easy += 1
                        elif row[0] == "Medium":
                            Medium_Points += row[3]
                            Medium_Time += row[1]
                            Medium_Accuracy += float(row[2])
                            Medium += 1
                        elif row[0] == "Hard":
                            Hard_Points += row[3]
                            Hard_Time += row[1]
                            Hard_Accuracy += float(row[2])
                            Hard += 1

                    report_response_size = len(report_response)
                    Accuracy = round(Accuracy/report_response_size, 2)
                    if Easy != 0:
                        Easy_Accuracy = round(Easy_Accuracy/Easy, 2)
                    if Medium != 0:
                        Medium_Accuracy = round(Medium_Accuracy/Medium, 2)
                    if Hard != 0:
                        Hard_Accuracy = round(Hard_Accuracy/Hard, 2)

                    if Accuracy > 80:
                        Status = "Above 80"
                        Stage = "Above 80"
                    elif Accuracy > 60:
                        Status = "Above 60"
                        Stage = "Above 60"
                    elif Accuracy > 40:
                        Status = "Above 40"
                        Stage = "Above 40"
                    else:
                        Status = "Below 40"
                        Stage = "Below 40"

                    return make_response(render_template(template, 
                    FirstName=FirstName, LastName=LastName, ChildName=ChildName, ChildAge=ChildAge, Date=date,
                    Totalpoints=Totalpoints, Totaltime=strftime("%H:%M:%S", gmtime(Totaltime)), Status=Status, Stage=Stage, Accuracy=Accuracy,
                    Easy_Points=Easy_Points, Easy_Time=strftime("%H:%M:%S", gmtime(Easy_Time)), Easy_Accuracy=Easy_Accuracy,
                    Medium_Points=Medium_Points, Medium_Time=strftime("%H:%M:%S", gmtime(Medium_Time)), Medium_Accuracy=Medium_Accuracy,
                    Hard_Points=Hard_Points, Hard_Time=strftime("%H:%M:%S", gmtime(Hard_Time)), Hard_Accuracy=Hard_Accuracy,
                    ), 200, headers)

            return "Report type not specified"
            
        except Exception as e:
            msg = "failed"
            response = str(e)
            exc_type, exc_obj, exc_tb = sys.exc_info()
            logger.error("Exception | get_reports: "+response+"\nType: "+str(exc_type)+"\nLine: "+str(exc_tb.tb_lineno))
            return jsonify({"msg":msg, "response":response})
            


