#!/bin/bash
# use predefined variables to access passed arguments
#echo arguments to the shell
cd /tmp/moxa/
sudo ./mxuninst
sudo ./mxuninst
sudo ./mxaddsvr 192.168.40.120 16
sudo ./mxaddsvr 192.168.40.121 16
sudo ./mxaddsvr 192.168.40.122 16
sudo ./mxloadsvr
