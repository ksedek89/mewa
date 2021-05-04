package com.kormoran.timer;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kormoran.sensors.Device;

import com.kormoran.sensors.propertiesenum.PortType;


public class ReadDataTimer extends TimerTask {
    private final static Logger logger = Logger.getLogger(ReadDataTimer.class);

    private Device device;
    
    public ReadDataTimer(Device device) {
        super();
        this.device = device;
    }

    public void run() {
        try{
        if (device.getType().equals(PortType.REAL)) {

           if (device.getRsDevice().needRequest()) {
                device.getRsDevice().sendFrame(device.getPortName());

           }
            byte data[] = device.getRsDevice().getFrame(device.getPortName());//

            device.getRsDevice().setData(data);
            
        } else {
            device.getRsDevice().generateSimulationData();
            device.getRsDevice().setSimulateData();
        }

        String udpFrame = device.getRsDevice().getUdpFrame();

        String udp2Frame = device.getRsDevice().getUdp2Frame();
        device.getRsDevice().sendDataOverUdp(udpFrame);
       if(udp2Frame!=null){
 
           device.getRsDevice().sendDataOverUdp(udp2Frame);            
        }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        /*
        1. sprawdzic czy symulacja, jesli tak punkt 7
        2a. sprawdzic czy wymagane jest zapytanie
        3a. zapytac (opcjonalnie)
        4a. pobrac wiadomosc
        5a. ustawic parametry
        6a. wyslac wiadomosc - koniec
        2b. przeprowadzic symulacje testujaca
        2c. wyslac wiadomosc - koniec
        */
    }

}
