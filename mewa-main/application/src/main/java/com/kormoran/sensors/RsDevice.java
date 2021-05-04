package com.kormoran.sensors;

import com.kormoran.exception.IllegalConfigurationException;
import com.kormoran.exception.WritingIdToNonIdDeviceException;



public interface RsDevice {    
    public void setId(int id) throws WritingIdToNonIdDeviceException, IllegalConfigurationException;   
    public int getId();
    public boolean needRequest();
    public void sendFrame(String comPort) throws Exception;
    public byte[] getFrame(String comPort);
    public void setSimulateData();
    public void generateSimulationData();
    public void setData(byte[] data);
    public String getUdpFrame();
    public String getUdp2Frame();
    public void sendDataOverUdp(String frame);
    public String setThreshold(String threshold);
  
}
