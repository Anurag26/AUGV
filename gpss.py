import serial
import time
import string
import pynmea2
import RPi.GPIO as gpio
global check
#check = 1


def gpsloatlong():
    try:
        check = 1
        global data
        l=[]
        while check:
            gpio.setmode(gpio.BCM)

           # port = '/dev/ttyAMA0'
            ser=serial.Serial('/dev/ttyAMA0', baudrate= 9600, timeout = 0.2)


            try:
                #global data
                data = ser.readline()
                data = data.decode("utf-8","ignore")
            except:
                print("loading")
                gpsloatlong()

            if data[0:6]=='$GPRMC':
                check = 0
                msg = pynmea2.parse(data)
                #print("Decoded pynmea2 code is",msg)
                latval = msg.lat
                longval = msg.lon
                if len(latval) == 0 and len(longval) == 0:
                    latval = "0"
                    longval = "0"
                lon_float = float(longval)
                lat_flot = float(latval)
                #lat1 =  lat_int * 100
                lat2 = lat_flot /100
                #lat_seconds = lat1 % 100
                lat_minutes = lat_flot % 100 # + lat_seconds * 60
                do_lat = int(lat2 % 100)
                #latitude offset observed is '-0.010773'
                dd_lat = do_lat + lat_minutes / 60
                print("latitude is ",dd_lat)
                #lon1 = lon_float * 100
                lon2 = lon_float / 100
                #lon_seconds = lon1 % 100
                lon_minutes = lon_float % 100
                do_lon  = int(lon2 % 100)
                #longitude offset observed it '-0.007217'
                dd_lon = do_lon+ lon_minutes /60  #- 0.017937
                print("longi is ",dd_lon)
                #time.sleep(0.5)
                l.append(dd_lat)
                l.append(dd_lon)
                print(l)
                return l
            #else:
    #else is only for debugging
                #print "not working"
    except:
        print("Exception occured in getloatlong")

#t = gpsloatlong()
gpsloatlong()
#print(t)
