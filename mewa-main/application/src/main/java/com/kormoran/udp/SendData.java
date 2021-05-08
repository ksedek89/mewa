package com.kormoran.udp;


import java.text.SimpleDateFormat;

import java.util.Date;



import com.kormoran.reader.Nmea0183;

import com.kormoran.sensors.device.DirSensor;
import com.kormoran.sensors.device.Label480;


public class SendData {


    public SendData() {
        super();
    }
    
    public String sendCarse(String mode, String id, String state) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARSE,");
        frame.append(getCurrentTimeToFrame());
        frame.append(mode+",");
        frame.append(id+",");
        frame.append(state+",");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }    
    public String sendCarca() {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARCA,");
        frame.append(getCurrentTimeToFrame());
        frame.append(DirSensor.getThreshold1()+",");
        frame.append(DirSensor.getPrefixThreshold1()+",");
        frame.append(DirSensor.getThreshold2()+",");
        frame.append(DirSensor.getPrefixThreshold2()+",");
        frame.append(DirSensor.getThreshold3()+",");
        frame.append(DirSensor.getPrefixThreshold3()+"*");  
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }
    
    
    //do testow
    public String sendCarcc() {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARCC,");
        frame.append(getCurrentTimeToFrame());
        String threshold1 = "1";
        String prefixThreshold1 = "N";
        String threshold2 =  "10" ;
        String prefixThreshold2 = "N";;
        String threshold3 = "80";
        String prefixThreshold3 = "N";
        frame.append(threshold1+",");
        frame.append(prefixThreshold1+",");
        frame.append(threshold2+",");
        frame.append(prefixThreshold2+",");
        frame.append(threshold3+",");
        frame.append(prefixThreshold3+"*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }





    public String sendCarac(Label480 label480) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARAC,");
        frame.append(getCurrentTimeToFrame());
        if (label480.getOxygen() != null) {
            frame.append(label480.getOxygen() + ",");
            frame.append(Label480.THRESHOLD_O2 + ",");
            frame.append(label480.getOxygenAlarm()+",");
            frame.append(label480.getCo2() + ",");
            frame.append(Label480.THRESHOLD_CO2 + ",");
            frame.append(label480.getAlarmCo2()+",");
            frame.append(label480.getErrorCode() + "*");

        } 
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }


    public String getCurrentTimeToFrame() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate.substring(0, strDate.length() - 1).concat(",");
    }
}
