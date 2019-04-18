# -*- coding: utf-8 -*-
"""
Created on Thu Apr 18 12:37:43 2019

@author: Anurag Bannur
"""

import magnetometer
import threading
import schedule
import time

def checkBearingError():
    

def initiateTurning(Bearings,Flag,Steps):
    while(Flag!=Steps):
        RequiredBearing=Bearings[Flag]
        schedule.every(1).second.do(checkBearingError)
    
    
    