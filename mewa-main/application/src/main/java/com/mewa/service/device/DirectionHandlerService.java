package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
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
            for(Future future: futureList){
                future.get();
            }
            directionService.handleSiuFrame(directionDeviceList);
            System.out.println();
        }catch (Exception e){

        }
    }

}
