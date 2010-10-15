#!/bin/bash

run () {
    max_time=$1
    command="${2}"
    level=$3
    perl -e 'alarm shift @ARGV; exec "@ARGV"' $max_time $command $level;
}

# Default values
#first=$1
#last=$2

if [ -n "${3}" ]; then
    COMMAND="${3}"
else
    COMMAND="java sokoban.Main IDSPusher"
fi

if [ -n "$4" ]; then
    MAX_TIME=$4
else
    MAX_TIME=60
fi

echo "Running test on levels $1 to $2 with command $COMMAND and max time $MAX_TIME."


success=0
failed=0

for i in `seq $1 $2`; do
    echo -n "$i: "
    elapsed=`(time -p run $MAX_TIME "${COMMAND}" $i -q) 2>&1`
    if [ $? -ne "0" ]; then
        echo -n "failure"
        failed=`expr $failed + 1`
    else
        echo -n "success"
        success=`expr $success + 1`
    fi
    elapsed=`echo "$elapsed" | grep real | sed 's/.* //g'`
    echo ", $elapsed s"
done

echo "Solved: $success"
echo "Not solved: $failed"


