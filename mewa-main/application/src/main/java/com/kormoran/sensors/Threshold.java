package com.kormoran.sensors;


import com.kormoran.exception.IllegalConfigurationException;

import com.kormoran.exception.WrongChecksumException;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;

import java.util.Properties;

import com.kormoran.sensors.device.DirSensor;
import org.apache.log4j.Logger;

import com.kormoran.reader.Nmea0183;

import com.kormoran.udp.Client;

import com.kormoran.udp.SendData;

public class Threshold implements Runnable{
    private final static Logger logger = Logger.getLogger(Threshold.class);

    private String value;
    
    private static String threshold1;
    private static String prefixThreshold1;
    private static String threshold2;
    private static String prefixThreshold2;
    private static String threshold3;
    private static String prefixThreshold3;
    
    private static boolean checkingChecksum;
    
    public Threshold(String value) {
        super();
        this.value = value;
    }


    @Override
    public void run() {
/*        if(value.startsWith("$PCARCC")){
            try{
                String [] data= value.split(",");
                String [] checkSum = value.split("\\*");  
                String [] data1 = checkSum[0].split(",");  
            if(checkingChecksum){    
                if(!((Nmea0183.calculateCheckSum(checkSum[0].concat("*")) == Integer.parseInt(checkSum[1].substring(0,3).replace("\r", "").replace("\n", ""), 16)))){
                    new WrongChecksumException("Wrong checksum for PCARCC").printStackTrace();                     
                }              
            }
            if(data.length>7){ 
                threshold1 = data[2] ;
                prefixThreshold1 = data[3];
                threshold2 = data[4];
                prefixThreshold2 = data[5];
                threshold3 = data[6];
                prefixThreshold3 = data1[7];                      
                if(isConfigurationCorrect()){
                    DirSensor.setThreshold1(threshold1);
                    DirSensor.setPrefixThreshold1(prefixThreshold1);
                    DirSensor.setThreshold2(threshold2);
                    DirSensor.setPrefixThreshold2(prefixThreshold2);               
                    DirSensor.setThreshold3(threshold3);
                    DirSensor.setPrefixThreshold3(prefixThreshold3);                     
                    new Thread(new Client(new SendData().sendCarca())).start();
                    updateThreshold();                 
                    Properties prop = new Properties();
                    String propFileName = "threshold.properties";
                    OutputStream output = new FileOutputStream(propFileName);
                    DirSensor.setPrefixThreshold1("u");
                    DirSensor.setPrefixThreshold2("u");
                    DirSensor.setPrefixThreshold3("u");
                    prop.setProperty("threshold1", DirSensor.getThreshold1().concat(" ").concat(DirSensor.getPrefixThreshold1().toLowerCase()));
                    prop.setProperty("threshold2", DirSensor.getThreshold2().concat(" ").concat(DirSensor.getPrefixThreshold2().toLowerCase()));
                    prop.setProperty("threshold3", DirSensor.getThreshold3().concat(" ").concat(DirSensor.getPrefixThreshold3().toLowerCase()));
                    prop.store(output, null);
                }else{
                    throw new IllegalConfigurationException();
                }   
            }
            }catch(IllegalConfigurationException e){
                logger.error(e.getMessage(), e);
            }catch(IOException e){
                logger.error(e.getMessage(), e);
            }
        }*/
    }
    
    public static void updateThreshold(){
        if(DirSensor.getPrefixThreshold1().equalsIgnoreCase("n")){
            DirSensor.setThreshold1(String.valueOf((Double.valueOf(DirSensor.getThreshold1()) /1000)));
        }else if(DirSensor.getPrefixThreshold1().equalsIgnoreCase("m")){
            DirSensor.setThreshold1(String.valueOf((Double.valueOf(DirSensor.getThreshold1()) *1000)));
        }
        if(DirSensor.getPrefixThreshold2().equalsIgnoreCase("n")){
            DirSensor.setThreshold2(String.valueOf((Double.valueOf(DirSensor.getThreshold2()) /1000)));
        }else if(DirSensor.getPrefixThreshold2().equalsIgnoreCase("m")){
            DirSensor.setThreshold2(String.valueOf((Double.valueOf(DirSensor.getThreshold2()) *1000)));
        }        
        if(DirSensor.getPrefixThreshold3().equalsIgnoreCase("n")){
            DirSensor.setThreshold3(String.valueOf((Double.valueOf(DirSensor.getThreshold3()) /1000)));
        }else if(DirSensor.getPrefixThreshold3().equalsIgnoreCase("m")){
            DirSensor.setThreshold3(String.valueOf((Double.valueOf(DirSensor.getThreshold3()) *1000)));
        }    
    }
    
    public static boolean isConfigurationCorrect() throws IllegalConfigurationException {
        int thres1  = Integer.valueOf(threshold1);
        int thres2  = Integer.valueOf(threshold2);
        int thres3  = Integer.valueOf(threshold3);
        int factor1 = DirSensor.getFactor(prefixThreshold1);
        int factor2 = DirSensor.getFactor(prefixThreshold2);
        int factor3 = DirSensor.getFactor(prefixThreshold3);
        int value1 = thres1*factor1;
        int value2 = thres2*factor2;
        int value3 = thres3*factor3;
        return (value3>=value2&&value2>=value1);
    }

    public static void setThreshold1(String threshold1) {
        Threshold.threshold1 = threshold1;
    }

    public static String getThreshold1() {
        return threshold1;
    }

    public static void setPrefixThreshold1(String prefixThreshold1) {
        Threshold.prefixThreshold1 = prefixThreshold1;
    }

    public static String getPrefixThreshold1() {
        return prefixThreshold1;
    }

    public static void setThreshold2(String threshold2) {
        Threshold.threshold2 = threshold2;
    }

    public static String getThreshold2() {
        return threshold2;
    }

    public static void setPrefixThreshold2(String prefixThreshold2) {
        Threshold.prefixThreshold2 = prefixThreshold2;
    }

    public static String getPrefixThreshold2() {
        return prefixThreshold2;
    }

    public static void setThreshold3(String threshold3) {
        Threshold.threshold3 = threshold3;
    }

    public static String getThreshold3() {
        return threshold3;
    }

    public static void setPrefixThreshold3(String prefixThreshold3) {
        Threshold.prefixThreshold3 = prefixThreshold3;
    }

    public static String getPrefixThreshold3() {
        return prefixThreshold3;
    }

    public static void setCheckingChecksum(boolean checkingChecksum) {
        Threshold.checkingChecksum = checkingChecksum;
    }

    public static boolean isCheckingChecksum() {
        return checkingChecksum;
    }


}
