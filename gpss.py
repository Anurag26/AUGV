import time
import multiprocessin
import serial
import string
import pynmea2
import RPi.GPIO as gpio
 
def gpslatlong():

    while 1:

        gpio.setmode(gpio.BCM)

        port = "/dev/ttyAMA0"

        ser = serial.Serial(port, baudrate= 9600, timeout = 0.5)  

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


def multi_process():
    
    # Start foo as a process
    p = multiprocessing.Process(gpslatlong())
    #print(p)
    print("h0")
    p.start()
    print("h1")

    # Wait 10 seconds for foo
    time.sleep(10)
    print("h2")

    # Terminate foo
    p.terminate()
    print("h3")

    # Cleanup
multi_process(15)

