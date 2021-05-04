package com.mewa.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Server  {

    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(8086);
            byte[] receiveData = new byte[1024];
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                log.info("Received data: " + sentence);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
