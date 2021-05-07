package com.kormoran.sensors;

import com.kormoran.filterventilation.Ventilation;

import java.util.List;

import com.kormoran.sensors.device.CarfsMessage;
import com.kormoran.sensors.device.RadSensor;
import org.apache.log4j.Logger;

import com.kormoran.udp.Client;
import com.kormoran.udp.SendData;

public class Filter implements Runnable{
    private String sentence;
    private List<Device> deviceList;
    final static Logger logger = Logger.getLogger(Filter.class);

    public Filter(List<Device> deviceList,String sentence) {
        super();
        this.sentence = sentence;
        this.deviceList = deviceList;

    }
    
    

    public void run() {
/*        logger.warn("Otrzymano: " + sentence);
        String[] values = sentence.split(",");
        String mode = values[2];
        String id = values[3];
        String state = values[4].substring(0, 1);
        
        new Thread(new Client(new SendData().sendCarse(mode,id,state))).start();

        if(mode.equalsIgnoreCase("ASS")){
            RadSensor device = null;
            for(Device a: deviceList){
                if(a.getRsDevice() instanceof RadSensor && String.valueOf(a.getRsDevice().getId()).equalsIgnoreCase(id)){
                    device = (RadSensor)a.getRsDevice();
                    if(state.equalsIgnoreCase("1")){
                        device.setTurnOn(true);
                    }else if (state.equalsIgnoreCase("0")){
                        device.setTurnOff(true);
                    }
                    break;
                }
            }
            //wysłanie ramki
            
        }else if(state.equalsIgnoreCase("0")||mode.equalsIgnoreCase("F")||mode.equalsIgnoreCase("W")){
            //włącznie filtrowentylacji
            CarfsMessage.setChangingState(true);
            CarfsMessage.setChangingStateCtn(0);
            String value = null;
            if(state.equalsIgnoreCase("0")){
                logger.info("wylaczenie filtrowentlacji i wentylacji");
                value = "off";
            }else if(mode.equalsIgnoreCase("F")){
                //włącznie filtrowentylacji
                logger.info("wlaczenie filtrowentlacji");          
                value = "filters";
            }else if(mode.equalsIgnoreCase("W")){
                //włącznie filtrowentylacji
                logger.info("wlaczenie wentlacji");
                value = "ventilation";
            }
            try{
//                for(int i = 0 ;i<2;i++){
                    new Thread(new Ventilation(value)).start();
//                    Thread.sleep(2000);
//                }
            }catch(Exception e){
                logger.error(e.getMessage(), e);
            }
        }*/

    }
}
