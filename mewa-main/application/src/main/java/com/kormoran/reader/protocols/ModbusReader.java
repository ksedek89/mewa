package com.kormoran.reader.protocols;

import java.nio.ByteBuffer;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.apache.log4j.Logger;


import com.kormoran.sensors.Rs485;


public class ModbusReader {
    private final static Logger logger = Logger.getLogger(ModbusReader.class);

    private SerialPort serialPort;
    
    public ModbusReader() {
        super();
    }

    public void sentBytes(byte[] frame, String portName, Rs485 rs485) {
        try{
            serialPort = new SerialPort(portName);
            serialPort.openPort();
            serialPort.setParams(rs485.getBaudRate(), rs485.getDataBits(), rs485.getStopBit(), rs485.getParityBit());
            byte[] crc = Rs485.ModRTU_CRC(frame, frame.length);
            ByteBuffer bb = ByteBuffer.allocate(frame.length + crc.length);
            bb.put(frame);
            bb.put(crc);
            byte[] sendingBytes = bb.array();
            serialPort.writeBytes(sendingBytes);
            Thread.sleep(500);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    


    public void sentBytes(byte[] frame) throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(frame.length);
        bb.put(frame);
        byte[] sendingBytes = bb.array();
        serialPort.writeBytes(sendingBytes);
    }



    public void sentBytesWithoutCheckSum(byte[] frame, String portName, Rs485 rs485) throws Exception {
        serialPort = new SerialPort(portName);
        serialPort.openPort();
        serialPort.setParams(rs485.getBaudRate(), rs485.getDataBits(), rs485.getStopBit(), rs485.getParityBit());
        ByteBuffer bb = ByteBuffer.allocate(frame.length);
        bb.put(frame);
        byte[] sendingBytes = bb.array();
        serialPort.writeBytes(sendingBytes);
        Thread.sleep(1000);        
    }

    public byte[]getBytes(){
        byte[] receivedBytes = null;
        try {
        receivedBytes = serialPort.readBytes();  
//        System.out.println("ReceivedBytes: " + receivedBytes.length);
         serialPort.closePort();
        } catch (SerialPortException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
            return receivedBytes;
        }
        return receivedBytes;
    }
    public byte[]getBytesWithoutClosing(){
        byte[] receivedBytes = null;
        try {
            receivedBytes = serialPort.readBytes();  
        } catch (SerialPortException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
            return receivedBytes;
        }
        return receivedBytes;
    }   
    public void closePort(){
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            logger.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
