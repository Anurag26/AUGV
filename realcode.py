from firebase import firebase
import gpss 
import magnetometer 
import math 
#import threading 
print("import done ")
ManeuverLongLat = 0 
TotDistance=""
IndividualStepDistance=""
Instructions=""
global ManeuverBearings=""
global ManeuverLongLat=""
TotalSteps=""
#flag = ""
global counter 
counter = 0 
global distance 
distance = 0
#global angle 
#angle = 0  
global flag
flag = 1 
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
#result = app.get('/json','routes')
print(" data from firbase")

Routes = app.get('/json','routes')
print("getting routes")
#print(Routes)
Legs = Routes['legs']
ManeuverLongLat = Legs['ManeuverLongLat']
float(ManeuverBearings) = Legs['ManeuverBearings']
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
    print("In path_verification ")
    #step_counter = counter
    
    #counter = 0 
    #delta = magnetometer.bearings(ManeuverBearings[step_counter])
    print("Calling take turn ")
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
    print("called navigation :4")
#    step_counter = cou
    distance_threshold = 35
    print(" distance threshold set to " , distance_threshold)
    latlon = gpss.gpsloatlong()
    #get the current position
    current_lat = latlon[0]
    print("Current lat = ", current_lat)
    current_lon = latlon[1]
    print("Current lon = ", current_lon)
    #get the target position
    target_lat = ManeuverLongLat[count * 2]
    target_lon = ManeuverLongLat[count * 2 + 1]
    print("Target latitude from firebase", target_lat)
    print("Target longitude from firebase", target_lon)
    #get the distance to the destination using distance formula
    distance = (math.sqrt((target_lat - current_lat)**2 + (target_lon - current_lon)**2)) * 111000
    if distance > distance_threshold and count == 0:
        print("origin error")

        
    print("caculated Distance: ",distance)

    global angle 

    angle = magnetometer.bearings(ManeuverBearings[count])
   #threading.Timer(0.5, path_verification()).start()
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
