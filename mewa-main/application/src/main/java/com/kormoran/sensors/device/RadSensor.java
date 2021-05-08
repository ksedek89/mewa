package com.kormoran.sensors.device;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import jssc.SerialPort;

import org.apache.log4j.Logger;

import com.kormoran.reader.protocols.ModbusReader;

import com.kormoran.sensors.Rs485;
import com.kormoran.sensors.RsDevice;

import com.kormoran.sensors.propertiesenum.Prefix;

import com.kormoran.udp.SendData;


        //czujnik radio chemiczny
public class RadSensor  implements RsDevice {
    final static Logger logger = Logger.getLogger(RadSensor.class);

    private byte [] frame = new byte[]{0x23, 0x65, 0x03, (byte)0xEB, 0x5B };
    
    private byte [] ventilation= new byte[]{0x23, 0x62, 0x03, (byte)0xE9, (byte)0x6B };
    
    private byte [] turnOnFrame = new byte[]{0x23, 0x62, 0x04, (byte)0xA8, (byte)0xA9 };

    private byte [] turnOffFrame = new byte[]{0x23, 0x62, 0x05, (byte)0x69, (byte)0x69 };
    

    private Rs485 rs485;
    private ModbusReader modbusReader;

    int id;                             
    private String totalDosageInt = "0";         
    private String prefixTotalDosageInt = "N";   
    private String dosagePowerInt = "0";         
    private String prefixDosagePowerInt = "N";   
    private String totalDosageExt = "0";         
    private String prefixTotalDosageExt = "N";   
    private String dosagePowerExt = "0";         
    private String prefixDosagePowerExt = "N";   
    private String timestampFromTurnOn; 
    private String pollutionG = "0";          
    private String pollutionH = "0";          
    private String pollutionT = "0";          
    private String radAlarmInt = "0";            
    private String radAlarmExt = "0";            
    private String chemAlarm = "0";           
    private String errorCode = "0";           
    
    private List<RadSensor> radSensorList = new ArrayList<RadSensor>();
    private boolean polutionFromOther = false;
  
    private boolean sendClearVentilation;
    
    private final long timeStarted;
    
    
    private static boolean sendVentilationFailure;

    private boolean turnOn;
    private boolean turnOff;


    private boolean sendData = false;

    @Override
    public String getUdpFrame() {
         return  new SendData().sendCarrcInt(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {

    }


    
    public RadSensor() {
        super();
        timeStarted = System.currentTimeMillis();
        
        rs485 = new Rs485();
        rs485.setBaudRate(SerialPort.BAUDRATE_19200);
        rs485.setDataBits(SerialPort.DATABITS_8);
        rs485.setParityBit(SerialPort.PARITY_NONE);
        rs485.setStopBit(SerialPort.STOPBITS_1);
    }

    @Override
    public void setData(byte[] data) {
        getTimeDifference();
        errorCode = "0";
        try{
 /*          byte [] data1 =new byte[]{0x23, 0x61, 0x01, 0x0, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0, 0x0, 0x0, 0x0, 0x45, (byte) 0xde, 0x23, 0x63, 0x0,
            (byte) 0x80, 0x67, 0x34, (byte) 0x81, 0x48, (byte) 0xc3, 0x33, (byte) 0xc3, 0x6c, (byte) 0xda, 0x32, 0x13,
            (byte) 0xac, (byte) 0xc7, 0x32, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x46, 0x4f, 0x74, 0x73, 0x74, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                                   0x0, 0x0, 0x0, 0x15, (byte) 0xc4
        };  */
         
         if(data==null){
            sendData = false;
         }else{
             sendData = true;
/*            for(int i = 0;i<data.length;i++){
                 System.out.print(String.format("0x%02X",data[i])+" ");
             }
             System.out.println();    */  
            for(int i = 0;i<data.length-15;i++){
                if((0xFF&data[i]) ==0x23&& (0xFF&data[i+1]) == 0x61){     
                    pollutionG = String.valueOf(data[i+7]);
                    pollutionH = String.valueOf(data[i+8]);
                    pollutionT = String.valueOf(data[i+9]);
                    
                    if(id == 3){
                        if(!pollutionG.equalsIgnoreCase("0") || !pollutionH.equalsIgnoreCase("0") || !pollutionT.equalsIgnoreCase("0") ){
                            sendVentilationFailure = true;
                        }else{
                            sendVentilationFailure = false;
                        }
                    }
                    
                    int temp =(data[i+3]<<8)+data[i+2];
                    //bit zerowy
                    errorCode =String.valueOf(Integer.valueOf(errorCode) | ((getBit(temp,4)|getBit(temp,5)|getBit(temp,6))<<3));
                    errorCode =String.valueOf(Integer.valueOf(errorCode) | ((getBit(temp,8)|getBit(temp,9))<<2));
                    errorCode =String.valueOf(Integer.valueOf(errorCode) | ((getBit(temp,7)<<4)));
                   //wyjatek dla jednego css bez sondy zew
                    if(id !=1){
                        //sonda zewnetrzna
                        errorCode =String.valueOf(Integer.valueOf(errorCode) | ((getBit(temp,2)<<0)));
                    }
                    errorCode =String.valueOf(Integer.valueOf(errorCode) | ((getBit(temp,3)<<1)));

                   if(pollutionG.equalsIgnoreCase("0")&&pollutionH.equalsIgnoreCase("0")&&pollutionT.equalsIgnoreCase("0") ){

                        sendClearVentilation = true;
                   }else{
                       if(radSensorList!=null && radSensorList.size()>0){
                           for(RadSensor a: radSensorList){
                               a.setPolutionFromOther(true);
                           }
                       }
                        sendClearVentilation = false;
                   }
                    break;
                }
            } 
             
             
             for(int i = 0;i<data.length-40;i++){
                if((0xFF&data[i]) ==0x23&& (0xFF&data[i+1]) == 0x63){
                    byte [] tempInt = new byte[]{data[i+6], data[i+7],data[i+8],data[i+9]  };
                    byte [] tempExt = new byte[]{data[i+2], data[i+3],data[i+4],data[i+5]  };
                    //nGy
                    float aInt =  Rs485.ieee24Format(tempInt)*1000000000;
                    dosagePowerInt= String.valueOf(Float.valueOf(aInt).intValue());
                    prefixDosagePowerInt = "N";
                    
                    float aExt =  Rs485.ieee24Format(tempExt)*1000000000;
                    dosagePowerExt= String.valueOf(Float.valueOf(aExt).intValue());
                    prefixDosagePowerExt = "N";
                    
                    tempInt = new byte[]{data[i+14], data[i+15],data[i+16],data[i+17]  };
                    aInt =  Rs485.ieee24Format(tempInt)*1000000000;
                    totalDosageInt = String.valueOf(Float.valueOf(aInt).intValue());
                    
                    prefixTotalDosageInt = "N";
                    
                    tempExt = new byte[]{data[i+10], data[i+11],data[i+12],data[i+13]  };
                    aExt =  Rs485.ieee24Format(tempExt)*1000000000;
                    totalDosageExt = String.valueOf(Float.valueOf(aExt).intValue());
                    prefixTotalDosageExt = "N";
                    
                    System.out.println();
                    if(dosagePowerExt.equalsIgnoreCase("0")){
                        dosagePowerExt = dosagePowerInt;
                        prefixDosagePowerExt = prefixDosagePowerInt;
                        totalDosageExt = totalDosageInt;
                        prefixTotalDosageExt = prefixTotalDosageInt;
                    }
                    
                    break;
                }
             } 
   
             
         }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        setChemAlarm();
        setRadAlarm();
                     
    }
    
    public int getBit(int value, int position)
    {
       return (value >> position) &1;
    }
    


    @Override
    public void setId(int id){

        
    }
    
    @Override
    public void generateSimulationData() {
        getTimeDifference();
        
        Random random = new Random();
        totalDosageInt = String.valueOf(random.nextInt(1000));

        int temp = new Random().nextInt(3);
        for(Prefix value:Prefix.values()){
            if(value.getValue()==temp){
                prefixTotalDosageInt = value.getName();
            }
        }
        
        random = new Random();
        dosagePowerInt = String.valueOf(random.nextInt(1000));

        temp = new Random().nextInt(3);
        for(Prefix value:Prefix.values()){
            if(value.getValue()==temp){
                prefixDosagePowerInt = value.getName();
            }
        }
            
            
            random = new Random();
            pollutionG = String.valueOf(random.nextInt(6));   
            pollutionH = String.valueOf(random.nextInt(6));   
            pollutionT = String.valueOf(random.nextInt(6));   
           setRadAlarm();  
           setChemAlarm();
        
        
            generateErrorCode();
            
        }
    
    public void setRadAlarm(){
        radAlarmInt =  String.valueOf(isAlarmInt());
        radAlarmExt =  String.valueOf(isAlarmExt());
        
    }
    
    
    public  int isAlarmExt(){
            double thres1 = Double.valueOf(DirSensor.getThreshold1());
            double thres2 = Double.valueOf(DirSensor.getThreshold2());
            double thres3 = Double.valueOf(DirSensor.getThreshold3());
            int factor1 = DirSensor.getFactor(DirSensor.getPrefixThreshold1());
            int factor2 = DirSensor.getFactor(DirSensor.getPrefixThreshold2());
            int factor3 = DirSensor.getFactor(DirSensor.getPrefixThreshold3());

            double value1 = thres1 * factor1;
            double value2 = thres2 * factor2;
            double value3 = thres3 * factor3;
            
            double totalDos = Double.valueOf(dosagePowerExt);
            int totalFac = DirSensor.getFactor(prefixDosagePowerExt);
            
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
            

    }
    
    
    public  int isAlarmInt(){
            int thres1 = Integer.valueOf(DirSensor.getThreshold1());
            int thres2 = Integer.valueOf(DirSensor.getThreshold2());
            int thres3 = Integer.valueOf(DirSensor.getThreshold3());
 
            int factor1 = DirSensor.getFactor(DirSensor.getPrefixThreshold1());
            int factor2 = DirSensor.getFactor(DirSensor.getPrefixThreshold2());
            int factor3 = DirSensor.getFactor(DirSensor.getPrefixThreshold3());
            

            int value1 = thres1 * factor1;
            int value2 = thres2 * factor2;
            int value3 = thres3 * factor3;
            
            int totalDos = Integer.valueOf(dosagePowerInt);
            int totalFac = DirSensor.getFactor(prefixDosagePowerInt);
            
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

    }
    
    
    public void setChemAlarm(){
        if(pollutionG!= null&&pollutionH!=null&&pollutionT !=null){
        if(Integer.valueOf(pollutionG)==5||Integer.valueOf(pollutionH)==5||Integer.valueOf(pollutionT)==5){
            chemAlarm = "5";
        }else if(Integer.valueOf(pollutionG)==4||Integer.valueOf(pollutionH)==4||Integer.valueOf(pollutionT)==4){
            chemAlarm = "4";
        }else if(Integer.valueOf(pollutionG)==3||Integer.valueOf(pollutionH)==3||Integer.valueOf(pollutionT)==3){
            chemAlarm = "3";
        }else if(Integer.valueOf(pollutionG)==2||Integer.valueOf(pollutionH)==2||Integer.valueOf(pollutionT)==2){
            chemAlarm = "2";
        }else if(Integer.valueOf(pollutionG)==1||Integer.valueOf(pollutionH)==1||Integer.valueOf(pollutionT)==1){
            chemAlarm = "1";
        }else{
            chemAlarm = "0";
        }
        }
    }
    
    public void generateErrorCode(){
        errorCode = "0";
        Random random = new Random();
        int numberOfErrors = random.nextInt(6); 
        if(numberOfErrors>0){
        int [] values = new int[numberOfErrors];
        for(int i=0;i<values.length;i++){
            values[i] = random.nextInt(5); 
        }
        for(int val :values){
            errorCode =String.valueOf(Integer.valueOf(errorCode) | (1<<val) );
        }
        }else{
            errorCode = "0";
        }
    }
    
    public void getTimeDifference(){
        long a = System.currentTimeMillis();
        long difference = a-timeStarted;    
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;   
        hours = hours % 100;
        minutes = minutes % 60;
        seconds = seconds % 60;
        
        String hoursStr, minStr, secStr;
        
        hoursStr = (String.valueOf(hours).length()==1?"0".concat(String.valueOf(hours)):String.valueOf(hours));
        minStr = (String.valueOf(minutes).length()==1?"0".concat(String.valueOf(minutes)):String.valueOf(minutes));
        secStr = (String.valueOf(seconds).length()==1?"0".concat(String.valueOf(seconds)):String.valueOf(seconds));

        timestampFromTurnOn =  hoursStr + minStr + secStr ; 
    }
    

    @Override
    public boolean needRequest() {
        
        return true;
    }



    @Override
    public void sendFrame(String comPort) throws Exception {
        modbusReader = new ModbusReader(); 
        modbusReader.sentBytesWithoutCheckSum(frame, comPort, rs485);   

    }

    @Override
    public byte[] getFrame(String comPort) {
        byte[] value =  modbusReader.getBytesWithoutClosing();
        try{
            if(sendClearVentilation){
                logger.warn("Przesylam czyszczenie");
                modbusReader.sentBytes(ventilation);
            } 
            if(turnOn){
                turnOn = false;
                logger.warn("Wlaczam PRS nr : "+ id);
                modbusReader.sentBytes(turnOnFrame);  
            }
            if(polutionFromOther){
                turnOn = false;
                logger.warn("Wlaczam PRS (zanieczyszczenie) nr : "+ id);
                modbusReader.sentBytes(turnOnFrame); 
                polutionFromOther = false;
            }
            if(turnOff){  
                turnOff = false;    
                logger.warn("Wylaczam PRS nr : "+ id);
                modbusReader.sentBytes(turnOffFrame);
            }
        }catch(Exception e){
            logger.error(e);
        }     
        modbusReader.closePort();
        return value;
    }


    @Override
    public void setSimulateData() {
        
    }



    public int getId() {
        return id;
    }




    public void setTimestampFromTurnOn(String timestampFromTurnOn) {
        this.timestampFromTurnOn = timestampFromTurnOn;
    }

    public String getTimestampFromTurnOn() {
        return timestampFromTurnOn;
    }

    public void setPollutionG(String pollutionG) {
        this.pollutionG = pollutionG;
    }

    public String getPollutionG() {
        return pollutionG;
    }

    public void setPollutionH(String pollutionH) {
        this.pollutionH = pollutionH;
    }

    public String getPollutionH() {
        return pollutionH;
    }

    public void setPollutionT(String pollutionT) {
        this.pollutionT = pollutionT;
    }

    public String getPollutionT() {
        return pollutionT;
    }

    public void setChemAlarm(String chemAlarm) {
        this.chemAlarm = chemAlarm;
    }

    public String getChemAlarm() {
        return chemAlarm;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }  
    public void setTotalDosageInt(String totalDosageInt) {
        this.totalDosageInt = totalDosageInt;
    }

    public String getTotalDosageInt() {
        return totalDosageInt;
    }

    public void setPrefixTotalDosageInt(String prefixTotalDosageInt) {
        this.prefixTotalDosageInt = prefixTotalDosageInt;
    }

    public String getPrefixTotalDosageInt() {
        return prefixTotalDosageInt;
    }

    public void setDosagePowerInt(String dosagePowerInt) {
        this.dosagePowerInt = dosagePowerInt;
    }

    public String getDosagePowerInt() {
        return dosagePowerInt;
    }

    public void setPrefixDosagePowerInt(String prefixDosagePowerInt) {
        this.prefixDosagePowerInt = prefixDosagePowerInt;
    }

    public String getPrefixDosagePowerInt() {
        return prefixDosagePowerInt;
    }

    public void setRadAlarmExt(String radAlarmExt) {
        this.radAlarmExt = radAlarmExt;
    }

    public String getRadAlarmExt() {
        return radAlarmExt;
    }

    public void setDosagePowerExt(String dosagePowerExt) {
        this.dosagePowerExt = dosagePowerExt;
    }

    public String getDosagePowerExt() {
        return dosagePowerExt;
    }

    public void setPrefixDosagePowerExt(String prefixDosagePowerExt) {
        this.prefixDosagePowerExt = prefixDosagePowerExt;
    }

    public String getPrefixDosagePowerExt() {
        return prefixDosagePowerExt;
    }

    public String getRadAlarmInt() {
        return radAlarmInt;
    }

    public void setTotalDosageExt(String totalDosageExt) {
        this.totalDosageExt = totalDosageExt;
    }

    public String getTotalDosageExt() {
        return totalDosageExt;
    }

    public void setPrefixTotalDosageExt(String prefixTotalDosageExt) {
        this.prefixTotalDosageExt = prefixTotalDosageExt;
    }

    public String getPrefixTotalDosageExt() {
        return prefixTotalDosageExt;
    }

    public String getUdp2Frame() {
        return  new SendData().sendCarrcExt(this);

    }


    public String setThreshold(String threshold) {
        return null;
    }

    public static void setSendVentilationFailure(boolean sendVentilationFailure) {
        RadSensor.sendVentilationFailure = sendVentilationFailure;
    }

    public static boolean isSendVentilationFailure() {
        return sendVentilationFailure;
    }

    public void setTurnOn(boolean turnOn) {
        this.turnOn = turnOn;
    }

    public boolean isTurnOn() {
        return turnOn;
    }

    public void setTurnOff(boolean turnOff) {
        this.turnOff = turnOff;
    }

    public boolean isTurnOff() {
        return turnOff;
    }

    public void setRadSensorList(List<RadSensor> radSensorList) {
        this.radSensorList = radSensorList;
    }

    public List<RadSensor> getRadSensorList() {
        return radSensorList;
    }

    public void setPolutionFromOther(boolean polutionFromOther) {
        this.polutionFromOther = polutionFromOther;
    }

    public boolean isPolutionFromOther() {
        return polutionFromOther;
    }
}
