#!/bin/bash
cd /u01/app/moxa
./mxuninst
./mxinst
./mxaddsvr 192.168.40.120 16
./mxaddsvr 192.168.40.121 16
./mxaddsvr 192.168.40.122 16


