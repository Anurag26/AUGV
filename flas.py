#!/usr/lib/python3.5
from flask import Flask, jsonify,request
from flask_api import FlaskAPI
from firebase import firebase
app = Flask(__name__)
TotDistance=""
IndividualStepDistance=""
Instructions=""
ManeuverBearings=""
ManeuverLongLat=""
TotalSteps=""
app1= firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)


@app.route('/', methods=["GET"])
def hello_world():
    #app=firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
    #result=app.get('/json','routes')
    #legs=jsonify(result['legs'])
    return "Pease do not hack us this is a college project"

@app.route('/start/<flag>/', methods=["POST"])
def start_navigate(flag):
    if flag=='1':
        print("Total distance")
        Routes = app1.get('/json','routes')
        Legs = Routes['legs']
        global TotDistance
        TotDistance= Legs['distance']
        global IndividualStepDistance
        IndividualStepDistance= Legs['IndividualStepDistance']
        global Instructions
        Instructions= Legs['Instructions']
        global ManeuverBearings
        ManeuverBearings= Legs['ManeuverBearings']
        global ManeuverLongLat
        ManeuverLongLat= Legs['ManeuverLongLat']
        global TotalSteps
        TotalSteps= Legs['TotalSteps']
            
        return 'successful'
        
    else:
        return 'STop nav'


if __name__ == '__main__':
    app.debug = True
    app.run()
