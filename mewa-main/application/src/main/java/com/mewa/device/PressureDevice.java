package com.mewa.device;

import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PressureDevice {
    private int id;
    private int pressure;
    private int alarm;
    private int errorCode;
    private int threshold;

    private boolean readError;

    private SerialPort serialPort;

    public PressureDevice(String portName) {
        this.serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.PARITY_NONE, SerialPort.STOPBITS_1);
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }
}
