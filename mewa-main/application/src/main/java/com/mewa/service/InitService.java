package com.mewa.service;

import com.mewa.device.PressureDevice;

import com.mewa.dto.DeviceDto;
import com.mewa.properties.DeviceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Optional;

@Service
public class InitService {
    @Autowired
    private MoxaService moxaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private DeviceProperties deviceProperties;

    @Async
    public void init() throws Exception{
        //TODO
        //reinstall moxa driver (optional)
        Map<Integer, String> moxaConfigurationMap = moxaService.initMoxa();
        //init moxa port map
        //init devices

        for (Map.Entry<Integer, String> entry : moxaConfigurationMap.entrySet()) {
            Optional<DeviceDto> element = deviceProperties.getDevices().stream().filter(e -> e.getMoxaId().equals(entry.getKey())).findFirst();
            if(!element.isPresent()){
                continue;
            }
            DeviceDto deviceDto = element.get();
            if(deviceDto.getDeviceType().equals("PRESS")){
                deviceService.getPressureDeviceList().add((new PressureDevice(entry.getValue())));
            }

        }


    }


}
