package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.VentilationDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DirectionHandlerService{

    @Autowired
    DirectionService directionService;

    @Autowired
    VentilationService ventilationService;

    private final static Long thresholdInNano = 25000l;

    @Async
    public void handleDirectionDevice(List<DirectionDevice> directionDeviceList, List<DpoDevice> dpoDeviceList, DpoDevice singleDpoDevice, VentilationDevice ventilationDevice){
        if((directionDeviceList.size() == 0 && dpoDeviceList.size() == 0 && singleDpoDevice == null) || ventilationDevice == null){
            return;
        }
        try {
            if(directionDeviceList.stream().anyMatch(e->e.getTotalDosage() > thresholdInNano)
            || dpoDeviceList.stream().filter(e->e.getPower() != null).anyMatch(e->Integer.valueOf(e.getPower()) > thresholdInNano)
            || (singleDpoDevice != null && Integer.valueOf(singleDpoDevice.getPower()) > thresholdInNano)){
                log.info("Turn on eight");
                ventilationDevice.setTurnOnEight(true);
            }else{
                log.info("Turn off eight");
                ventilationDevice.setTurnOnEight(false);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


}
