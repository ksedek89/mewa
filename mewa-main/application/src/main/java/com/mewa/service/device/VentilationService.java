package com.mewa.service.device;

import com.mewa.device.VentilationDevice;
import com.mewa.service.UdpClientService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

import static com.mewa.util.FrameUtil.getVentilationFrameForSiu;
import static com.mewa.util.Utils.ModRtuCrc;

@Service
@Slf4j
public class VentilationService {
    @Autowired
    private UdpClientService udpClientService;

    private VentilationDevice ventilationDevice;

    //silnik - flag, bypass - bypass
    private static final byte[] MOTOR_ON =      new byte[] { 2, 5, 0, 0, (byte)255, 0};
    private static final byte[] MOTOR_OFF =     new byte[] {2, 5, 0, 0, 0, 0};
    private static final byte[] BYPASS_ON =     new byte[] { 2, 5, 0, 5, (byte)255, 0};
    private static final byte[] BYPASS_OFF=     new byte[] {2, 5, 0, 5, 0, 0};

    private static final byte[] CHECK_MOTOR=    new byte[] {2, 2, 0, 0, 0, 1};
    private static final byte[] CHECK_BYPASS=   new byte[] {2, 1, 0, 5, 0, 1};
    private static final byte[] CHECK_PRESSURE= new byte[] {2, 4, 0, 31, 0, 1};
    private static final byte[] CHECK_EFFICIENCY= new byte[] {2, 4, 0, 32, 0, 1};

    public void turnOnEngine() throws Exception {
        log.info("Turning on");
        sendFrameToDevice(MOTOR_ON);
        readFrameFromDevice();
    }

    public void turnOffEngine() throws Exception {
        log.info("Turning off engine");
        sendFrameToDevice(MOTOR_OFF);
        readFrameFromDevice();
    }

    public void turnOnBypass() throws Exception {
        log.info("Turning on bypass");
        sendFrameToDevice(BYPASS_ON);
        readFrameFromDevice();
    }

    public void turnOffBypass() throws Exception {
        log.info("Turning off bypass");
        sendFrameToDevice(BYPASS_OFF);
        readFrameFromDevice();
    }

    public void turnOff() throws Exception {
        log.info("Turning off");
        turnOffEngine();
        turnOffBypass();
    }

    public void turnOnVentilation() throws Exception {
        log.info("Turning on ventilation");
        turnOnEngine();
        turnOffBypass();
    }

    public void turnOnFilter() throws Exception {
        log.info("Turning on filter");
        turnOnEngine();
        turnOnBypass();
    }

    public void handleVentilationFrame(String data) throws Exception {
        String[] values = data.split(",");
        String mode = values[2];
        String state = values[4].substring(0, 1);
        if(state.equalsIgnoreCase("0")){
            turnOff();
        }else if(mode.equalsIgnoreCase("F")){
            turnOnVentilation();
        }else if(mode.equalsIgnoreCase("W")){
            turnOnFilter();
        }
        handleSiu();
    }

    public void handleSiu() throws Exception {
        sendFrameToDevice(CHECK_MOTOR);
        byte[] motor = readFrameFromDevice();
        ventilationDevice.setMotor((int) motor[3]);

        sendFrameToDevice(CHECK_BYPASS);
        byte[] bypass = readFrameFromDevice();
        ventilationDevice.setBypass((int) bypass[3]);

        sendFrameToDevice(CHECK_PRESSURE);
        byte[] pressure = readFrameFromDevice();
        ventilationDevice.setInitialResistance(calculateResistance(pressure));

        sendFrameToDevice(CHECK_EFFICIENCY);
        byte[] efficiency = readFrameFromDevice();
        if(ventilationDevice.getBypass().equals("W")){
            ventilationDevice.setResistance(0);
        }else {
            ventilationDevice.setResistance(calculateResistance(efficiency));
        }
        ventilationDevice.setEfficiency(calculateEfficiency(efficiency));

        String frame = getVentilationFrameForSiu(ventilationDevice);
        log.info(ventilationDevice.toString());
        udpClientService.sendDatagram(frame);
    }

    public void setVentilationDevice(VentilationDevice ventilationDevice) {
        this.ventilationDevice = ventilationDevice;
    }

    private double calculateResistance(byte[] pressure){
        byte firstByte = pressure[3];
        byte secondByte = pressure[4];
        double b = (firstByte << 8 &0xFF00) | (secondByte & 0x00ff);
        return (b/2764.7 - 5)*1000;
    }

    private double calculateEfficiency(byte[] efficiency){
        byte firstByte = efficiency[3];
        byte secondByte = efficiency[4];
        double b = (firstByte << 8 &0xFF00) | (secondByte & 0x00ff);
        double c = (b/2764.7 - 5);
        if(c<0){
            return 0;
        }
        return ((0.0154 * Math.sqrt(2*(c*1000/1.21)))*3600);
    }

    private void sendFrameToDevice(byte[] frame) throws Exception{
        SerialPort serialPort = ventilationDevice.getSerialPort();
        if(!serialPort.isOpened()){
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        }
        byte[] crc = ModRtuCrc(frame, frame.length);
        ByteBuffer bb = ByteBuffer.allocate(frame.length + crc.length);
        bb.put(frame);
        bb.put(crc);
        byte[] sendingBytes = bb.array();
        serialPort.writeBytes(sendingBytes);
        Thread.sleep(500);
    }

    private byte[] readFrameFromDevice() throws Exception{
        SerialPort serialPort = ventilationDevice.getSerialPort();
        if(!serialPort.isOpened()){
            return null;
        }
        byte[] receivedBytes = serialPort.readBytes();
        System.out.print("Received bytes: ");
        for(int i = 0;i<receivedBytes.length;i++){
            System.out.print(String.format("0x%02X",receivedBytes[i])+" ");
        }
        System.out.println();
        return receivedBytes;
    }


}
