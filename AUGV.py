
# coding: utf-8

# In[ ]:


from firebase import firebase 
import time
from datetime import datetime 
import schedule 
i = 5 
UUID=[]
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
def job():
    result = app.get('/json','uuid')
    #print(result,str(datetime.now()))
    return result

def f():
    
    schedule.every(1).seconds.do(job)
    while 1:
        schedule.run_pending()
        time.sleep(0)
        p=job()
        compareUUID(p)
        #l=[]
        #l.append(p)
        #print("P in f is",p)

def compareUUID(x):
    
    #print("UUID in Compare is",x)
    UUID.insert(0,x)
    if len(UUID)>2:
        del UUID[2:]
        if(UUID[0]==UUID[1]):
            print("No change in route")
        else:
            print("Route Changed")
            parseNewRouteData()
    #for x in range(len(UUID)): 
        #print("UUID array is",UUID[x])  

def parseNewRouteData():
    print("Parsing New Route")
        
f()
  

