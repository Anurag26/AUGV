import time
from firebase import firebase
import gpss
import magnetometerServo
import math
import schedule
import RPi.GPIO as GPIO
import RPi.GPIO as gpio
import mag_calibr
import servo_test
ManeuverLongLat = 0
TotDistance=""
IndividualStepDistance=""
Instructions=""
ManeuverBearings=""
ManeuverLongLat=""
TotalSteps=""

servoPIN = 13
motorpin = 21
GPIO.setmode(GPIO.BCM)
GPIO.setup(servoPIN, GPIO.OUT)
GPIO.setup(motorpin, GPIO.OUT)
global servo_pwm , motor_pwm 
servo_pwm = GPIO.PWM(servoPIN, 50) # GPIO 17 for PWM with 50Hz
motor_pwm = GPIO.PWM(motorpin, 50)
motor_pwm.start(0)
servo_pwm.start(6.25)

global counter
counter = 0
global distance
distance = 0
global delta
delta = 0
global flag
flag = 1
step_counter = 1
maxandmin = mag_calibr.calibrate_the_compass()
magnetometerServo.update(maxandmin)
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
def compare_value(step_count):
    #try:
    current_lat = 0
    current_lon = 0
    global distance_threshold
    distance_threshold= 2 
    '''while current_lat == 0 and current_lon == 0:
        motor_pwm.ChangeDutyCycle(0)
        latlongs=gpss.gpsloatlong()
        current_lat = latlongs[0]
        current_lon = latlongs[1]'''
    global step_counter
    if step_counter < step_count+1:
        target_lat = ManeuverLongLat[step_counter * 2]
        target_lon = ManeuverLongLat[step_counter * 2 + 1]
        #distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)*111000
        
    
    #print(ManeuverBearings[step_counter - ])
#    print(delta)
    #gpio.setup(13,gpio.OUT)
    #gpio.output(13, True)
    #d1 = compare_value()
    
        #print("Move Forward ", distance)
        #print("Firebase bearings: ", ManeuverBearings[step_counter - 1])
        #current_bearings = magnetometerServo.bearings()
    print("calling servo_test")
    servo_test.servo_testing(target_lat,target_lon,servo_pwm,motor_pwm,ManeuverBearings[step_counter-1])       
        #motor_pwm.ChangeDutyCycle(50)
        #gpio.output(21, gpio.HIGH)
        #print("Face ", b)
        
        
    print("Reached destination", distance)
    motor_pwm.ChangeDutyCycle(0)
    step_counter = step_counter + 1
    print("Step counter incremented to ", step_counter)
    if step_counter == step_count + 1:
        print("Navigation complete")
        #gpio.cleanup()
        motor_pwm.ChangeDutyCycle(0)
       # schedule.clear('nav')
        #print("Schedule cleared")            

    #except:
    #    print("Exception occured in compare_value")

#schedule.every(2).seconds.do(compare_value, NumberOfSteps).tag('nav')

while step_counter <= NumberOfSteps + 1:
    compare_value(NumberOfSteps)
    # Checks whether a scheduled task
    # is pending to run or not
 #   schedule.run_pending()
#time.sleep(1)

