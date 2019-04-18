# -*- coding: utf-8 -*-
"""
Created on Thu Apr 18 13:20:49 2019

@author: admin
"""
from firebase import firebase
import gpss 
import magnetometer 
import math 
import schedule 
import RPi.GPIO as gpio 
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
global step_counter 
step_counter = 1
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
#result = app.get('/json','routes')
#print(result)
Routes = app.get('/json','routes')
Legs = Routes['legs']
ManeuverLongLat = Legs['ManeuverLongLat']
ManeuverBearings = Legs['ManeuverBearings']
NumberOfSteps = Legs["TotalSteps"]
print(ManeuverLongLat)
#print(ManeuverBearings)
#print(NumberOfSteps)
def compare_value():
    global distance_threshold 
    distance_threshold = 5
    gpss.gpsloatlong()
    current_lat = gpss.lat_scale
    current_lon = gpss.lon_scale
    target_lat = ManeuverLongLat[step_counter * 2]
    target_lon = ManeuverLongLat[step_counter * 2 + 1]
    distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)
    return distance 

gpio.setup(13,gpio.OUT)
gpio.output(13, True)
schedule.every(5).seconds.do(compare_value)
d1 = compare_value()
if d1 < distance_threshold :
    schedule.cancel(compare_value)
    gpio.output(13,False)
    