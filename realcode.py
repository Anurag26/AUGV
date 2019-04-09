from firebase import firebase
import gpss 
import magnetometer 
import math 
#import threading 
print("1")
ManeuverLongLat = 0 
TotDistance=""
IndividualStepDistance=""
Instructions=""
ManeuverBearings=""
ManeuverLongLat=""
TotalSteps=""
#flag = ""
global counter 
counter = 0 
global distance 
distance = 0
global angle 
angle = 0  
global flag
flag = 1 
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
#result = app.get('/json','routes')
print("2")

Routes = app.get('/json','routes')
print("3")
#print(Routes)
Legs = Routes['legs']
ManeuverLongLat = Legs['ManeuverLongLat']
ManeuverBearings = Legs['ManeuverBearings']
NumberOfSteps = Legs["TotalSteps"]
print(ManeuverLongLat)
print(ManeuverBearings)
print(NumberOfSteps)


#while flag:
 #   navigation(counter)

def take_a_turn(x):
    print("Called tt 8")
    #method for turning the bot
    print("delta: ",x)


def path_verification(path_count):
    print("In path_verification 6")
    #step_counter = counter
    distance_threshold = 1
    #counter = 0 
    #delta = magnetometer.bearings(ManeuverBearings[step_counter])
    print("Calling take tuen 7")
    take_a_turn(angle)
    print("returned to path ver 9")
    #start the rover motion
    if distance < distance_threshold:
        count_1 = path_count+1
        #stop the motors
#    if count_1 >= NumberOfSteps:
#        flag = 0
    print(flag)
    print("Path vr ended 10")

def navigation(count):
    print("CAlled navigation 4")
#    step_counter = cou
    latlon = gpss.gpsloatlong()
    #get the current position
    current_lat = latlon[0]
    print("Current lat = ", current_lat)
    current_lon = latlon[1]
    print("Current lon = ", current_lon)
    #get the target position
    target_lat = ManeuverLongLat[count * 2]
    target_lon = ManeuverLongLat[count * 2 + 1]
    print("Target latitude", target_lat)
    print("Target longitude", target_lon)
    #get the distance to the destination using distance formula
    distance = (math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)) * 111000
    if distance > 10 and count == 0:
        print("origin error")
    print("Distance: ",distance)
    angle = magnetometer.bearings(ManeuverBearings[count])
   # threading.Timer(0.5, path_verification()).start()
    print("Calling path verification 5")
    path_verification(count)
    print("returned to nav10")
    if count == NumberOfSteps:
        print("End of navigation")

def getFlag() :
    while counter != NumberOfSteps:
        navigation(counter)
#while flag:
 #   print("CAlling navigation 3.1")
  #  navigation(counter)
getFlag()
