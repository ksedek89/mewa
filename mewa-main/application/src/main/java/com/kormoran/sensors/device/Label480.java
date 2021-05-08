package com.kormoran.sensors.device;


import java.util.Random;

import jssc.SerialPort;


import com.kormoran.reader.protocols.ModbusReader;

import com.kormoran.sensors.Rs485;

import com.kormoran.sensors.RsDevice;



import com.kormoran.udp.SendData;
        //czujnik 
public class Label480  implements RsDevice {

    private ModbusReader modbusReader;
    private Rs485 rs485;
    private byte[] frame;
    
    private String oxygen  = "0";
    public static final String THRESHOLD_O2 = "19.0";
    private String oxygenAlarm = "0";
    private String co2 = "0";
    public static final String THRESHOLD_CO2= "0.5";
    private String alarmCo2 = "0";
    private String errorCode = "0";
    private boolean sendData = false;

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getCo2() {
        return co2;
    }
   
    public Label480() {
        super();
        rs485 = new Rs485();
        rs485.setBaudRate(SerialPort.BAUDRATE_19200);
        rs485.setDataBits(SerialPort.DATABITS_8);
        rs485.setParityBit(SerialPort.PARITY_EVEN);
        rs485.setStopBit(SerialPort.STOPBITS_1);
        frame = new byte[]{1, 4, 0, 64, 0, 26};
    }


    public void setRs485(Rs485 rs485) {
        this.rs485 = rs485;
      
    }

    public Rs485 getRs485() {
        return rs485;
    }

    public int getId() {
        return 0;
    }

    
    @Override
    public void setData(byte[] data) {
        if(data !=null&&data.length>36){
            sendData = true;
            int b = (data[5] << 8 &0xFF00) | (data[6] & 0x00ff);
/*             System.out.print("Label480:"+" ");
            for(int i = 0;i<data.length;i++){
                System.out.print(String.format("0x%02X",data[i])+" ");
            }
            System.out.println(); */
            if(b==0 || b == 51712){
                errorCode =String.valueOf(Integer.valueOf(errorCode) | 1);
                oxygen = "0";
            }else{
                errorCode =String.valueOf(Integer.valueOf(errorCode) & 2 );
                String oxygenTmp =  String.valueOf(b); 
                oxygen =  ""+oxygenTmp.charAt(0) +  oxygenTmp.charAt(1) +  "." + oxygenTmp.charAt(2) ;
            }
            int d = (data[37] << 8 &0xFF00) | (data[38] & 0x00ff) ;
            if(d==0|| d == 51712){
                errorCode =String.valueOf(Integer.valueOf(errorCode) | 2);
                co2 = "100.0";
            }else{
                errorCode =String.valueOf(Integer.valueOf(errorCode) & 1);
                co2 = String.valueOf((double)d/10000); 
            }
            oxygenAlarm = Double.valueOf(oxygen) > Double.valueOf(THRESHOLD_O2)?"0":"1";
            alarmCo2= Double.valueOf(co2) > Double.valueOf(THRESHOLD_CO2)?"1":"0";
//            System.out.println("Co2: " + co2);
//            System.out.println("O2: " + oxygen);
            if(Integer.valueOf(errorCode) == 3){
                sendData = false;
            }
        }else{
            sendData = false;
            co2 = null;
            oxygen = null;
        }
    }


    @Override
    public void setId(int id){
    }

    @Override
    public boolean needRequest() { 
        return true;
    }

    @Override
    public void sendFrame(String comPort) throws Exception{
        modbusReader = new ModbusReader();
        modbusReader.sentBytes(frame, comPort, rs485);
    
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
        oxygen = String.valueOf(random.nextInt(30));
        oxygenAlarm = Double.valueOf(oxygen) > Double.valueOf(THRESHOLD_O2)?"0":"1";
        co2= String.valueOf(random.nextInt(100));
        alarmCo2= Double.valueOf(oxygen) > Double.valueOf(THRESHOLD_CO2)?"1":"0";
        errorCode = String.valueOf(random.nextInt(2));
    }

    public void setFrame(byte[] frame) {
        this.frame = frame;
    }

    public byte[] getFrame() {
        return frame;
    }

    public void setOxygenAlarm(String oxygenAlarm) {
        this.oxygenAlarm = oxygenAlarm;
    }

    public String getOxygenAlarm() {
        return oxygenAlarm;
    }

    public void setAlarmCo2(String alarmCo2) {
        this.alarmCo2 = alarmCo2;
    }

    public String getAlarmCo2() {
        return alarmCo2;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getUdpFrame() {
      return new SendData().sendCarac(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {

    }

    public String getUdp2Frame() {
        return null;
    }



    public String setThreshold(String threshold) {
        return null;
    }
}
