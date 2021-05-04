package com.mewa.device;

import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DirectionDevice implements Device{

    private int id;
    private int directionAngle;
    private double totalDosage;
    private String totalDosagePrefix;
    private int radAlarm;
    private int neutrons;
    private int initNeutrons;
    private String errorCode;

    private SerialPort serialPort;

    public DirectionDevice(String portName, int id, Integer directionAngle) {
        this.id = id;
        this.directionAngle = directionAngle;
        this.totalDosagePrefix = "N";
        this.serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.PARITY_NONE, SerialPort.STOPBITS_1);
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }
}
