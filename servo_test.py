import RPi.GPIO as GPIO
import time
import math
import magnetometer
import mag_calibr
import gpss
def servo_testing(target_lat,target_lon,servo_pwm,motor_pwm):
#The PWM values for the extreme position ranges from 5
#to 12.5. 8.75 is the PWM value for the center position.
#An increment of 0.0625 in the PWM causes a rotation of
#1 degree.
    servoPIN = 13
    motorpin = 21
    motor_pwm.start(0)
    straight=6.25
    distance_threshold=5
    #angle = 0
    servo_pwm.start(2.5) # Initialization
    print("Wheels are straight")
    servo_pwm.start(straight)
    maxandmin=mag_calibr.calibrate_the_compass()
    print("STart moving")
    try:
        global current_lat,current_lon
        current_lat=0
        current_lon=0
        while current_lat == 0 and current_lon == 0:
            motor_pwm.ChangeDutyCycle(0)
            latlongs=gpss.gpsloatlong()
            current_lat = latlongs[0]
            current_lon = latlongs[1]
        cla = math.radians(current_lat)
        clo = math.radians(current_lon)
        nla = math.radians(target_lat)
        nlo = math.radians(target_lon)
        z = nlo-clo
        x = math.cos(nla) * math.sin(z)
        y = math.cos(cla) * math.sin(nla) - (math.sin(cla) * math.cos(nla) * math.cos(z))
        b = math.degrees(math.atan2(x,y))
        distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)*111000
        while distance>distance_threshold:
            motor_pwm.ChangeDutyCycle(50)
            #magnetometer.update(maxandmin)
            angle=magnetometer.bearings()
            angleDiff=angle-b
            if abs(angleDiff) > 40:
                if angleDiff > 0:
                    angleDiff = 40
                else:
                    angleDiff = -40
            print("Angle is ",angle, " Angle diff is ",angleDiff)
            if abs(angleDiff) > 3.0:
                motor_pwm.ChangeDutyCycle(0)
                dc=angleDiff*0.0625*0.15
                print("DC ",dc)
                #if angleDiff < 0:
                finaldc=straight + (dc)
                print("final dc ",finaldc)
                servo_pwm.start(finaldc)
                motor_pwm.ChangeDutyCycle(50)
                '''else:
                    finaldc=straight + (dc)
                    print("final dc ",finaldc)
                    servo_pwm.start(finaldc)
                    motor_pwm.ChangeDutyCycle(10)'''
            else:
                servo_pwm.start(straight)

            print(" end ")
            latlongs=gpss.gpsloatlong()
            current_lat = latlongs[0]
            current_lon = latlongs[1]
            distance = math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)*111000
    except KeyboardInterrupt:
      servo_pwm.stop()
      motor_pwm.stop()
      GPIO.cleanup()
