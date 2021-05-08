package com.kormoran.udp;


import java.text.SimpleDateFormat;

import java.util.Date;



import com.kormoran.reader.Nmea0183;

import com.kormoran.sensors.device.Label480;


public class SendData {


    public SendData() {
        super();
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
