package com.mewa.device;

import com.mewa.enums.SerialEnum;
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
            serialPort.setParams(SerialEnum.DIRECTION.getBaudRate(), SerialEnum.DIRECTION.getDataBits(), SerialEnum.DIRECTION.getStopBits(), SerialEnum.DIRECTION.getParityBits());
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }
}
