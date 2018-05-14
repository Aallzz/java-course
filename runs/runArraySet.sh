#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
cd java
javac ru/ifmo/rain/Petrovski/arrayset/ArraySet.java
cd info/kgeorgiy/java/advanced/arrayset/
rm -f *.class
cd - 
java -cp ./:../artifacts/ArraySetTest.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar info.kgeorgiy.java.advanced.arrayset.Tester NavigableSet ru.ifmo.rain.Petrovski.arrayset.ArraySet $1
cd ..

