package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PressureDevice implements Device{
    private int id;
    private int pressure;
    private int alarm;
    private int errorCode;
    private int threshold;
    private TypeE type;

    private int moxaId;
    private SerialPort serialPort;

    public PressureDevice(String portName, int id, int moxaId, Integer threshold, TypeE type) {
        this.id = id;
        this.threshold = threshold;
        this.serialPort = new SerialPort(portName);
        this.type = type;
        this.moxaId = moxaId;
    }

    @Override
    public String toString() {
        return String.format("PressureDevice{%-5s%-13s%-15s%-10s%-13s%-16s}",
            "id=" + id,
            ", moxa-id=" + (id/16+1) + "-" + id%16,
            ", pressure=" + pressure,
            ", alarm=" + alarm ,
            ", errorCode=" + errorCode,
            ", threshold=" + threshold

        );
    }
}
