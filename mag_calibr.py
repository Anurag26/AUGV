#Magnetometer values required: x-axis and y-axix
#x-axis value: xMag
#y-axis value: yMag


import smbus
import time
import math

bus = smbus.SMBus(1)

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

data0 = bus.read_byte_data(0x1D, 0x0A)
data1 = bus.read_byte_data(0x1D, 0x0B)
yMag = data1 * 256 + data0
if yMag > 32767 :
    yMag -= 65536



maximum_x=xMag
minimum_x=xMag
maximum_y=yMag
minimum_y=yMag
maxandmin=[]
maxmin = []
t_end = time.time() + 20   #time.time() will return the current time, HENCE 30 SECONDS IS ADDED TO IT TO MAKE PGM RUN FOR 30 SECONDS
def calibrate_the_compass():
    global maximum_x, minimum_x, maximum_y, minimum_y
    while time.time()<t_end:
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

        data0 = bus.read_byte_data(0x1D, 0x0A)
        data1 = bus.read_byte_data(0x1D, 0x0B)
        yMag = data1 * 256 + data0
        if yMag > 32767 :
            yMag -= 65536

        if xMag < minimum_x:
            minimum_x = xMag

        if xMag > maximum_x:
            maximum_x = xMag

        if yMag < minimum_y:
            minimum_y = yMag

        if yMag > maximum_y:
            maximum_y = yMag

        print(maximum_x, "\t", minimum_x, "\t", maximum_y, "\t", minimum_y)
        global maxandmin
    maxandmin.append(maximum_x)
    maxandmin.append(minimum_x)
    maxandmin.append(maximum_y)
    maxandmin.append(minimum_y)
    return maxandmin
t=calibrate_the_compass()
print("Compass calibrated")
print("X Maximum:", t[0])
print("X Minimum:", t[1])
print("Y Maximum:", t[2])
print("Y Minimum:", t[3])
