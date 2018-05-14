#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
cd java
javac ru/ifmo/rain/Petrovski/walk/RecursiveWalk.java
cd info/kgeorgiy/java/advanced/walk/
rm -f *.class
cd - 
java -cp ./:../artifacts/WalkTest.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar info.kgeorgiy.java.advanced.walk.Tester RecursiveWalk ru.ifmo.rain.Petrovski.walk.RecursiveWalk $1
rm -rf __Test__Walk__/ 
cd ..

