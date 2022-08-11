package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.service.device.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Data
//klasa która uruchamia scheduler uruchamiający się co 1sekundę (zmienna cron.request-frequency)
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
    private DirectionService directionService;

    @Autowired
    private MoxaHandlerService moxaHandlerService;

    @Autowired
    private VentilationService ventilationService;

    @Autowired
    private DirectionHandlerService directionHandlerService;

    private List<PressureDevice> pressureDeviceList = new ArrayList<>();
    private List<OxygenDevice> oxygenDeviceList = new ArrayList<>();
    private List<DirectionDevice> directionDeviceList = new ArrayList<>();
    private List<MoxaDevice> moxaDeviceList = new ArrayList<>();
    private List<DpoDevice> dpoDeviceList = new ArrayList<>();
    private DpoDevice singleDpoDevice;
    private List <VentilationDevice> ventilationDeviceList;

    //wątek który uruchamia pomiary kolejnych urządzeń
    @Scheduled(cron = "${cron.request-frequency}")
    public void handleDevices() throws Exception {
        //trzeba czekać aż konfiguracja się zakończy
        if(!configurationFinished){
            return;
        }
        //uruchamiaj pomiary wszysktich skonfiguranych czujników ciśnienia asynchornicznie (@Async)
        for(PressureDevice pressureDevice: pressureDeviceList){
            pressureService.handlePressureDevice(pressureDevice, moxaDeviceList);
        }

        //kolejne urządzenia analogicznie
        for(OxygenDevice oxygenDevice: oxygenDeviceList){
            oxygenService.handlOxygenDevice(oxygenDevice);
        }


        dpoService.handleDpoDevice(dpoDeviceList, moxaDeviceList);
        dpoService.handleDpoDevice(Arrays.asList(singleDpoDevice), moxaDeviceList);

        for(DirectionDevice a: directionDeviceList){
            directionService.handleDirectionDevice(a);
        }

        directionHandlerService.handleDirectionDevice(directionDeviceList, dpoDeviceList, singleDpoDevice, ventilationDeviceList);

        moxaHandlerService.handleMoxaDevice(moxaDeviceList);

        ventilationService.handleSiuAsync(moxaDeviceList);

    }

    public void setConfigurationFinished(boolean configurationFinished) {
        this.configurationFinished = configurationFinished;
    }

    public void setSymulation(boolean symulation) {
        this.symulation = symulation;
    }
}
