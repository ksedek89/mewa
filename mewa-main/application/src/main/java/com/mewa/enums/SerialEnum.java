package com.mewa.enums;

import jssc.SerialPort;

public enum SerialEnum {
    DIRECTION(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE),
    VENTILATION(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE),
    PRESSURE(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

    SerialEnum(int baudRate, int dataBits, int stopBits , int parityBits) {
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parityBits = parityBits;
    }

    private int baudRate;
    private int dataBits;
    private int parityBits;
    private int stopBits;

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getParityBits() {
        return parityBits;
    }

    public int getStopBits() {
        return stopBits;
    }
}
