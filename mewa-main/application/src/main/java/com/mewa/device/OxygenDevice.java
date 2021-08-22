package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class OxygenDevice implements Device{
    private int id;
    private double oxygen;
    private double oxygenThreshold;
    private String oxygenAlarm;
    private double co2;
    private double co2Threshold;
    private String co2Alarm;
    private String errorCode;

    private TypeE type;

    private SerialPort serialPort;

    public OxygenDevice(String portName, int id, TypeE type) {
        this.id = id;
        this.serialPort = new SerialPort(portName);
        this.type = type;
        if(type.equals(TypeE.SYM)){
            return;
        }
        try {
            serialPort.openPort();
            serialPort.setParams(SerialEnum.PRESSURE.getBaudRate(), SerialEnum.PRESSURE.getDataBits(), SerialEnum.PRESSURE.getStopBits(), SerialEnum.PRESSURE.getParityBits());
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }

}
