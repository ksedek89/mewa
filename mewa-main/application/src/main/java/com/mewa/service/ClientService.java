package com.mewa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


@Slf4j
@Service
public class ClientService{

    @Value(value = "${siu.ip}")
    private String ip;
    @Value(value = "${siu.port}")
    private int port;

    @Async
    public void sendDatagram(String frame){
        try{
            log.debug("Frame sent: "+ frame.replaceAll("\n", "").replaceAll("\r", ""));
            DatagramSocket clientSocket = new DatagramSocket();
            byte[] sendData = frame.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
            clientSocket.send(sendPacket);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }

    }

}
