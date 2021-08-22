package com.mewa.service;

import com.mewa.service.device.VentilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UdpService {

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Autowired
    private VentilationService ventilationService;

    public void receive(Message message) throws Exception {
        String data = new String((byte[]) message.getPayload()).replace("\n", "").replace("\r", "");
//        log.info("Received data: " + data);
        if (data.startsWith("$PCARCC")) {
            thresholdValuesService.updateThresholdValues(data);
        }else if(data.startsWith("$PCARSE")){
            ventilationService.handleVentilationFrame(data);
        }
    }

}
