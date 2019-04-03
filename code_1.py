import gpss
import magnetometer
import json
import math
import time, threading
import RPi.GPIO as gpio
gpio.setmode(gpio.BCM)

motor_fwd = [16, 18]	#pins to run the motors forward
motor_rvs = [22, 24]	#pins to run the motors reverse
gpio.setup(motor_fwd, gpio.OUT)
gpio.setup(motor_rvs, gpio.OUT)

#section to get the route JSON data
with open("JSON_local.json") as json_file:
    text = json_file.read()
    json_data = json.loads(text)
    json = json_data['json']
    Routes = json['routes']
    Legs = Routes['legs']
    ManeuverLongLat = Legs['ManeuverLongLat']
    ManeuverBearings = Legs['ManeuverBearings']
    NumberOfSteps = Legs["TotalSteps"]

global current_lat, current_lon, target_lat, target_lon, target_bearing, delta
step_counter = 0
global distance_threshold
distance_threshold = 0.25
global distance

def navigation():
    gpss.gpsloatlong()
    #get the current position
    current_lat = gpss.lat_scale()
    current_lon = gpss.lon_scale()
    #get the target position
    target_lat = ManeuverLongLat[step_counter * 2]
    target_lon = ManeuverLongLat[step_counter * 2 + 1]
    #get the distance to the destination using distance formula
    distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)
    delta = magnetometer.bearings(gpss.ManeuverBearings[step_counter])
    take_a_turn(delta)
    while(distance > distance_threshold):
        print("Origin point error")
    gpio.output(motor_fwd, gpio.HIGH)
    gpio.output(motor_rvs, gpio.LOW)
	
    threading.Timer(0.5, path_verification).start()
    #call path correction/verification function every 0.5 seconds

    if flag == 0:
        print("End of navigation")





##################################################################################################
#method for taking a turn
def take_a_turn():
    gpio.output(motor_fwd, gpio.HIGH)
    gpio.output(motor_rvs, gpio.LOW)


#method for path correction
def path_verification():
	delta = magnetometer.bearings(gpss.ManeuverBearings[step_counter])
	take_a_turn(delta)
	if distance < distance_threshold:
		counter = counter+1
	if counter >= NumberOfSteps:
		flag = 0
