package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.PressureDevice;

import com.mewa.dto.DeviceDto;
import com.mewa.model.entity.ThresholdValue;
import com.mewa.model.repository.ThresholdValueRepository;
import com.mewa.properties.DeviceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.List;
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

    @Autowired
    ThresholdValueRepository thresholdValueRepository;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Async
    public void init() throws Exception{
        thresholdValuesService.init();
        Map<Integer, String> moxaConfigurationMap = moxaService.initMoxa();
        for (Map.Entry<Integer, String> entry : moxaConfigurationMap.entrySet()) {
            Optional<DeviceDto> element = deviceProperties.getDevices().stream().filter(e -> e.getMoxaId().equals(entry.getKey())).findFirst();
            if(!element.isPresent()){
                continue;
            }
            DeviceDto deviceDto = element.get();
            if(deviceDto.getDeviceType().equals("PRESS")){
                deviceService.getPressureDeviceList().add((new PressureDevice(entry.getValue(), entry.getKey(), deviceDto.getThresholdPressure())));
            }
            if(deviceDto.getDeviceType().equals("DIR")){
                deviceService.getDirectionDeviceList().add((new DirectionDevice(entry.getValue(), entry.getKey(), deviceDto.getDirectionAngle())));
            }
        }
        deviceService.setConfigurationFinished(true);
    }

}
