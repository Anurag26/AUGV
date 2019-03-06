from firebase import firebase 
import time
from datetime import datetime 
import schedule 
i = 5 
UUID=[]
TotDistance=""
IndividualStepDistance=""
Instructions=""
ManeuverBearings=""
ManeuverLongLat=""
TotalSteps=""


app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
# =============================================================================
# def job():
#     print("Job is running")
#     result = app.get('/json','uuid')
#     #print(result,str(datetime.now()))
#     return result
# 
# =============================================================================

# =============================================================================
# def f():
#     print("F is running")
#     schedule.every(1).seconds.do(job)
#     print("Schedule while loop starting")
#     while 1:
#         schedule.run_pending()
#         time.sleep(0)
#         p=job()
#         compareUUID(p)
#         #l=[]
#         #l.append(p)
#         #print("P in f is",p)
#         
# =============================================================================
# =============================================================================
# def parseNewRouteData():
#     print("new Route needs to parse")
#     global k
#     k="hello"
#     app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
#     Routes = app.get('/json','routes')
#     Legs = Routes['legs']
#     global TotDistance
#     TotDistance= Legs['distance']
#     global IndividualStepDistance
#     IndividualStepDistance= Legs['IndividualStepDistance']
#     global Instructions
#     Instructions= Legs['Instructions']
#     global ManeuverBearings
#     ManeuverBearings= Legs['ManeuverBearings']
#     global ManeuverLongLat
#     ManeuverLongLat= Legs['ManeuverLongLat']
#     global TotalSteps
#     TotalSteps= Legs['TotalSteps']
#     test()
#     #app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
# =============================================================================
    
    

# =============================================================================
# def compareUUID(x):
#     print("Compare UUID is Running")
#     #print("UUID in Compare is",x)
#     UUID.insert(0,x)
#     print("UUID")
#     if len(UUID)>2:
#         del UUID[2:]
#         if(UUID[0]==UUID[1]):
#             print("No change in route")
#         else:
#             print("Route Changed")
#             parseNewRouteData()
# =============================================================================
    #for x in range(len(UUID)): 
        #print("UUID array is",UUID[x])  
def test():
    print('Global Variable',TotDistance)
    
def ParseRouteForFirstTime():
    print("Total distance")
    Routes = app.get('/json','routes')
    Legs = Routes['legs']
    global TotDistance
    TotDistance= Legs['distance']
    global IndividualStepDistance
    IndividualStepDistance= Legs['IndividualStepDistance']
    global Instructions
    Instructions= Legs['Instructions']
    global ManeuverBearings
    ManeuverBearings= Legs['ManeuverBearings']
    global ManeuverLongLat
    ManeuverLongLat= Legs['ManeuverLongLat']
    global TotalSteps
    TotalSteps= Legs['TotalSteps']
    test()
    
    #Routes = app.get('/json','routes')
    
    
    #a=getDistance()
    #print(a)
            
        
def main():
#    print("This is main function")
#    print("Parse the route first time")
   # app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
   #assignApp(app)
    #parseRouteData()
    ParseRouteForFirstTime()
#    f()
    
if __name__ == "__main__":
   main()
     

#print(k)
