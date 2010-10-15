#!/bin/bash

rm -f server_levels.slc

for i in {1..136}; do
    echo ";LEVEL ${i}" >> server_levels.slc; 
    nc cvap103.nada.kth.se 5555 -q 1 <<< $i >> server_levels.slc; 
done
