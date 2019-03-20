#source link: https://medium.com/@DefCon_007/using-a-gps-module-neo-7m-with-raspberry-pi-3-45100bc0bb41
#The code has been slihgtly modified from the one on the website

import time
import serial
import string
import pynmea2
import RPi.GPIO as gpio

gpio.setmode(gpio.BCM)
 
port = "/dev/ttyAMA0" # the serial port to which the pi is connected.
 
#create a serial object
ser = serial.Serial(port, baudrate = 9600, timeout = 0.5)
 
while 1:
    try:
        data = ser.readline()
    except:
	print("loading") 
	#wait for the serial port to churn out data

    if data[0:6] == '$GPGGA': # the long and lat data are always contained in the GPGGA string of the NMEA data 
        msg = pynmea2.parse(data)
#parse the latitude and print
        latval = msg.lat
	concatlat = "lat:" + str(latval)
        print concatlat

	#parse the longitude and print
	longval = msg.lon
	concatlong = "long:"+ str(longval)
	print concatlong
	time.sleep(0.5)#wait a little before picking the next data.
