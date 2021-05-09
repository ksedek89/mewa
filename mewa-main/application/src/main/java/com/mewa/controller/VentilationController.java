package com.mewa.controller;

import com.mewa.device.VentilationDevice;
import com.mewa.service.device.VentilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class VentilationController {

    @Autowired
    private VentilationService ventilationService;

    @PostMapping(value = "/turn-on")
    public VentilationDevice turnOn() throws Exception {
        ventilationService.turnOnEngine();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off")
    public VentilationDevice turnOffVentilation() throws Exception {
        ventilationService.turnOff();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off-bypass")
    public VentilationDevice turnOffBypass() throws Exception {
        ventilationService.turnOffBypass();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off-engine")
    public VentilationDevice turnOffEngine() throws Exception {
        ventilationService.turnOffEngine();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-on-ventilation")
    public VentilationDevice turnOnVentilation() throws Exception {
        ventilationService.turnOnVentilation();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-on-filter")
    public VentilationDevice turnOnFilter() throws Exception {
        ventilationService.turnOnFilter();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/switch-ventilation")
    public VentilationDevice switchToVentilation() throws Exception {
        ventilationService.turnOffBypass();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/switch-filter")
    public VentilationDevice switchToFilter() throws Exception {
        ventilationService.turnOnBypass();
        return ventilationService.handleSiu();
    }

    @PostMapping(value = "/measure")
    public VentilationDevice measure() throws Exception {
        return ventilationService.handleSiu();
    }
}
