package com.mewa.controller.test;

import com.mewa.device.Device;
import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.service.DeviceService;
import com.mewa.service.device.PressureService;
import com.mewa.service.device.VentilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.keeit.DirDto;
import pl.keeit.FilterDto;
import pl.keeit.RequestDto;

import java.util.Optional;

import static com.mewa.util.Utils.getFactorCompareToNano;

@RestController(value = "/pressure")
public class TestController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    VentilationService ventilationService;

    @PostMapping
    public void setDevicesData(@RequestBody  RequestDto requestDto){
        requestDto.getPressureDevices()
            .stream()
            .forEach(e-> {
                Optional<PressureDevice> pressureDeviceOptional = deviceService.getPressureDeviceList().stream().filter(f -> f.getId() == e.getId()).findFirst();
                if(pressureDeviceOptional.isPresent()){
                    PressureDevice pressureDevice = pressureDeviceOptional.get();
                    pressureDevice.setPressure(e.getPressure());
                    pressureDevice.setThreshold(e.getThreshold());

                }
            });
        requestDto.getOxygenDevices()
            .stream()
            .forEach(e-> {
                Optional<OxygenDevice> oxygenDeviceOptional = deviceService.getOxygenDeviceList().stream().filter(f -> f.getId() == e.getId()).findFirst();
                if(oxygenDeviceOptional.isPresent()){
                    OxygenDevice oxygenDevice = oxygenDeviceOptional.get();
                    oxygenDevice.setOxygen(Double.valueOf(e.getOxygen()));
                    oxygenDevice.setOxygenThreshold(Double.valueOf(e.getOxygenThreshold()));
                    oxygenDevice.setCo2(Double.valueOf(e.getCo2()));
                    oxygenDevice.setCo2Threshold(Double.valueOf(e.getCo2Threshold()));
                }
            });

        requestDto.getDpoDevices()
            .stream()
            .forEach(e-> {
                Optional<DpoDevice> dpoDeviceOptional = deviceService.getDpoDeviceList().stream().filter(f -> f.getId() == e.getId()).findFirst();
                if(dpoDeviceOptional.isPresent()){
                    DpoDevice dpoDevice = dpoDeviceOptional.get();
                    dpoDevice.setPower(String.valueOf(Double.valueOf(e.getDpoPower())* getFactorCompareToNano(e.getDpoPowerUnit())));
                    dpoDevice.setDosage(String.valueOf(Double.valueOf(e.getDpoTotalDosage())* getFactorCompareToNano(e.getDpoTotalDosageUnit())));
                }else if(deviceService.getSingleDpoDevice().getId() == e.getId()){
                    DpoDevice dpoDevice = deviceService.getSingleDpoDevice();
                    dpoDevice.setPower(String.valueOf(Double.valueOf(e.getDpoPower())* getFactorCompareToNano(e.getDpoPowerUnit())));
                    dpoDevice.setDosage(String.valueOf(Double.valueOf(e.getDpoTotalDosage())* getFactorCompareToNano(e.getDpoTotalDosageUnit())));
                }
            });

        requestDto.getDirDevices()
            .stream()
            .forEach(e-> {
                Optional<DirectionDevice> dirDtoOptional = deviceService.getDirectionDeviceList().stream().filter(f -> f.getId() == e.getId()).findFirst();
                if(dirDtoOptional.isPresent()){
                    DirectionDevice directionDevice = dirDtoOptional.get();
                    directionDevice.setNeutrons(Integer.valueOf(e.getDirNeutrons()));
                    directionDevice.setInitNeutrons(Integer.valueOf(e.getDirInitNeutrons()));
                    directionDevice.setTotalDosage(Integer.valueOf(e.getDirPower()));
                    directionDevice.setTotalDosagePrefix(e.getDirPowerUnit());

                }
            });
        requestDto.getMoxaDevices()
            .stream()
            .forEach(e-> {
                Optional<MoxaDevice> moxaDeviceOptional = deviceService.getMoxaDeviceList().stream().filter(f -> f.getId() == e.getId()).findFirst();
                if(moxaDeviceOptional.isPresent()){
                    MoxaDevice moxaDevice = moxaDeviceOptional.get();
                    moxaDevice.setStatus(e.isEnabled() ? "A" : "F");
                }
            });
        requestDto.getFilterDevices()
            .stream()
            .forEach(e-> {
                for (VentilationDevice ventilationDevice: ventilationService.getVentDeviceList() ) {
                    ventilationDevice.setResistance(Double.valueOf(e.getResistance()));
                    ventilationDevice.setInitialResistance(Double.valueOf(e.getInitialResistance()));
                    ventilationDevice.setEfficiency(Double.valueOf(e.getEfficiency()));
                }
            });

    }


}
