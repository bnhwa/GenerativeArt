# README:

Module 1 Project for Creative Embedded Systems.



## <u>About</u>

This program is a generative wallpaper in which a user can search a place they want to find the time at. The wallpaper as well as a led pseudo "clock" is in sync with the place where the user wants to search.

This project involves running 2 programs on a raspberry pi on boot: 

1. The generation of a dynamic wallpaper on an external monitor using Processing, which the and displays the time based on a user search. The wallpaper procedurally generates realistic trees in a watercolor style as well as mountains. The leaves continue to change color every few seconds. The wallpaper's background is dependent on the area of interest the user searched for.
2.  Python program hosting a local server and connecting to the webAPI of timezonedb, displaying the searched location  akin to a watch on a Freenove 8 RGB LED Module.   



## <u>Installation/Setup</u>

1. This program utilizes a raspberry Pi 3b+, along with a FreeNove 8RGB led module 

### Program Installations

1. Clone this github repo

   ```
   git clone https://github.com/bnhwa/GenerativeArt.git
   ```

   

2. Processing on Raspberry PI: To install Processing, go to the Raspberry Pi shell and do:

   ```
   curl https://processing.org/download/install-arm.sh | sudo sh
   ```

3. Follow the instructions on to install the relevant libraries [Neopixel libraries](https://learn.adafruit.com/neopixels-on-raspberry-pi/overview )

4. the google, BeautifulSoup,  multiprocess, and Flask libraries need to be installed. You can do this with pip (if you have pip3 use it instead)

   ```
   pip install beautifulsoup4
   pip install google
   pip install multiprocess
   pip install Flask
   ```

   

### Hardware Setup



## <u>Running</u>

modify your `/etc/rc.local` such that you put in the commands to run both programs

```
sudo processing-java sketch=<fullpath>/generativeWallpaper --run &
sudo python3 <fullpath>/apiClock.py &
```

boot the pi and both programs should run. 

