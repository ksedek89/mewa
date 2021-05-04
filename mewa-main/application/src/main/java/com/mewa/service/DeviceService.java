package com.mewa.service;

import com.mewa.device.PressureDevice;
import com.mewa.service.device.PressureService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Data
public class DeviceService {

    @Autowired
    private PressureService pressureService;

    private List<PressureDevice> pressureDeviceList = new ArrayList<>();

    @Scheduled(fixedDelay = 1000)
    public void handleDevices(){
        log.info("handle");
        for(PressureDevice pressureDevice: pressureDeviceList){
            pressureService.handlePressureDevice(pressureDevice);
        }
    }
}
