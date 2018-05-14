#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
cd java
javac ru/ifmo/rain/Petrovski/studentdb/StudentDB.java 
cd info/kgeorgiy/java/advanced/student/
rm *.class
cd - 
java -cp ./:../artifacts/StudentTest.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar info.kgeorgiy.java.advanced.student.Tester StudentGroupQuery ru.ifmo.rain.Petrovski.studentdb.StudentDB "$1"
cd ..

