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
    private double totalDosage;
    private String totalDosagePrefix;
    private int radAlarm;
    private int neutrons;
    private int initNeutrons;
    private String errorCode;

    private int moxaId;
    private TypeE type;
    private SerialPort serialPort;


    public DirectionDevice(String portName, int id, int moxaId, Integer directionAngle, TypeE typeE) {
        this.type = typeE;
        this.moxaId = moxaId;
        this.id = id;
        this.directionAngle = directionAngle;
        this.totalDosagePrefix = "N";
        this.serialPort = new SerialPort(portName);
    }
}
