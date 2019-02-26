import json


legs = ""
json_content ="" 
#class RouteParsing:
        
        #Gives the individual step distance
def getIndiStepDist():  
    stepdist= legs['IndividualStepDistance']
    return stepdist
        
         #Gives the instructions
def getInstruct():  
    Inst=legs['Instructions']
    return(Inst)    
                
    #Gives the Maneuver Bearings
def getBearings():  
    Bear=legs['ManeuverBearings']
    return(Bear)
        
        #Gives the Maneuver Latitude and Longitude
def getLatLong():  
    LatLong=legs['ManeuverLongLat']
    return LatLong
                
        #Gives the Total Steps
def getTotSteps(): 
    TotSteps=legs['TotalSteps']
    return(TotSteps)
                   
            #Gives the Total Distance
def getDistance(self):  
    Dist=legs['distance']
    return Dist
                
            #Gives the uuid
def getuuid():  
    uuid=json_content['uuid']
    return uuid
    
                
       
with open('./augv-c7210-export.json') as access_json:
    read_content = json.load(access_json)
    json_content = read_content['json']
    routes=json_content['routes']
    legs=routes['legs']
        
        