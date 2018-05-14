#!/bin/bash

for f in $(find .. -name "*.class"); do echo $f; rm $f; done

