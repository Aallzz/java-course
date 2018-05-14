#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
cd java/
javadoc -link https://docs.oracle.com/javase/8/docs/api/ -private -d ../MyJavaDoc -cp ./../artifacts/JarImplementorTest.jar:./../lib/hamcrest-core-1.3.jar:./../lib/junit-4.11.jar:./../lib/jsoup-1.8.1.jar:./../lib/quickcheck-0.6.jar: ./ru/ifmo/rain/Petrovski/implementor/Implementor.java ./ru/ifmo/rain/Petrovski/implementor/package-info.java ./info/kgeorgiy/java/advanced/implementor/Impler.java ./info/kgeorgiy/java/advanced/implementor/JarImpler.java ./info/kgeorgiy/java/advanced/implementor/ImplerException.java
cd ..
read -p "Show generated documentation? (y/n)`echo $'\n>'`" -n 1 -r
echo 
if [[ $REPLY =~ ^[Yy]$ ]] 
then
  firefox "MyJavaDoc/overview-summary.html"
fi

