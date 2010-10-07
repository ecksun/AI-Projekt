#!/bin/bash

if [ -n "$1" ]; then
    BOARD=$1
else
    BOARD=3
fi

javac -sourcepath . sokoban/Main.java
java -classpath . sokoban.Main $BOARD

