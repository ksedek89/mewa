package com.mewa.service.device;

import com.mewa.device.OxygenDevice;
import com.mewa.enums.TypeE;
import com.mewa.service.UdpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import static com.mewa.util.FrameUtil.getOxygenFrameForSiu;

@Service
@Slf4j
public class OxygenService {
    @Autowired
    UdpClientService udpClientService;

    @Async
    public void handlOxygenDevice(OxygenDevice oxygenDevice) {
        if(oxygenDevice.getType().equals(TypeE.SYM)){
            prepareSymData(oxygenDevice);
        }
        String frameForSiu =  preprareFrameForSiu(oxygenDevice);
        udpClientService.sendDatagram(frameForSiu);
    }

    private void prepareSymData(OxygenDevice oxygenDevice) {
        oxygenDevice.setCo2Alarm(oxygenDevice.getCo2() > oxygenDevice.getCo2Threshold() ? "1" : "0");
        oxygenDevice.setOxygenAlarm(oxygenDevice.getOxygen() < oxygenDevice.getOxygenThreshold() ? "1" : "0");
    }

    private String preprareFrameForSiu(OxygenDevice oxygenDevice) {
        return getOxygenFrameForSiu(oxygenDevice);
    }

}
