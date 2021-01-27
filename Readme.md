# README:

Module 1 Project for Creative Embedded Systems.



## <u>About</u>

This project is meant to be an interactive generative kiosk art run on either the Raspberry Pi B+ or 4 which also functions as a world clock. The user can search open-endedly for any place in the world they want to find the time at. The wallpaper itself (all of it which is procedurally generated) as well as an external led pseudo "clock" respectively changes animation and time according to the place where the user wants to search.

This project involves running 2 programs on a raspberry pi on boot: 

1. The generation of a dynamic wallpaper on an external monitor using Processing, which the and displays the time based on a user search.  The wallpaper procedurally generates realistic trees in a watercolor style as well as mountains. The leaves continue to change color every minute and when a new location is queried by the user. The wallpaper's sky background is dependent on the area of interest the user searched for.
2.  Python program hosting two processes: a local Flask server and a clock controlling a Freenove 8 RGB LED Module.  The Flask server receives request from the Processing user query, webscrapes google for the place the user wants to find, then accesses the timezonedb database for information such as the time there, country/municipality, and time zone

Demo of the Art/server [here](https://youtu.be/PQb85eTswbk)

Demo of the entire system, pi+external clock



## <u>Installation/Setup</u>

1. This program is meant to run on a raspberry Pi 3b+ or 4, along with a FreeNove 8RGB led module 

### Program Installations

1. Clone this github repo

   ```
   git clone https://github.com/bnhwa/GenerativeArt.git
   ```

   

2. Processing on Raspberry PI: To install Processing, go to the Raspberry Pi shell and do:

   ```
   curl https://processing.org/download/install-arm.sh | sudo sh
   ```

   On Processing, go to import library and download controlP5

3. Follow the instructions on to install the relevant libraries [Neopixel libraries](https://learn.adafruit.com/neopixels-on-raspberry-pi/overview )

4. the google, BeautifulSoup,  multiprocess, and Flask libraries need to be installed. On the raspberry pi (versions 3b+ and 4), you can do this with pip3 (if you have pip for older versions of the pi use it instead)

   ```
   pip install beautifulsoup4
   pip install google
   pip install multiprocess
   pip install Flask
   ```

   

### Hardware Setup



## <u>Running</u>

modify your `\home\pi\.bashrc` file.

First call `sudo nano \home\pi\.bashrc`

```
sudo processing-java sketch=<fullpath>/generativeWallpaper --run &
sudo python3 <fullpath>/apiClock.py &
```

boot the pi and both programs should run. 

