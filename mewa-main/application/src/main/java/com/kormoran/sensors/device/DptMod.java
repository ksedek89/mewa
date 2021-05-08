package com.kormoran.sensors.device;


import java.util.Random;

import jssc.SerialPort;

import org.apache.log4j.Logger;

import com.kormoran.reader.protocols.ModbusReader;

import com.kormoran.sensors.Rs485;

import com.kormoran.sensors.RsDevice;


import com.kormoran.udp.Client;
import com.kormoran.udp.SendData;
    // czujnik ciśnienia 
public class DptMod  implements RsDevice {
    private final static Logger logger = Logger.getLogger(DptMod.class);

    //in ms
    private ModbusReader modbusReader;
    private static final byte[] frame = new byte[] { 1, 4, 0, 1, 0, 1 };
    private Rs485 rs485;
    
    private int id;
    private String threshold = "100";    

    private String pressure = "0";
    private String alarm ="0";
    private String errorCode = "0";
    private boolean sendData =  false;
 
    public DptMod() {
        super();
        rs485 = new Rs485();
        rs485.setBaudRate(SerialPort.BAUDRATE_9600);
        rs485.setDataBits(SerialPort.DATABITS_8);
        rs485.setParityBit(SerialPort.PARITY_NONE);
        rs485.setStopBit(SerialPort.STOPBITS_1);    
    }


    @Override
    public void setData(byte[] data) {
/*        if(data != null) {
            errorCode = "0";
            sendData = true;
            int c = (data[3] << 8 & 0xFF00) | (data[4] & 0x00ff);
            if (c > 32768) {
                pressure = String.valueOf(c - 65536);
            } else {
                pressure = String.valueOf(c);
            }
            
            alarm = Integer.valueOf(pressure) > Integer.valueOf(threshold)?"0":"1";
        }else{
            sendData = false;
            pressure = "0";
//            errorCode = "1";
        }*/
    }





    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
    }



    @Override
    public boolean needRequest() {
        return true;
    }

 

    @Override
    public void sendFrame(String comPort) throws Exception {
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
        pressure = String.valueOf(random.nextInt(1000));
        alarm = Integer.valueOf(pressure) > Integer.valueOf(threshold)?"0":"1";
        errorCode = String.valueOf(random.nextInt(2));        
    }
    
    
    public void setId(int id){

    }

    public int getId() {
        return id;
    }


    public void setRs485(Rs485 rs485) {
        this.rs485 = rs485;
    }

    public Rs485 getRs485() {
        return rs485;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getUdpFrame() {
       return new SendData().sendCarps(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {
        if(sendData){
            new Thread(new Client(frame)).start();
        }
    }

    public String getUdp2Frame() {
        return null;
    }


    public String getThreshold() {
        return threshold;
    }

    public String setThreshold() {
        return null;
    }

    public String setThreshold(String threshold) {
        this.threshold = threshold;
        return null;
    }
}
