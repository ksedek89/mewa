package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.device.OxygenDevice;
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

    @Async
    public void handleDirectionDevice(List<DirectionDevice> directionDeviceList){
        try {
            if(directionDeviceList.size() == 0){
                return;
            }
            List<Future<DirectionDevice>> futureList = new ArrayList<>();
            for(DirectionDevice directionDevice :directionDeviceList){
                futureList.add(directionService.handleDirectionDevice(directionDevice));
            }
            if(directionDeviceList.stream().anyMatch(e->e.getType().equals(TypeE.SYM))){
                directionDeviceList.stream().forEach(e->prepareSymData(e));
                directionService.handleSiuFrame(directionDeviceList);
                return;
            }
            for(Future future: futureList){
                future.get();
            }
            directionService.handleSiuFrame(directionDeviceList);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void prepareSymData(DirectionDevice directionDevice) {
        directionDevice.setRadAlarm(directionService.isAlarm(directionDevice));
        directionDevice.setErrorCode("0");
    }


}
