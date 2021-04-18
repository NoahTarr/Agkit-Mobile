# AgKit-Mobile

ROS-Mobile is an [Android](https://www.android.com/) application designed for dynamic control and visualization of mobile robotic systems operated by the Robot Operating System ([ROS](http://wiki.ros.org/)). The application uses ROS nodes initializing publisher and subscriber with standard ROS messages. The overall code architecture pattern is Model View ViewModel ([MVVM]([https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel](https://en.wikipedia.org/wiki/Model–view–viewmodel))), which stabilizes the application and makes it highly customizable. For a detailed overview of the functionality, please refer to our [wiki](https://github.com/NoahTarr/Agkit-Mobile/wiki) or the original [ROS-Mobile's wiki](https://github.com/ROS-Mobile/ROS-Mobile-Android/wiki).

Agkit-Mobile is built on top of ROS-Mobile for application specific to the AgKit project. For more information on AgKit specifically, check out the [AgKit Repo](https://github.com/plant-ai-biophysics-lab/AgKit).

- Current stable Version: 1.2.0

## Requirements

- Mobile Android Device with Android Version 5.0 (Lollipop) or higher
- Android Studio Version 3.6.1 or higher (if built from source)

## Installing Instructions

There are two ways to install AgKit-Mobile: build from source and install a pre-made APK.

##### Build from Source (recommended)
- Install Android Studio Version 3.6.1 or higher
- Download the complete repository (Master Branch) and open it via Android Studio
- If needed, follow instructions to add .gemf map file for GPS [here](https://github.com/NoahTarr/Agkit-Mobile/wiki/GPS-Maps#installing-and-generating-offline-gps-maps)
- Built the Code (Make Project Button), connect your mobile device to your PC and install the Software (Run 'app'). Make sure you activated the developer options and adb debugging on your mobile device and installed all required drivers for usb and adb.

##### Install pre-made APK from AgKit-Mobile:

- Download the AgKit-Mobile version as [apk file](https://github.com/NoahTarr/Agkit-Mobile/blob/master/app/release/app-release.apk) and store it in an easy-to-find location onto your mobile device
- Allow third-party apps on your device. Therefore go to **Menu > Settings > Security** and check **Unknown Sources** 
- Go now to the apk file, tap it, then hit install

## ROS-Mobile Introduction Video.  
[![Introduction Video](http://img.youtube.com/vi/T0HrEcO-0x0/0.jpg)](http://www.youtube.com/watch?v=T0HrEcO-0x0)
*This was created by the original ROS-Mobile developer, not us.*  

## Currently available ROS Nodes

- Camera (sensor_msgs/Image)
- Gps (sensor_msgs/NavSatFix)
- Button (sensor_msgs/Bool)
- ToggleButton (sensor_msgs/Bool)
- CameraAngleAdjustor (sensor_msgs/Float32)
- Debug (similar to rostopic echo)
- Logger (std_msgs/String)

## Short Example Usage

<p float="left" align="middle">
  <img src="/images/ShortExample01.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample02.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample03.jpg" width="200 hspace="50" />
  <img src="/images/ShortExample04.jpg" width="200 hspace="50" />
</p>


Manually map an apartment environment using a differential drive robot. Therefore, we connected the application with the ROS master running the differential drive robot over wireless LAN by inserting the correct IP address in the *MASTER* configuration tab, first figure. Adding ROS nodes in the *DETAILS* tab, second figure and third figure, enables the control of the differential drive robot via a joystick method sending *geometry\_msgs/Twist* to a *cmd\_vel* topic and the visualization of the generated occupancy grid map by subscribing to the *map* topic via a gridmap method. In the *VIZ* tab, most right figure, the recorded occupancy grid map is displayed as well as the joystick. The joystick can be used via touch for sending control inputs over the *cmd\_vel* topic to the differential drive robot. For a more detailed examples, we refer to our [wiki](https://github.com/ROS-Mobile/ROS-Mobile-Android/wiki/Example-Applications).

## Contributors

#### Agkit-Mobile: 
Developers: [Noah Tarr](https://github.com/NoahTarr), [Neil Katahira](https://github.com/neilkatahira), [Varsha Senthil](https://github.com/varshaaaaa)

#### ROS-Mobile:
Developers:
[Nico Studt](https://torellin.github.io/), [Nils Rottmann](https://nrottmann.github.io/)

Contributors:
[Marcus Davi](https://github.com/Marcus-Davi), [Dragos Circa](https://github.com/Cycov)
