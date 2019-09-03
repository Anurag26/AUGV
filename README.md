# Autonomous Unmanned Ground Vehicle
**The AUGV repository contains codebase for the Android application and the scripts needed to run and control the UGV. This was the senior year project during the Undergraduate Engineering course showcasing the technical and soft skills gained over four years.**


## Android Application
The android application served the purpose of controlling the UGV remotely. The Application had an intuitive UI allowing any user to request an UGV and send it to the intended destination.

![Imgur](https://i.imgur.com/1yErWTZ.jpg)       ![Imgur](https://i.imgur.com/XVnxAFw.jpg)  ![Imgur](https://i.imgur.com/SCnjjyP.png)

1. The first screenshot shows the users location on the map using a blue marker
2. The second screenshot shows a user searching for the location intended as the destination. Here, the user has the choice of either droppin a location pin or using the  google search. 
3. The third screenshot shows the user the route selected by the rover to travel.

**The tools used in building this application were:**
- [Android SDK](https://developer.android.com/studio)
- [GCP](https://cloud.google.com)
     1. [Places API](https://console.cloud.google.com/apis/library/places-backend.googleapis.com)
     2. [Maps SDK](https://console.cloud.google.com/apis/library/maps-android-backend.googleapis.com)
     3. [Directions API](https://console.cloud.google.com/google/maps-apis/apis/directions-backend.googleapis.com)
- [Firebase](https://firebase.google.com/)
    [Realtime Database](https://firebase.google.com/docs/database)
    

        

## The UGV 
![Imgur](https://i.imgur.com/KMNgR7X.jpg)

The UGV used the Raspberrypi 3b+ as the main processing unit. The peripherals attached to the pi included the magentometer, gps module, servo motor, DC motors, 9V Li-Po battery and a 1000mah power bank.

* The programming scripts were written in python and stored locally on the pi. 
* As the UGV was to be controlled remotely a web server was set up on pi and a detailed explanation is provided here https://medium.com/@bannuranurag/controlling-raspberry-pi-using-an-android-application-c7334625e513


## Contact
If interested in knowing more about this project or you would like to read the paper written on the same please contact one of the contributors.


## Licensing
```
Copyright 2019 Anurag Sanjay Bannur

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
