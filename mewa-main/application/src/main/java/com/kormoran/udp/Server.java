package com.kormoran.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.List;

import org.apache.log4j.Logger;

import com.kormoran.sensors.Device;
import com.kormoran.sensors.Filter;
import com.kormoran.sensors.Threshold;


public class Server implements Runnable {
    private final static Logger logger = Logger.getLogger(Server.class);

    private static int incomingPort;

    private List<Device> deviceList;
    public Server (List<Device> deviceList){
        super();
        this.deviceList = deviceList;
    }

    @Override
    public void run() {
        try {
            logger.info("Listening on port: " + incomingPort);
            DatagramSocket serverSocket = new DatagramSocket(incomingPort);
            byte[] receiveData = new byte[1024];
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                logger.info("Received data: " + sentence);
                if(sentence.startsWith("$PCARCC")){
                    new Thread(new Threshold(sentence)).start();
                }
                if(sentence.startsWith("$PCARSE")){
                    new Thread(new Filter(deviceList,sentence)).start();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static void setIncomingPort(int incomingPort) {
        Server.incomingPort = incomingPort;
    }

    public static int getIncomingPort() {
        return incomingPort;
    }
}
