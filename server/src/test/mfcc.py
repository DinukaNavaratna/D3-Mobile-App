import librosa
import librosa.display
import matplotlib.pyplot as plt
from dtw import dtw
from numpy.linalg import norm
from flask_restful import Resource
from flask import request, jsonify
from loguru import logger
import sys

class compare(Resource):
    def get(self):
        try:
                content = request.json
                file1 = content['file1']
                file2 = content['file2']
                #file1 = "src/Recordings/අකුරු කියමු.wav"
                #file2 = "clips/456.wav"

                #Loading audio files
                y1, sr1 = librosa.load(file1) 
                y2, sr2 = librosa.load(file2)

                #Showing multiple plots using subplot
        #        plt.subplot(1, 2, 1) 
                mfcc1 = librosa.feature.mfcc(y1,sr1)   #Computing MFCC values
        #        librosa.display.specshow(mfcc1)

        #        plt.subplot(1, 2, 2)
                mfcc2 = librosa.feature.mfcc(y2, sr2)
        #        librosa.display.specshow(mfcc2)

                #dist, cost, path = dtw(mfcc1.T, mfcc2.T)
                dist, cost, acc_cost, path = dtw(mfcc1.T, mfcc2.T, dist=lambda x, y: norm(x - y, ord=1))

        #        plt.imshow(cost.T, origin='lower', cmap=plt.get_cmap('gray'), interpolation='nearest')
        #        plt.plot(path[0], path[1], 'w')   #creating plot for DTW

        #        plt.show()  #To display the plots graphically

                #Assumption: taking the distance 100000 as the margin to measure the difference.
                if dist < 100000:
                        accuracy = ((100000-dist)/1000)
                else:
                        accuracy = 0
                        print("The normalized distance between the two : ", dist)   # 0 for similar audios 
                return jsonify({"msg":"success", "accuracy":"%.2f" % accuracy+"%"})
        except Exception as e:
            response = str(e)
            exc_type, exc_obj, exc_tb = sys.exc_info()
            logger.error("Exception | mfcc compare: "+response+"\nType: "+str(exc_type)+"\nLine: "+str(exc_tb.tb_lineno))
            return jsonify({"msg":"failed", "response":response})