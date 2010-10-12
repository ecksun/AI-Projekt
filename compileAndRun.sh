#!/bin/bash

if [ -n "$1" ]; then
    SOLVER=$1
else
    SOLVER="IDS"
fi

if [ -n "$2" ]; then
    BOARD=$2
else
    BOARD=3
fi

javac -sourcepath . sokoban/Main.java sokoban/solvers/*.java
java -classpath . sokoban.Main $SOLVER $BOARD

