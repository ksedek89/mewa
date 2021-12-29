package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DirectionDevice implements Device{

    private int id;
    private int directionAngle;
    private long totalDosage;
    private String totalDosagePrefix;
    private int radAlarm;
    private long neutrons;
    private long initNeutrons;
    private long errorCode;

    private int moxaId;
    private TypeE type;
    private SerialPort serialPort;

    private int symCounter;


    public DirectionDevice(String portName, int id, int moxaId, Integer directionAngle, TypeE typeE) {
        this.type = typeE;
        this.moxaId = moxaId;
        this.id = id;
        this.directionAngle = directionAngle;
        this.totalDosagePrefix = "N";
        this.serialPort = new SerialPort(portName);
    }
}
