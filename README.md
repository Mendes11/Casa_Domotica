# Casa_Domotica
Automated House Project

Project for the University Challenge of Domótics 2016

The project consists of a house prototype controlled by an Android Tablet.

## The controllable components are:
    * Windows (Opening)
    * Sunroof (Opening)
    * Eletrical Sockets (Activation using Relays)
    * Ilumination (Intensity of LEDs) using a PID controller
    * Access Control using RFID (Activation of the door using a Relay)
    * Automatic closing the windows when detecting rain using a water detector.
    
## Directories

* Android - APP Android
* Arduino - Program to control all peripherals and receive commands from I2C bus.
* MySQL - SQL Code.
* Peças Impressora 3D - Models for the prototypes.
* Placa-Circuito - Circuit Board for the relays.
* Servidor PHP - Apache2 - PHP Server to serve as API for the Android APP.
* Servidor Python - Python Server responsible for the decision making when modifications in the SQL server are made. It actuates using I2C commands sent to the Arduino.
