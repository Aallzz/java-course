#!/bin/bash

cat  ${0##*/} 
read -p "Press any key to continue... " -n1 -s
cd java
javac -cp ./:../artifacts/WebCrawler.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar ru/ifmo/rain/Petrovski/crawler/WebCrawler.java info/kgeorgiy/java/advanced/crawler/Tester.java
cd info/kgeorgiy/java/advanced/crawler/
#rm *.class
cd - 
java -cp ./:../artifacts/WebCrawler.jar:../lib/hamcrest-core-1.3.jar:../lib/jsoup-1.8.1.jar:../lib/junit-4.11.jar:../lib/quickcheck-0.6.jar info.kgeorgiy.java.advanced.crawler.Tester easy ru.ifmo.rain.Petrovski.crawler.WebCrawler "$1"
cd ..

