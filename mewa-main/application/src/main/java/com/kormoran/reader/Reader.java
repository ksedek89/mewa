package com.kormoran.reader;

import java.util.Timer;


import com.kormoran.sensors.Device;

import com.kormoran.sensors.device.DirSensor;

import com.kormoran.sensors.device.RadSensor;


import com.kormoran.timer.ReadDataTimer;

public class Reader {

    private static int timestamp;

    private Configuration configuration;   

    public Reader(Configuration configuration){
        super();
        this.configuration = configuration;
    }
    
    public void initReader(){
                //connect all Dir sensors
        for(Device a:configuration.getDeviceList()){
            //connectDevices - poziazanie kolejnych czujnikow z pierwszym
            if(a.getRsDevice() instanceof DirSensor){
                DirSensor aa = (DirSensor)a.getRsDevice();
                if(((DirSensor)a.getRsDevice()).getId() == 1){
                    DirSensor mainSensor;
                    mainSensor = (DirSensor)a.getRsDevice();
                    for(Device b:configuration.getDeviceList()){
                        if(b.getRsDevice() instanceof DirSensor){
                            if(((DirSensor)b.getRsDevice()).getId() != 1){
                                mainSensor.getDirSensorList().add((DirSensor)b.getRsDevice());
                            }
                        }
                    } 
                    break;
                }
            }                
        }

        for(Device a:configuration.getDeviceList()){           
            if(a.getRsDevice() instanceof RadSensor){
                RadSensor aa = (RadSensor)a.getRsDevice();
                    RadSensor mainSensor;
                    mainSensor = (RadSensor)a.getRsDevice();
                    for(Device b:configuration.getDeviceList()){
                        if(b.getRsDevice() instanceof RadSensor){
                                mainSensor.getRadSensorList().add((RadSensor)b.getRsDevice());
                        }
                    } 
                }
            }
        
        
        
        
        for(Device a:configuration.getDeviceList()){
            Timer timerDevice = new Timer();
            ReadDataTimer readDataTimer = new ReadDataTimer(a);
            timerDevice.scheduleAtFixedRate(readDataTimer, 0, timestamp);
         }
        

        


    }


    public static void setTimestamp(int timestamp) {
        Reader.timestamp = timestamp;
    }

    public static int getTimestamp() {
        return timestamp;
    }
}
