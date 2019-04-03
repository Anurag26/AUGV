# -*- coding: utf-8 -*-
"""
Created on Wed Apr  3 14:40:35 2019

@author: admin
"""

from firebase import firebase
import gpss 
import magnetometer 
import math 
import threading 
ManeuverLongLat = 0 
TotDistance=""
IndividualStepDistance=""
Instructions=""
ManeuverBearings=""
ManeuverLongLat=""
TotalSteps=""
global counter 
counter = 0 
global distance 
distance = 0
global delta 
delta = 0  
global flag
flag = 1 
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
#result = app.get('/json','routes')
#print(result)
Routes = app.get('/json','routes')
Legs = Routes['legs']
ManeuverLongLat = Legs['ManeuverLongLat']
ManeuverBearings = Legs['ManeuverBearings']
NumberOfSteps = Legs["TotalSteps"]
print(ManeuverLongLat)
print(ManeuverBearings)
print(NumberOfSteps)
def take_a_turn(x):
    print(delta) 


def path_verification():
    
    step_counter = counter
    distance_threshold = 0.25
    #counter = 0 
    delta = magnetometer.bearings(gpss.ManeuverBearings[step_counter])
    take_a_turn(delta)
    if distance < distance_threshold:
        step_counter = step_counter+1
    if step_counter >= NumberOfSteps:
        flag = 0
        print(flag)

def navigation():
    
    step_counter = counter
    gpss.gpsloatlong()
    #get the current position
    current_lat = gpss.lat_scale
    current_lon = gpss.lon_scale
    #get the target position
    target_lat = ManeuverLongLat[step_counter * 2]
    target_lon = ManeuverLongLat[step_counter * 2 + 1]
    #get the distance to the destination using distance formula
    distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)
    print(distance)
    delta = magnetometer.bearings(gpss.ManeuverBearings[step_counter])
    print(delta)
    threading.Timer(0.5, path_verification).start()
    if flag == 0:
        print("End of navigation")

#method for path correction

    
