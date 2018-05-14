#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
echo 
cd java/
javac info/kgeorgiy/java/advanced/implementor/Impler*.java info/kgeorgiy/java/advanced/implementor/JarImpler.java
javac -cp ./../artifacts/JarImplementorTest.jar:./../lib/hamcrest-core-1.3.jar:./../lib/junit-4.11.jar:./../lib/jsoup-1.8.1.jar:./../lib/quickcheck-0.6.jar: -Xlint ru/ifmo/rain/Petrovski/implementor/Implementor.java
jar cfm ../Implementor.jar Manifest.txt ru/ifmo/rain/Petrovski/implementor/*.class info/kgeorgiy/java/advanced/implementor/*.class
rm info/kgeorgiy/java/advanced/implementor/*.class
cd ..
chmod 777 Implementor.jar
