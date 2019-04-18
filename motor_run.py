# -*- coding: utf-8 -*-
"""
Created on Thu Apr 18 16:17:27 2019

@author: admin """


import RPi.GPIO as gpio 
import schedule 
def run_motor():
    gpio.setup(13,gpio.OUT)
    gpio.output(13, True)
    
    
schedule.every(5).seconds.do(run_motor)
