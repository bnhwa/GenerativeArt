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

5. Go to `apiClock.py` (and if you want to run this on standalone computer go to `apiClockNonPi.py`if you want to run this on regular desktop) and get an api key from [timezonedb](https://timezonedb.com/api). In the relevant file, change the `API_key` variable to the key you have.

   ### For the Pi Specifically

   1. Open Processing and export `generativeWallpaper.pde`to executable; you do this by going to the processing IDE to File->Export Application. When prompted, you want to choose Linux for the pi and only need the `application.linux-armv6hf` folder that is produced

   2. Add to the end of your bashrc (i.e., edit the file via `sudo nano /home/pi/.bashrc`) the following, as well as the execution for the pi. Make sure to use the full absolute paths!

      ```
      sudo /home/pi/<your directory to the sketch folder>/application.linux-armv6hf /<the executable>
      
      sudo python3 /home/pi/<your directory to the python file>/apiClock.py
      ```

### Hardware Setup (only for the pi)

1. Follow [here](https://learn.adafruit.com/neopixels-on-raspberry-pi/raspberry-pi-wiring) , specifically the section, 'Raspberry Pi Wiring with Diode'. However, what I did was use GPIO 21 instead of 18 (the program is also configured for 21) due to weird behavior of GPIO 18. I also used GPIO 2 (the 5v GPIO) on the Pi in lieu of the 'external 5v power source' mentioned and the ground GPIO 6 for the respective ground.

## <u>Running</u>

### On the Pi:

If you followed all the instructions, upon rebooting the pi, both programs should run (it is slow to start up though).

### On a Regular Computer:

Go to the directory of `apiClockNonPi.py` and run `python apiClockNonPi.py` first. Then, run the Processing Sketch. Voila.

