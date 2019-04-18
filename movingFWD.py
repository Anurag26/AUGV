# -*- coding: utf-8 -*-
"""
Created on Thu Apr 18 10:38:56 2019

@author: Anurag Bannur
"""

# this program can be used as the basic architecture for making the rover move
# The rover shall retrieve the lat long from firebase, compare and move
# The move occurs only if dist is in the threshold
# Once it reaches the destination it searches for the next one
from firebase import firebase
import json
from geopy import distance
import RPi.GPIO as GPIO
import threading
import turnTheUGV




app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
result = app.get('/json','uuid')
routes=app.get('/json','routes')
legs=routes['legs']
LongLat=legs['ManeuverLongLat'] 
Bearings=legs['ManeuverBearings']
steps=legs['TotalSteps']

def CalculateDistance(End,Start):

    #HERE either use your forumla or try geopy.distance.distance()
    

    return Distace_Error


def startMoving(LongLat,Steps,Bearings):
    LongLat=LongLat
    print("This is ",LongLat,"Steps ",Steps)
    Flag=-1
    Iterator=0
    BearingIterator=0
    DistanceErrorThreshold=20
    while(Flag!=Steps):
        DestinationLongLat=LongLat[Iterator:Iterator+2]
        CurrentLongLat=gpss.py  #Ensure GPSS.py returns a single value
        DistanceError=CalculateDistance(DestinationLongLat,CurrentLongLat)
        if(DistanceError<DistanceErrorThreshold):
            #Bearing_Thread = threading.Thread(target=bearingFunction inside magnetometer.py)
            turnTheUGV.initiateTurning(Bearings,Flag,Steps)
            
        Flag=Flag+1
        Iterator=Iterator+2
        
    
    
startMoving(LongLat,steps,Bearings)    