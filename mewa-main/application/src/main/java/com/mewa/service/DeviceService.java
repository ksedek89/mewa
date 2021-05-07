package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.PressureDevice;
import com.mewa.service.device.DirectionHandlerService;
import com.mewa.service.device.PressureService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Data
public class DeviceService {
    private boolean configurationFinished;

    @Autowired
    private PressureService pressureService;

    @Autowired
    private DirectionHandlerService directionHandlerService;

    private List<PressureDevice> pressureDeviceList = new ArrayList<>();
    private List<DirectionDevice> directionDeviceList = new ArrayList<>();

    @Scheduled(cron = "${cron.request-frequency}")
    public void handleDevices() throws Exception {
        if(!configurationFinished){
            return;
        }
        for(PressureDevice pressureDevice: pressureDeviceList){
            pressureService.handlePressureDevice(pressureDevice);
        }
        directionHandlerService.handleDirectionDevice(directionDeviceList);
    }

    public void setConfigurationFinished(boolean configurationFinished) {
        this.configurationFinished = configurationFinished;
    }
}
