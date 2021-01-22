# -*- coding: utf-8 -*-
"""
Created on Fri Jan 22 11:17:33 2021

@author: bb339
Raspberry pi 

api key:   C7BV3SHDW5LP
"""
import requests
import time
import importlib
# import board
# import neopixel
###see if libraries are imported, if not, install them

def current_milli_time():
    return round(time.time() * 1000)

def request(lat,lng):
    #params
    API_key = "C7BV3SHDW5LP"
    _format = "json"
    _url = "http://api.timezonedb.com/v2.1/get-time-zone" 
    res = requests.get(_url,params=dict(key=API_key,by="position",format=_format,lat=lat,lng=lng)).json()
    if (res['status']=="OK"):
        start = time.time()
        ts = res["formatted"].split(' ')[-1].split(":")
        print(ts)
        secs = (3600*int(ts[0]))+(60*int(ts[1]))+int(ts[2])
        
        end = time.time()
        return secs+(end-start)
        
    else:
        print("check your internet connection")
    # print(res)
    # print(res.json())

def main():
    lat = 35.6762
    lng = 139.6503
    # lat = 51.5074
    # lng = -0.1278
    totsec = 86400
    ctr = request(lat,lng)
    print(ctr)
    print(ctr/86400)
    
    
    #tot seconds = 86400
    # while(True):
    #     if (ctr>86400):
    #         ctr = 0
    #     pass
    


if __name__ == "__main__":
    main()