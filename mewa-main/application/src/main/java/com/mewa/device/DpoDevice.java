package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DpoDevice implements Device{
    private int id;
    private String dosage;
    private String power;
    private int alarm;
    private int errorCode;
    private TypeE type;


    private SerialPort serialPort;

    public DpoDevice(String portName, int id, TypeE typeE) {
        this.type = typeE;
        this.id = id;
        this.serialPort = new SerialPort(portName);
        if(type.equals(TypeE.SYM)){
            return;
        }
        try {
            serialPort.openPort();
            serialPort.setParams(SerialEnum.DPO.getBaudRate(), SerialEnum.DPO.getDataBits(), SerialEnum.DPO.getStopBits(), SerialEnum.PRESSURE.getParityBits());
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "DpoDevice{" +
            "id=" + id +
            ", dosage='" + dosage + '\'' +
            ", power='" + power + '\'' +
            ", alarm=" + alarm +
            ", errorCode=" + errorCode +
            '}';
    }
}
