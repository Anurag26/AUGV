
import time
import serial
import string
import pynmea2
import RPi.GPIO as gpio
def hello_sexy():
    print("Hello")
#counter =  0 
def gpsloatlong():

    while 1:

        gpio.setmode(gpio.BCM)

        port = "/dev/ttyAMA0"

        ser = serial.Serial(port, baudrate= 9600, timeout = 0.5)  
  #  print "hello world"

        try:
            data = ser.readline()
        except:
            print("loading")

        if data[0:6]=='$GPRMC' and '$GPGGA':
            msg = pynmea2.parse(data)
            latval = msg.lat
            lat_int = float(latval)
            lat_scale =  lat_int/100
            print lat_scale

            longval = msg.lon
            lon_float = float(longval)
	    lon_scale = lon_float/100
            print lon_scale
            time.sleep(0.5)
        #else:
#else is only for debugging
            #print "not working"  


gpsloatlong()
