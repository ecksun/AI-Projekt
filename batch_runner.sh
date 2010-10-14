#!/bin/bash

run () {
    max_time=$1
    command=$2
    solver=$3
    level=$4
    perl -e 'alarm shift @ARGV; exec "@ARGV"' $max_time $command $solver $level;
}

COMMAND="java sokoban.Main"
SOLVER="IDSPusher"
MAX_TIME=60

run_args="$MAX_TIME $COMMAND $SOLVER"
success=0
failed=0

for i in `seq $1 $2`; do
    echo -n "$i: "
    elapsed=`(time -p run "${run_args} ${i}" -q) 2>&1`
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


