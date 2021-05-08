package com.kormoran.sensors;


public class Device {

    //DptMod/Label480/
    private RsDevice rsDevice;
    //1-32
    private int portNumber;
    //tty...
    private String portName;

    
    
    public Device() {
        super();
    }
    


    public void setRsDevice(RsDevice rsDevice) {
        this.rsDevice = rsDevice;
    }

    public RsDevice getRsDevice() {
        return rsDevice;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortName() {
        return portName;
    }




}
