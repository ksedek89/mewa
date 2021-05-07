package com.mewa.device;

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


    private SerialPort serialPort;

    public PressureDevice(String portName, int id, Integer threshold) {
        this.id = id;
        this.threshold = threshold;
        this.serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
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
