package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.PressureDevice;

import com.mewa.device.VentilationDevice;
import com.mewa.enums.TypeE;
import com.mewa.model.entity.Device;
import com.mewa.model.repository.DeviceRepository;
import com.mewa.model.repository.ThresholdValueRepository;
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
@Slf4j
public class InitService {

    @Autowired
    private MoxaService moxaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VentilationService ventilationService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MoxaProperties moxaProperties;

    @Autowired
    ThresholdValueRepository thresholdValueRepository;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Async
    public void init() throws Exception {

        thresholdValuesService.init();
        List<Device> activeDevices = deviceRepository.findAllByActiveEquals("T");

        //SIMULATION
        if(activeDevices.stream().anyMatch(e->e.getType().equals(TypeE.SYM))){
            deviceService.setSymulation(true);
            for(Device deviceDto: activeDevices) {
                if (deviceDto.getDeviceType().equals("PRESS")) {
                    deviceService.getPressureDeviceList()
                        .add((new PressureDevice(null, deviceDto.getDeviceId(), deviceDto.getThresholdPressure(), deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("OXY")) {
                    deviceService.getOxygenDeviceList().add((new OxygenDevice(null, deviceDto.getDeviceId(), deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("DIR")) {
                    deviceService.getDirectionDeviceList()
                        .add((new DirectionDevice(null, deviceDto.getDeviceId(), deviceDto.getDirectionAngle(), deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("DPO")) {
                    if (deviceDto.getDeviceId() == 3) {
                        deviceService.setSingleDpoDevice(new DpoDevice(null, deviceDto.getDeviceId(), deviceDto.getType()));
                    } else {
                        deviceService.getDpoDeviceList().add(new DpoDevice(null, deviceDto.getDeviceId(), deviceDto.getType()));
                    }
                }
            }
            List<MoxaDevice> moxaDevices = moxaProperties.getConfiguration().stream().map(e -> MoxaDevice.builder().status("A").type(TypeE.SYM).id(e.getId()).ip(e.getIp()).build()).collect(Collectors.toList());
            deviceService.setMoxaDeviceList(moxaDevices);
            deviceService.setConfigurationFinished(true);
            return;
        }

        //REAL DEVICES
        Map<Integer, String> moxaConfigurationMap = moxaService.initMoxa();
        for (Device device: activeDevices){
            log.info("Device from properties: " + device.toString());
            if(device.getMoxaId() == null || device.getMoxaNumber() == null || device.getDeviceType() == null){
                throw new Exception("Bledna konfiguracja");
            }
        }
        for (Map.Entry<Integer, String> entry : moxaConfigurationMap.entrySet()) {
            List<Device> deviceList = activeDevices
                .stream()
                .filter(e -> entry.getKey().equals(e.getMoxaId() + 16 * (e.getMoxaNumber() - 1))).collect(Collectors.toList());
            if(deviceList.size() == 0){
                continue;
            }
            for(Device deviceDto: deviceList) {
                log.info("For element: " + entry.getKey() + ", port: " + entry.getValue() + ",  Found device:" + deviceDto.toString());
                if (deviceDto.getDeviceType().equals("PRESS")) {
                    deviceService.getPressureDeviceList()
                        .add((new PressureDevice(entry.getValue(), entry.getKey(), deviceDto.getThresholdPressure(), deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("DIR")) {
                    deviceService.getDirectionDeviceList()
                        .add((new DirectionDevice(entry.getValue(), entry.getKey(), deviceDto.getDirectionAngle(),deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("VENT")) {
                    ventilationService.setVentilationDevice(new VentilationDevice(entry.getValue()));
                }
                if (deviceDto.getDeviceType().equals("OXY")) {
                    deviceService.getOxygenDeviceList().add((new OxygenDevice(entry.getValue(), entry.getKey(), deviceDto.getType())));
                }
                if (deviceDto.getDeviceType().equals("DPO")) {
                    if (deviceDto.getDeviceId() == 3) {
                        deviceService.setSingleDpoDevice(new DpoDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getType()));
                    } else {
                        deviceService.getDpoDeviceList().add(new DpoDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getType()));
                    }
                }
            }
        }
        List<MoxaDevice> moxaDevices = moxaProperties.getConfiguration().stream().map(e -> MoxaDevice.builder().type(TypeE.REAL).id(e.getId()).ip(e.getIp()).build()).collect(Collectors.toList());
        deviceService.setMoxaDeviceList(moxaDevices);
        deviceService.setConfigurationFinished(true);
    }

}
