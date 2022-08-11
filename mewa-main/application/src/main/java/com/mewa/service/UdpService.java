package com.mewa.service;

import com.mewa.service.device.VentilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Slf4j
@Service
//serwer udp
public class UdpService {

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Autowired
    private VentilationService ventilationService;

    //przetwarzanie ramek przychodzących
    public void receive(Message message) throws Exception {
        String data = new String((byte[]) message.getPayload()).replace("\n", "").replace("\r", "");
        log.info("Received data: " + data);
        if (data.startsWith("$PCARCC")) {
            thresholdValuesService.updateThresholdValues(data);
        }else if(data.startsWith("$PCARSE")){
            ventilationService.handleVentilationFrame(data);
        }
    }

}
