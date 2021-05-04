package com.kormoran.sensors.device;

import com.kormoran.exception.IllegalConfigurationException;
import com.kormoran.exception.WritingIdToNonIdDeviceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jssc.SerialPort;

import org.apache.log4j.Logger;

import com.kormoran.reader.protocols.ModbusReader;

import com.kormoran.sensors.Rs485;
import com.kormoran.sensors.RsDevice;

import com.kormoran.sensors.propertiesenum.Prefix;

import com.kormoran.udp.Client;
import com.kormoran.udp.SendData;

        //czujnik kierunkowy
public class DirSensor implements RsDevice {
    final static Logger logger = Logger.getLogger(DirSensor.class);

    private Rs485 rs485;    
    private ModbusReader modbusReader;
    
    private final String frame = "Sk?";
    private int id;
    private String angle;

    private String direction = "0";
    private String totalDosage = "0";
    private String totalDosagePrefix = "U";
    private String radAlarm = "0";
    private static String threshold1;
    private static String prefixThreshold1;
    private static String threshold2;
    private static String prefixThreshold2;
    private static String threshold3;
    private static String prefixThreshold3;
    private String errorCode = "0";
    
    private String initNeutrons;
    private String neutrons;
    //only for main list
    List<DirSensor> dirSensorList = new ArrayList<DirSensor>();
    


    private boolean sendData = false;
    
    public DirSensor() {
        super();
        rs485 = new Rs485();
        rs485.setBaudRate(SerialPort.BAUDRATE_9600);
        rs485.setDataBits(SerialPort.DATABITS_8);
        rs485.setParityBit(SerialPort.PARITY_NONE);
        rs485.setStopBit(SerialPort.STOPBITS_1);   
    }
    
    public static boolean isConfigurationCorrect() throws IllegalConfigurationException {
        double thres1  = Double.valueOf(threshold1);
        double thres2  = Double.valueOf(threshold2);
        double thres3  = Double.valueOf(threshold3);
        double factor1 = getFactor(prefixThreshold1);
        double factor2 = getFactor(prefixThreshold2);
        double factor3 = getFactor(prefixThreshold3);
        double value1 = thres1*factor1;
        double value2 = thres2*factor2;
        double value3 = thres3*factor3;
        return (value3>=value2&&value2>=value1);
    }
    
    public  int isAlarm(){
/*        try {
            double thres1 = Double.valueOf(threshold1);
            double thres2 = Double.valueOf(threshold2);
            double thres3 = Double.valueOf(threshold3);
            int factor1 = getFactor(prefixThreshold1);
            int factor2 = getFactor(prefixThreshold2);
            int factor3 = getFactor(prefixThreshold3);
            double value1 = thres1 * factor1;
            double value2 = thres2 * factor2;
            double value3 = thres3 * factor3;
            
            double totalDos = Double.valueOf(totalDosage);
            int totalFac = getFactor(totalDosagePrefix);
            double total = totalDos * totalFac;
            
            if(total>value3){
                return 3;
            }else if(total>value2){
                return 2;
            }else if(total>value1){
                return 1;
            }else{
                return 0;
            }
            
            
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalConfigurationException e) {
            logger.error(e.getMessage(), e);
        }*/
        
        return 0;
    }
    
    public static int getFactor(String value1) throws IllegalConfigurationException{
/*        String value = value1.toLowerCase();
        if(value.equalsIgnoreCase("u")){
            return  1000;
        }else if(value.equalsIgnoreCase("m")){
            return 1000000;
        }else if(value.equalsIgnoreCase("n")){
            return 1;
        }else{
            throw new IllegalConfigurationException("Excepted prefixes: m, u , n (no case sensitive");
        }*/
        return 1;
    }

    @Override
    public void setData(byte[] data) {             
/*        try{
            if(data==null){
                errorCode = String.valueOf(1<<(id-1));                   
                totalDosage = "0";
//                sendData = false;
                return;
            }else{
//                 sendData = true;         
                
              *//*  System.out.println("Dir sensor:"+" ");
                for(int i = 0;i<data.length;i++){
                    System.out.print(String.format("0x%02X",data[i])+" ");

                } 
                System.out.println();  *//*
                for(int i = 0;i<data.length-12;i++){
                   if((0xFF&data[i]) ==0x01&& ((0xFF&data[i+1]) == 0x1f|| (0xFF&data[i+1])  == 0xaa)){
                        String value = String.valueOf((char)data[i+4]).concat(String.valueOf((char)data[i+5])).
                            concat(String.valueOf((char)data[i+6])).concat(String.valueOf((char)data[i+7])).
                            concat(String.valueOf((char)data[i+8])).concat(String.valueOf((char)data[i+9])).
                            concat(String.valueOf((char)data[i+10])).concat(String.valueOf((char)data[i+11]));
                        value = value.replaceAll("\\s", "0");
                        totalDosage =String.valueOf(new Double((Double.valueOf(value)*1000)).intValue());
                        totalDosagePrefix = "N";
                        radAlarm =  String.valueOf(isAlarm());
                        errorCode = "0";  
                        
                       neutrons = String.valueOf(data[i+25]);

                       if(initNeutrons==null){
                           initNeutrons = neutrons;
                       }
                       
                       if (id ==1){         
                           checkAllDevices();
                       }
                        break;
                   }
                }

            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        for(DirSensor a:dirSensorList){
            errorCode = String.valueOf(Integer.valueOf(errorCode) | Integer.valueOf(a.getErrorCode()));
        }*/
    }



    @Override
    
    public void setId(int id) throws WritingIdToNonIdDeviceException, IllegalConfigurationException {
        if(id<1||id>5){
            throw new IllegalConfigurationException("Id must be between 1 and 14");
        }
        switch (id){
        case 1:
            angle = "0";
            break;
        case 2:
            angle = "60";
            break;
        case 3:
            angle = "120";
            break;
        case 4:
            angle = "-120";
            break;
        case 5: 
            angle = "-60";
            break;
        }
        this.id = id;
    }

    @Override
    public boolean needRequest() {
        return true;
    }


    @Override
    public void sendFrame(String comPort) throws Exception{
        modbusReader = new ModbusReader();
        modbusReader.sentBytesWithoutCheckSum(frame.getBytes(), comPort, rs485);
    }

    @Override
    public byte[] getFrame(String comPort) {
        return modbusReader.getBytes() ;
    }



    @Override
    public void setSimulateData() {
    }

    @Override
    public void generateSimulationData() {
        Random random = new Random();
        direction  = String.valueOf(random.nextInt(360)-180);
        totalDosage = String.valueOf(random.nextInt(1000));
        
        int temp = new Random().nextInt(3);
        for(Prefix value:Prefix.values()){
            if(value.getValue()==temp){
                totalDosagePrefix = value.getName();
            }
        }
        radAlarm =  String.valueOf(isAlarm());
        errorCode =  String.valueOf(random.nextInt(16));
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setTotalDosage(String totalDosage) {
        this.totalDosage = totalDosage;
    }

    public String getTotalDosage() {
        return totalDosage;
    }

    public void setTotalDosagePrefix(String totalDosagePrefix) {
        this.totalDosagePrefix = totalDosagePrefix;
    }

    public String getTotalDosagePrefix() {
        return totalDosagePrefix;
    }

    public void setRadAlarm(String radAlarm) {
        this.radAlarm = radAlarm;
    }

    public String getRadAlarm() {
        return radAlarm;
    }

    public static void setThreshold1(String threshold1) {
        DirSensor.threshold1 = threshold1;
    }

    public static String getThreshold1() {
        return threshold1;
    }

    public static void setPrefixThreshold1(String prefixThreshold1) {
        DirSensor.prefixThreshold1 = prefixThreshold1.toUpperCase();
    }

    public static String getPrefixThreshold1() {
        return prefixThreshold1;
    }

    public static void setThreshold2(String threshold2) {
        DirSensor.threshold2 = threshold2;
    }

    public static String getThreshold2() {
        return threshold2;
    }

    public static void setPrefixThreshold2(String prefixThreshold2) {
        DirSensor.prefixThreshold2 = prefixThreshold2.toUpperCase();
    }

    public static String getPrefixThreshold2() {
        return prefixThreshold2;
    }

    public static void setThreshold3(String threshold3) {
        DirSensor.threshold3 = threshold3;
    }

    public static String getThreshold3() {
        return threshold3;
    }

    public static void setPrefixThreshold3(String prefixThreshold3) {
        DirSensor.prefixThreshold3 = prefixThreshold3.toUpperCase();
    }

    public static String getPrefixThreshold3() {
        return prefixThreshold3;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getUdpFrame() {
        return new SendData().sendCards(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {
        if (id ==1){        
            new Thread(new Client(frame)).start();
        }
    }
    
    public void checkAllDevices(){
 /*       String maxTotalDosageTemp = totalDosage;
        direction = angle;
        for(DirSensor a:dirSensorList){
           if(Integer.valueOf(a.getTotalDosage())>Integer.valueOf(maxTotalDosageTemp)){
               maxTotalDosageTemp = a.getTotalDosage();
               direction = a.getAngle();
               radAlarm = a.getRadAlarm();
               totalDosagePrefix = a.getTotalDosagePrefix();
           }
        }
        totalDosage = maxTotalDosageTemp;  */
    }
    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getAngle() {
        return angle;
    }
    
    public int getId() {
        return id;
    }

    public void setDirSensorList(List<DirSensor> dirSensorList) {
        this.dirSensorList = dirSensorList;
    }

    public List<DirSensor> getDirSensorList() {
        return dirSensorList;
    }

    public String getUdp2Frame() {
        return null;
    }



    public String setThreshold(String threshold) {
        return null;
    }

    public void setInitNeutrons(String initNeutrons) {
        this.initNeutrons = initNeutrons;
    }

    public String getInitNeutrons() {
        return initNeutrons;
    }

    public void setNeutrons(String neutrons) {
        this.neutrons = neutrons;
    }

    public String getNeutrons() {
        return neutrons;
    }
}
