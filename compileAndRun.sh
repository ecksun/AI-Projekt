#!/bin/bash

minHEAP=2G
maxHEAP=3G
stackSize=8M

if [ -n "$2" ]; then
    SOLVER=$2
else
    SOLVER="BidirectionalIDS"
fi

if [ -n "$1" ]; then
    BOARD=$1
else
    BOARD=3
fi

javac -sourcepath . sokoban/Main.java sokoban/solvers/*.java
# java -classpath . -Xms$minHEAP -Xmx$maxHEAP -Xss$stackSize -XX:+AggressiveOpts -XX:CompileThreshold=10 sokoban.Main $SOLVER $BOARD
java -classpath . -Xms$minHEAP -Xmx$maxHEAP -Xss$stackSize sokoban.Main $SOLVER $BOARD

