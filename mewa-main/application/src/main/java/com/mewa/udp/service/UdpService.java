package com.mewa.udp.service;

import com.mewa.service.ThresholdValuesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UdpService {
    @Autowired
    private ThresholdValuesService thresholdValuesService;

    public void receive(Message message) {
        String data = new String((byte[]) message.getPayload()).replace("\n", "").replace("\r", "");
        log.info("Received data: " + data);
        if (data.startsWith("$PCARCC")) {
            thresholdValuesService.updateThresholdValues(data);
        }else if(data.startsWith("$PCARSE")){

        }
    }
}
