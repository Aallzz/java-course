#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
mydir=`pwd`
cd java
javac ru/ifmo/rain/Petrovski/concurrent/IterativeParallelism.java ru/ifmo/rain/Petrovski/mapper/ParallelMapperImpl.java
cd info/kgeorgiy/java/advanced/concurrent/
rm *.class
cd $mydir/java/info/kgeorgiy/java/advanced/mapper
rm *.class
cd $mydir/java/info/kgeorgiy/java/advanced/base
rm *.class
cd $mydir/java
java -cp ./:../artifacts/ParallelMapperTest.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar info.kgeorgiy.java.advanced.mapper.Tester list ru.ifmo.rain.Petrovski.mapper.ParallelMapperImpl,ru.ifmo.rain.Petrovski.concurrent.IterativeParallelism "$1"
cd ..

