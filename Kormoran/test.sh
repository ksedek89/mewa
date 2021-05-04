#!/bin/bash
# use predefined variables to access passed arguments
#echo arguments to the shell
cd /tmp/moxa/
./mxuninst
./mxinst SP1
./mxaddsvr $1 32

