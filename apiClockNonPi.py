# -*- coding: utf-8 -*-
"""
Created on Fri Jan 22 11:17:33 2021

@author: bb339
Raspberry pi 

api key:   C7BV3SHDW5LP
"""
import requests
import time

import datetime
from functools import reduce
# install BeautifulSoup4 and google
from bs4 import BeautifulSoup
import googlesearch#install flask
from flask import Flask
from flask import Response, request, jsonify,send_from_directory



app = Flask(__name__)
#clock globals
totsec = 86400
ctr = 0
@app.route("/search/<query>")
def coord_query(query):
    """
    Web API server query
    """
    if query is None: return jsonify(success = False)
    query = query.replace("&&"," ")
    
    lat,long= google_coords(query)
    if (lat is not None and long is not None):
        res = coords_request(lat,long)
        if (res is not None):
            return jsonify(success=True,
                           curr_time=res["curr_time"],
                country=res["country"],
                city=res["city"],
                prog_secs=datetime.datetime.now().second)
        else:
            return jsonify(success=False)
    
    ret_test = 1
    return jsonify(data=ret_test,d2="4")
    
    
def current_milli_time():
    return round(time.time() * 1000)

def coords_request(lat,lng):
    global ctr, totsec
    #params
    API_key = "C7BV3SHDW5LP"
    _format = "json"
    _url = "http://api.timezonedb.com/v2.1/get-time-zone" 
    res = requests.get(_url,params=dict(key=API_key,by="position",format=_format,lat=lat,lng=lng)).json()
    if (res['status']=="OK"):
        start = time.time()
        print(res)
        ts = res["formatted"].split(' ')[-1].split(":")
        #print(ts)
        secs = (3600*int(ts[0]))+(60*int(ts[1]))+int(ts[2])
        
        end = time.time()
        ctr = secs+(end-start)
        return {"curr_time":ctr,
                "country":format_none(res["countryName"]),
                "city":format_none(res["zoneName"])            
            }
    else:
        return None
        # return secs+(end-start)
def format_none(val):
    return val if val is not None else ""
    
def google_coords(query):
    """
    Web Scrape google for the feedback form for coordinates,
    
    """
    h = {"User-Agent":"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36"}
    r = requests.get("https://www.google.ie/search?q={}+coordinates"\
                   .format(' '.join(query.split()).replace(" ","+")), headers=h).text
    soup = BeautifulSoup(r,"lxml")
    search_res = soup.find("div", {"class": "Z0LcW XcVN5d"})#coordinates tag
    if search_res is None:
        pass
    else:
        rep={"° N":"*1",
             "° S":"*-1",
             "° E":"*1",
             "° W":"*-1"
             }
        res = search_res.text
        for k,v in rep.items():
            res = res.replace(k,v)
        #latitude north ° N
        # print(res)
        u_lat, u_long = list(map(lambda x: reduce((lambda y, z: y * z), map(lambda k: float(k), x.split("*")))   ,\
                                 res.split(",")))
        #print(u_lat, u_long)
        return (u_lat,u_long)
    
    

if __name__ == "__main__":
    # app.debug = True
    #print(0)
    app.run(host="127.0.0.1",port=8025)
