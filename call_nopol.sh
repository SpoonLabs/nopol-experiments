#!/bin/sh
# executes nopol based on the four parameters passed by reproduce.py

java ${JVM_PARA} -jar data/lib/nopol.jar nopol $1 $2 z3 $3 $4


