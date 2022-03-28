package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.VentilationDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DpoHandlerService {

    @Autowired
    DpoService directionService;

    @Autowired
    VentilationService ventilationService;

    private final static Long thresholdInNano = 25000l;

    @Async
    public void handleDirectionDevice(List<DpoDevice> dpoDeviceList, DpoDevice dpoDevice,  VentilationDevice ventilationDevice){
        if(dpoDeviceList == null || dpoDeviceList.size() == 0 || ventilationDevice == null){
            return;
        }
        List<DpoDevice> tempDpoDeviceList = dpoDeviceList.stream().collect(Collectors.toList());
        tempDpoDeviceList.add(dpoDevice);
        try {
            if(tempDpoDeviceList.stream().filter(e->e.getPower() != null).anyMatch(e->Integer.valueOf(e.getPower()) > thresholdInNano)){
                ventilationService.turnOnEightEngine();
            }else{
                ventilationService.turnOffEightEngine();
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


}
