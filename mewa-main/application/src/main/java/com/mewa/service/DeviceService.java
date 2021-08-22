package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.PressureDevice;
import com.mewa.service.device.DirectionHandlerService;
import com.mewa.service.device.DpoService;
import com.mewa.service.device.MoxaHandlerService;
import com.mewa.service.device.OxygenService;
import com.mewa.service.device.PressureService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Data
public class DeviceService {
    private boolean symulation;

    private boolean configurationFinished;

    @Autowired
    private PressureService pressureService;

    @Autowired
    private OxygenService oxygenService;

    @Autowired
    private DpoService dpoService;

    @Autowired
    private DirectionHandlerService directionHandlerService;

    @Autowired
    private MoxaHandlerService moxaHandlerService;


    private List<PressureDevice> pressureDeviceList = new ArrayList<>();
    private List<OxygenDevice> oxygenDeviceList = new ArrayList<>();
    private List<DirectionDevice> directionDeviceList = new ArrayList<>();
    private List<MoxaDevice> moxaDeviceList = new ArrayList<>();
    private List<DpoDevice> dpoDeviceList = new ArrayList<>();
    private DpoDevice singleDpoDevice;

    @Scheduled(cron = "${cron.request-frequency}")
    public void handleDevices() throws Exception {
        if(!configurationFinished){
            return;
        }
        for(PressureDevice pressureDevice: pressureDeviceList){
            pressureService.handlePressureDevice(pressureDevice);
        }

        for(OxygenDevice oxygenDevice: oxygenDeviceList){
            oxygenService.handlOxygenDevice(oxygenDevice);
        }


        dpoService.handleDpoDevice(dpoDeviceList);
        dpoService.handleDpoDevice(Arrays.asList(singleDpoDevice));

        directionHandlerService.handleDirectionDevice(directionDeviceList);
        moxaHandlerService.handleMoxaDevice(moxaDeviceList);

    }

    public void setConfigurationFinished(boolean configurationFinished) {
        this.configurationFinished = configurationFinished;
    }

    public void setSymulation(boolean symulation) {
        this.symulation = symulation;
    }
}
