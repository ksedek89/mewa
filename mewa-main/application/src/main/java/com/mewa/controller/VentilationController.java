package com.mewa.controller;

import com.mewa.service.device.VentilationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class VentilationController {

    @Autowired
    private VentilationService ventilationService;

    @PostMapping(value = "/turn-on")
    public void turnOn() throws Exception {
        ventilationService.turnOnEngine();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off")
    public void turnOffVentilation() throws Exception {
        ventilationService.turnOff();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off-bypass")
    public void turnOffBypass() throws Exception {
        ventilationService.turnOffBypass();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-off-engine")
    public void turnOffEngine() throws Exception {
        ventilationService.turnOffEngine();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-on-ventilation")
    public void turnOnVentilation() throws Exception {
        ventilationService.turnOnVentilation();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/turn-on-filter")
    public void turnOnFilter() throws Exception {
        ventilationService.turnOnFilter();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/switch-ventilation")
    public void switchToVentilation() throws Exception {
        ventilationService.turnOffBypass();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/switch-filter")
    public void switchToFilter() throws Exception {
        ventilationService.turnOnBypass();
        ventilationService.handleSiu();
    }

    @PostMapping(value = "/measure")
    public void measure() throws Exception {
        ventilationService.handleSiu();
    }
}
