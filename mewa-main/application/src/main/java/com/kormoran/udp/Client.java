package com.kormoran.udp;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Logger;


public class Client implements Runnable{
    final static Logger logger = Logger.getLogger(Client.class);


    private static String ip;
    private static int port; 
    
    private String value;
    
    public Client(String value) {
      this.value = value;
    }

    public  void sendDatagram(){
/*        try{
            DatagramSocket clientSocket = new DatagramSocket();
            byte[] sendData = new byte[value.length()];
            String sentence= value;
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port);
            logger.debug("Datagram sent: "+ value);
            
            clientSocket.send(sendPacket);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }*/

    }

    @Override
    public void run() {
    sendDatagram();
    }

    public static void setIp(String ip) {
        Client.ip = ip;
    }

    public static String getIp() {
        return ip;
    }

    public static void setPort(int port) {
        Client.port = port;
    }

    public static int getPort() {
        return port;
    }
}
