package com.mewa.service;

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
    private UnicastSendingMessageHandler handler;

    public void receive(Message message) {
        String data = new String((byte[]) message.getPayload()).replace("\n", "").replace("\r", "");
        log.info("Received data: " + data);
        if (data.startsWith("$PCARCC")) {
            thresholdValuesService.updateThresholdValues(data);
        }else if(data.startsWith("$PCARSE")){

        }
    }

    @Async
    public void sendDatagram(String frame){
        try{
            log.debug("Frame sent: "+ frame.replaceAll("\n", "").replaceAll("\r", ""));
            handler.handleMessageInternal(new GenericMessage(frame));
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
