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
import board
import neopixel
pixel_pin = board.D21

# The number of NeoPixels
num_pixels = 8

# The order of the pixel colors - RGB or GRB. Some NeoPixels have red and green reversed!
# For RGBW NeoPixels, simply change the ORDER to RGBW or GRBW.
ORDER = neopixel.GRB

pixels = neopixel.NeoPixel(
    pixel_pin, num_pixels, brightness=0.2, auto_write=False, pixel_order=ORDER
)

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
        #print(ts)
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
    #lat = 51.5074
    #lng = -0.1278
    totsec = 86400
    ctr = request(lat,lng)
    print("initialized")
    #print(ctr)
    #print(ctr/86400)
    print(len(pixels))
    cols = [(255,0,0),(25,23,225)]
    while(True):
        if (ctr>totsec):
            ctr = 0
        #if daytime disp red,
        #nighttime display blue
        sel = cols[0]  if (ctr<(totsec/2)) else cols[1]

        for i in range(int(num_pixels*(ctr/totsec))):
            pixels[i] = sel
        pixels.show()
        ctr+=1
        #pixels.show()
        time.sleep(1)


if __name__ == "__main__":
    main()