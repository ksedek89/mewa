package com.kormoran.sensors;

public class Rs485 {

    private int baudRate;
    private int dataBits;
    private int stopBit;
    private int parityBit;

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setStopBit(int stopBit) {
        this.stopBit = stopBit;
    }

    public int getStopBit() {
        return stopBit;
    }

    public void setParityBit(int parityBit) {
        this.parityBit = parityBit;
    }

    public int getParityBit() {
        return parityBit;
    }

    public Rs485() {
        super();
    }



}
