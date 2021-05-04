package com.kormoran.udp;


import java.text.SimpleDateFormat;

import java.util.Date;



import com.kormoran.reader.Nmea0183;

import com.kormoran.sensors.device.CarfsMessage;
import com.kormoran.sensors.device.CarisMessage;
import com.kormoran.sensors.device.DirSensor;
import com.kormoran.sensors.device.DptMod;
import com.kormoran.sensors.device.Label480;
import com.kormoran.sensors.device.RadSensor;


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
    

    public String sendCards(DirSensor dirSensor) {
        StringBuilder frame = new StringBuilder();
/*        frame.append("$PCARDS,");
        frame.append(getCurrentTimeToFrame());
        frame.append(dirSensor.getDirection()+",");
        frame.append(dirSensor.getTotalDosage()+",");
        frame.append(dirSensor.getTotalDosagePrefix()+",");
        frame.append(dirSensor.getRadAlarm()+",");
        frame.append(DirSensor.getThreshold1()+",");
        frame.append(DirSensor.getThreshold2()+",");
        frame.append(DirSensor.getThreshold3()+",");
        
        frame.append("0"+",");
        frame.append(dirSensor.getNeutrons()+",");
        String difference = String.valueOf(Integer.valueOf(dirSensor.getNeutrons()).intValue() - Integer.valueOf(dirSensor.getInitNeutrons()).intValue());
        frame.append(difference+",");
        frame.append(difference+",");

        frame.append(dirSensor.getErrorCode()+"*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");*/
        return frame.toString();
    }
    
    public String sendCarrcInt(RadSensor radSensor){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARRC,");
        frame.append(getCurrentTimeToFrame());
        frame.append(radSensor.getId()+",");
        frame.append("I"+",");
        frame.append(Double.valueOf(radSensor.getTotalDosageInt()).intValue()+",");
        frame.append(radSensor.getPrefixTotalDosageInt()+",");
        frame.append(Double.valueOf(radSensor.getDosagePowerInt()).intValue()+",");
        frame.append(radSensor.getPrefixDosagePowerInt()+",");
        frame.append(DirSensor.getThreshold1()+",");
        frame.append(DirSensor.getThreshold2()+",");
        frame.append(DirSensor.getThreshold3()+",");
        frame.append(radSensor.getTimestampFromTurnOn()+",");
        frame.append("G"+radSensor.getPollutionG()+",");
        frame.append("H"+radSensor.getPollutionH()+",");
        frame.append("T"+radSensor.getPollutionT()+",");
        frame.append(radSensor.getRadAlarmInt()+",");
        frame.append(radSensor.getChemAlarm()+",");
        frame.append(radSensor.getErrorCode()+"*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }
    
    
    public String sendCarrcExt(RadSensor radSensor){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARRC,");
        frame.append(getCurrentTimeToFrame());
        
        frame.append(radSensor.getId()+",");
        frame.append("E"+",");
        frame.append(Double.valueOf(radSensor.getTotalDosageExt()).intValue()+",");
        frame.append(radSensor.getPrefixTotalDosageExt()+",");
        frame.append(Double.valueOf(radSensor.getDosagePowerExt()).intValue()+",");
        frame.append(radSensor.getPrefixDosagePowerExt()+",");
        frame.append(DirSensor.getThreshold1()+",");
        frame.append(DirSensor.getThreshold2()+",");
        frame.append(DirSensor.getThreshold3()+",");
        frame.append(radSensor.getTimestampFromTurnOn()+",");
        frame.append("G"+radSensor.getPollutionG()+",");
        frame.append("H"+radSensor.getPollutionH()+",");
        frame.append("T"+radSensor.getPollutionT()+",");
        frame.append(radSensor.getRadAlarmExt()+",");
        frame.append(radSensor.getChemAlarm()+",");
        frame.append(radSensor.getErrorCode()+"*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }
    
    
    public String sendCaris(CarisMessage carisMessage){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARIS,");
        frame.append(getCurrentTimeToFrame());        
        frame.append(carisMessage.getStatus()+",0*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }
    
    public String sendCarfs(CarfsMessage carfsMessage){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARFS,");
        frame.append(getCurrentTimeToFrame());
        frame.append(carfsMessage.getInstallationId()+",");
        if(RadSensor.isSendVentilationFailure()){
            frame.append("F,");            
       }else{            
            frame.append(carfsMessage.getWorkState()+",");
        }
        frame.append(carfsMessage.getWorkMode()+",");
        frame.append(carfsMessage.getEfficiency()+",");
        frame.append(carfsMessage.getInitialResistance()+",");
        frame.append(carfsMessage.getResistance()+"*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }

    public String sendCarps(DptMod dptmod) {
/*        StringBuilder frame = new StringBuilder();
        frame.append("$PCARPS,");
        frame.append(getCurrentTimeToFrame());
        frame.append(dptmod.getId() + ",");
        frame.append(dptmod.getPressure()+ ",");
        frame.append(dptmod.getThreshold()+ ",");
        frame.append(dptmod.getAlarm()+ ",");
        frame.append(dptmod.getErrorCode()+ "*");
        frame.append(Integer.toHexString(Nmea0183.calculateCheckSum(frame.toString())));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();*/
        return null;
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
