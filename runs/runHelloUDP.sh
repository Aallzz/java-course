#!/bin/bash

cd java
javac ru/ifmo/rain/Petrovski/helloudp/HelloUDPServer.java 
javac ru/ifmo/rain/Petrovski/helloudp/HelloUDPClient.java
java ru.ifmo.rain.Petrovski.helloudp.HelloUDPServer 1437 4 &
sleep 1 
java ru.ifmo.rain.Petrovski.helloudp.HelloUDPClient localhost 1437 "вы проиграли" 3 4
cd ..
