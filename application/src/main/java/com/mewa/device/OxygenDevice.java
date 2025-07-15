package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
//czujnik tlenu
public class OxygenDevice implements Device{
    private int id;
    private double oxygen;
    private double oxygenThreshold = 19.0;
    private String oxygenAlarm;
    private double co2;
    private double co2Threshold = 0.5;
    private String co2Alarm;
    private String errorCode;

    private TypeE type;


    public OxygenDevice(int id, TypeE type) {
        this.id = id;
        this.type = type;
    }

}
