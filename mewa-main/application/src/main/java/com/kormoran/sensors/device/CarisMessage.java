package com.kormoran.sensors.device;


import java.util.Timer;

import com.kormoran.sensors.response.MoxaResponseTimer;
import com.kormoran.sensors.RsDevice;


import com.kormoran.udp.Client;
import com.kormoran.udp.SendData;
        // integrator status
public class CarisMessage implements RsDevice {
    
    public static String MOXA_IP;
    private String status;
    private String errorCode;


    public CarisMessage() {
        super();        
        status = "F";
        errorCode = "0";
        Timer timer= new Timer();
        MoxaResponseTimer moxaResponseTimer= new MoxaResponseTimer(this);
        timer.scheduleAtFixedRate(moxaResponseTimer,0, 1000);
    }

    @Override
    public void setId(int id) {
    }

    @Override
    public boolean needRequest() {     
        return false;
    }

    @Override
    public void sendFrame(String comPort) {   
    }

    @Override
    public byte[] getFrame(String comPort) {   
        return new byte[0];
    }

    @Override
    public void setSimulateData() {
    }

    @Override
    public void generateSimulationData() {
    }

    @Override
    public void setData(byte[] data) {
    }

    @Override
    public String getUdpFrame() {
        return new SendData().sendCaris(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {
        new Thread(new Client(frame)).start();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUdp2Frame() {
        return null;
    }



    public String setThreshold(String threshold) {
        return null;
    }

    public int getId() {
        return 0;
    }
}
