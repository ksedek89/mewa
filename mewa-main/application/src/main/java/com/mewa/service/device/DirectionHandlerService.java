package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class DirectionHandlerService{

    @Autowired
    DirectionService directionService;

    @Autowired
    VentilationService ventilationService;

    private final static Long thresholdInNano = 25000l;

    @Async
    public void handleDirectionDevice(List<DirectionDevice> directionDeviceList, VentilationDevice ventilationDevice){
        if(directionDeviceList == null || directionDeviceList.size() == 0 || ventilationDevice == null){
            return;
        }
        try {
            if(directionDeviceList.stream().anyMatch(e->e.getTotalDosage() > thresholdInNano)){
                ventilationService.turnOnEightEngine();
            }else{
                ventilationService.turnOffEightEngine();
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }


}
