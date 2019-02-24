
# coding: utf-8

# In[ ]:


from firebase import firebase 
import time
from datetime import datetime 
import schedule 
i = 5 
app = firebase.FirebaseApplication('https://augv-c7210.firebaseio.com/',None)
def job():
    result = app.get('/json','uuid')
    print (result,str(datetime.now()))
    return result
def f():
    
    schedule.every(3).seconds.do(job)
    while i > :
        schedule.run_pending()
        time.sleep(1)
        job()
                
            

    

