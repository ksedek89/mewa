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
//serwis inicujący wszystkie urządzenia na podstawie konfiguracji z bazy danych i ustawień portów szeregowych moxy
public class InitService {

    @Autowired
    private MoxaService moxaService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VentilationService ventilationService;

    @Autowired
    private MoxaProperties moxaProperties;

    @Autowired
    ThresholdValueRepository thresholdValueRepository;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Async
    public void init() throws Exception {

        //inicjacja wartości progowych z tabeli threshol_value do obiektu, z którym później będzie porównywanie
        thresholdValuesService.init();
        //pobiera aktywne urządzenia
        List<Device> activeDevices = deviceRepository.findAllByActiveEquals("T");
        //filtruje tylko urządzenia sym
        List<Device> symDevicesList = activeDevices.stream().filter(e->e.getType().equals(TypeE.SYM)).collect(Collectors.toList());
        //filtruje tylko urządzenia real
        List<Device> actDevicesList = activeDevices.stream().filter(e->e.getType().equals(TypeE.REAL)).collect(Collectors.toList());
        List<MoxaDevice> moxaDevices = null;
        //SIMULATION
        //jeśli jest więcej niż 0 urządzenia sym
        if(symDevicesList.size()>0) {
            //ustaw tryb symulacji na true
            deviceService.setSymulation(true);
            for (Device deviceDto : symDevicesList) {
                if (deviceDto.getDeviceType().equals("PRESS")) {
                    //dodaj urząedzenie sym do listy czujników ciśnienia
                    deviceService.getPressureDeviceList()
                        .add((new PressureDevice(null, deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getThresholdPressure(), deviceDto.getType())));
                }
                //dodaj urząedzenie sym do listy czujników tlenu
                if (deviceDto.getDeviceType().equals("OXY")) {
                    deviceService.getOxygenDeviceList().add((new OxygenDevice(deviceDto.getDeviceId(), deviceDto.getType())));
                }
                //dodaj urząedzenie sym do listy czujników kierunkowych
                if (deviceDto.getDeviceType().equals("DIR")) {
                    deviceService.getDirectionDeviceList()
                        .add((new DirectionDevice(null, deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getDirectionAngle(), deviceDto.getType())));
                }
                //dodaj urząedzenie sym do listy czujników dpo lub pojedynczego dpo
                if (deviceDto.getDeviceType().equals("DPO")) {
                    //konfiguracja jest tak zrobiona że sonda o id=3 jest podłączone do pojedynczego elementu
                    if (deviceDto.getDeviceId() == 3) {
                        deviceService.setSingleDpoDevice(new DpoDevice(null, deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getType()));
                    } else {
                        //konfiguracja jest tak zrobiona że sondy o id=2 lub id=1 są podłączone we dwie
                        deviceService.getDpoDeviceList().add(new DpoDevice(null, deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getType()));
                    }
                }
                //zainicuje urządzenie vent wg. konfiguracji (na razie jest jedno)
                if (deviceDto.getDeviceType().equals("VENT")) {
                    ventilationService.setVentilationDevice(new VentilationDevice(null,deviceDto.getMoxaNumber(), deviceDto.getType()));
                    deviceService.setVentilationDevice(ventilationService.getVentilationDevice());
                }
            }
            //jeśli nie ma żadnego rzeczywistego urządzenia to ustaw MoxaDevice na tryb sym i wysyłaj A czyli Avalible.
            if (actDevicesList.size() == 0) {
                moxaDevices = moxaProperties.getConfiguration().stream().map(e -> MoxaDevice.builder().status("A").type(TypeE.SYM).id(e.getId()).ip(e.getIp()).build()).collect(Collectors.toList());
            }
        }

        //ustawienie prawdziwych urządzeń
        if(actDevicesList.size()>0) {
            //REAL DEVICES
            //pobranie konfiguracji z pliku konfiguracyjnego  /usr/lib/npreal2/driver/npreal2d.cf do mapy
            Map<Integer, String> moxaConfigurationMap = moxaService.initMoxa();
            for (Device device : actDevicesList) {
                log.info("Device from properties: " + device.toString());
                if (device.getMoxaId() == null || device.getMoxaNumber() == null || device.getDeviceType() == null) {
                    throw new Exception("Bledna konfiguracja");
                }
            }
            for (Map.Entry<Integer, String> entry : moxaConfigurationMap.entrySet()) {
                List<Device> deviceList = activeDevices
                    .stream()
                    .filter(e -> e.getType().equals(TypeE.REAL) && entry.getKey().equals(e.getMoxaId() + 16 * (e.getMoxaNumber() - 1))).collect(Collectors.toList());
                if (deviceList.size() == 0) {
                    continue;
                }
                //ustawienie odpowiednich konfiguracji analogicznie jak dla sym tylko tym razem z przekazaniem danych portu szeregoweego
                for (Device deviceDto : deviceList) {
                    log.info("For element: " + entry.getKey() + ", port: " + entry.getValue() + ",  Found device:" + deviceDto.toString());
                    if (deviceDto.getDeviceType().equals("PRESS")) {
                        deviceService.getPressureDeviceList()
                            .add((new PressureDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getThresholdPressure(), deviceDto.getType())));
                    }
                    if (deviceDto.getDeviceType().equals("DIR")) {
                        deviceService.getDirectionDeviceList()
                            .add((new DirectionDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getDirectionAngle(), deviceDto.getType())));
                    }
                    if (deviceDto.getDeviceType().equals("VENT")) {
                        ventilationService.setVentilationDevice(new VentilationDevice(entry.getValue(), deviceDto.getMoxaNumber(), deviceDto.getType()));
                        deviceService.setVentilationDevice(ventilationService.getVentilationDevice());
                    }
                    if (deviceDto.getDeviceType().equals("OXY")) {
                        deviceService.getOxygenDeviceList().add((new OxygenDevice(deviceDto.getDeviceId(), deviceDto.getType())));
                    }
                    if (deviceDto.getDeviceType().equals("DPO")) {
                        if (deviceDto.getDeviceId() == 3) {
                            deviceService.setSingleDpoDevice(new DpoDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getType()));
                        } else {
                            deviceService.getDpoDeviceList().add(new DpoDevice(entry.getValue(), deviceDto.getDeviceId(), deviceDto.getMoxaNumber(), deviceDto.getType()));
                        }
                    }
                }
            }
            //jeśli jest conajmniej jedno urządzenie to ustaw moxę na real i wysyłaj to co dostaniesz z pinga
            moxaDevices = moxaProperties.getConfiguration().stream().map(e -> MoxaDevice.builder().type(TypeE.REAL).id(e.getId()).ip(e.getIp()).build()).collect(Collectors.toList());
        }

//        ustawienie urządzeń typu moxa
        deviceService.setMoxaDeviceList(moxaDevices);
        //koniec konfiguracji, od tego czasu wątki które są oznaczone @scheduled mogą działać
        deviceService.setConfigurationFinished(true);
    }

}
