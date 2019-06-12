#Magnetometer values required: x-axis and y-axix
#x-axis value: xMag
#y-axis value: yMag


import smbus
import time
import math
import mag_calibr
#The defined function returns a signed integer which tells the direction
#to turn to. If the returned value is negative, then turn that many degrees
#to the left. If the returned value is positive, then turn that many degrees
#to the right. If, however, the returned value is zero, then turning in any
#direction is not required
maxmx = 0
minmx = 0
maxmy = 0
minmy = 0

def bearings(): #bearing_target):
    try:
        bus = smbus.SMBus(1)
        bearings = 0
        delta = 0
        change = 0
        bus.write_byte_data(0x1D, 0x20, 0x0F)
        bus.write_byte_data(0x1D, 0x23, 0x30)

        time.sleep(0.2)
        bus.write_byte_data(0x1D, 0x20, 0x67)
        bus.write_byte_data(0x1D, 0x21, 0x20)
        bus.write_byte_data(0x1D, 0x24, 0x70)
        bus.write_byte_data(0x1D, 0x25, 0x60)
        bus.write_byte_data(0x1D, 0x26, 0x00)

        time.sleep(0.5)
        data0 = bus.read_byte_data(0x1D, 0x08)
        data1 = bus.read_byte_data(0x1D, 0x09)
        xMag = data1 * 256 + data0
        if xMag > 32767 :
            xMag -= 65536
        xDiff = (abs(maxmx) + abs(minmx) if maxmx * minmx < 0 else abs(maxmx - minmx))/2
#        print("X Difference: ", xDiff)
        xMag = ((xMag + abs(minmx)) - xDiff) / xDiff

        data0 = bus.read_byte_data(0x1D, 0x0A)
        data1 = bus.read_byte_data(0x1D, 0x0B)
        yMag = data1 * 256 + data0
        if yMag > 32767 :
            yMag -= 65536
        yDiff = (abs(maxmy) + abs(minmy) if maxmy * minmy < 0 else abs(maxmy - minmy))/2
#        print("Y Difference: ", yDiff)
        yMag = ((yMag + abs(minmy)) - yDiff) / yDiff

    #    bearing = ((maxm - minm) - xMag) / ((maxm - minm) / 180)
    #    if yMag < 0:
    #        bearing = 360 - bearing
    #    print(bearing)
        bearings = int(math.atan2(xMag, yMag) * (-180)/3.141592654)
    #    bearings = math.atan2(xMag, yMag) * 180/3.141592654
    #    if bearings < 0:
    #        bearings = bearings + 360
        print("Current Heading:", bearings)
    #    print(xMag, "\t", yMag);
    #    return bearings
    except:
        print("Exception occured in magnetometer")
def update(maxandmin):
    global maxmx
    maxmx = maxandmin[0]
    global minmx
    minmx = maxandmin[1]
    global maxmy
    maxmy = maxandmin[2]
    global minmy
    minmy = maxandmin[3]
    print("Magnetometer updated ", maxmx,"\t", minmx, "\t", maxmy, "\t", minmy)

m = mag_calibr.calibrate_the_compass()
update(m)
while 1:
    bearings()
