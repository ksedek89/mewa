package com.mewa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//klient udp
public class UdpClientService {
    @Autowired
    private UnicastSendingMessageHandler handler;

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
