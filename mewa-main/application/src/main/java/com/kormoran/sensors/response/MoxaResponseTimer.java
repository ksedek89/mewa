package com.kormoran.sensors.response;

import java.net.InetAddress;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kormoran.sensors.device.CarisMessage;

public class MoxaResponseTimer extends TimerTask  {
    final static Logger logger = Logger.getLogger(MoxaResponseTimer.class);

    CarisMessage carisMessage;
    public MoxaResponseTimer(CarisMessage carisMessage) {
        super();
        this.carisMessage = carisMessage;
    }

    @Override
    public void run() {
        try{
            if(InetAddress.getByName(CarisMessage.MOXA_IP).isReachable(3000)){
                carisMessage.setStatus("A");
            }else{
                carisMessage.setStatus("F");
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
    }
}
