package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.PressureDevice;

import com.mewa.device.VentilationDevice;
import com.mewa.dto.DeviceDto;
import com.mewa.model.repository.ThresholdValueRepository;
import com.mewa.properties.DeviceProperties;
import com.mewa.properties.MoxaProperties;
import com.mewa.service.device.VentilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class InitService {

    @Autowired
    private MoxaService moxaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VentilationService ventilationService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private DeviceProperties deviceProperties;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MoxaProperties moxaProperties;

    @Autowired
    ThresholdValueRepository thresholdValueRepository;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Async
    public void init() throws Exception {
        thresholdValuesService.init();
        Map<Integer, String> moxaConfigurationMap = moxaService.initMoxa();
        for (DeviceDto deviceDto: deviceProperties.getDevices()){
            if(deviceDto.getMoxaId() == null || deviceDto.getMoxaNumber() == null || deviceDto.getDeviceType() == null){
                throw new Exception("Bledna konfiguracja");
            }
        }
        for (Map.Entry<Integer, String> entry : moxaConfigurationMap.entrySet()) {
            Optional<DeviceDto> element = deviceProperties.getDevices()
                .stream()
                .filter(e -> entry.getKey().equals(e.getMoxaId()+16*(e.getMoxaNumber()-1)))
                .findFirst();
            if(!element.isPresent()){
                continue;
            }
            DeviceDto deviceDto = element.get();
            if(deviceDto.getDeviceType().equals("PRESS")){
                deviceService.getPressureDeviceList()
                    .add((new PressureDevice(entry.getValue(), deviceDto.getDeviceId() != null ? deviceDto.getDeviceId(): entry.getKey(), deviceDto.getThresholdPressure())));
            }
            if(deviceDto.getDeviceType().equals("DIR")){
                deviceService.getDirectionDeviceList()
                    .add((new DirectionDevice(entry.getValue(), deviceDto.getDeviceId() != null ? deviceDto.getDeviceId(): entry.getKey(), deviceDto.getDirectionAngle())));
            }
            if(deviceDto.getDeviceType().equals("VENT")){
                ventilationService.setVentilationDevice(new VentilationDevice(entry.getValue()));
            }
        }
        List<MoxaDevice> moxaDevices = moxaProperties.getConfiguration().stream().map(e -> MoxaDevice.builder().id(e.getId()).ip(e.getIp()).build()).collect(Collectors.toList());
        deviceService.setMoxaDeviceList(moxaDevices);
        deviceService.setConfigurationFinished(true);
    }

}
