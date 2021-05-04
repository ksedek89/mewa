package com.kormoran.sensors;

import com.kormoran.sensors.propertiesenum.DeviceType;
import com.kormoran.sensors.propertiesenum.PortType;

public class Device {

    //sym/real
    private PortType type;
    //DptMod/Label480/
    private RsDevice rsDevice;
    //1-32
    private int portNumber;
    //tty...
    private String portName;
    
    private DeviceType deviceType;
    
    
    public Device() {
        super();
    }
    

    public void setType(PortType type) {
        this.type = type;
    }

    public PortType getType() {
        return type;
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

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }


}
