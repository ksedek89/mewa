package com.mewa.service.device;

import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.service.UdpService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

import static com.mewa.util.FrameUtil.getPressureFrameForSiu;
import static com.mewa.util.FrameUtil.getVentilationFrameForSiu;
import static com.mewa.util.Utils.ModRtuCrc;

@Service
@Slf4j
public class VentilationService {
    @Autowired
    private UdpService udpService;

    private VentilationDevice ventilationDevice;

    //silnik - flag, bypass - bypass
    private static final byte[] MOTOR_ON =      new byte[] { 2, 5, 0, 0, (byte)255, 0};
    private static final byte[] MOTOR_OFF =     new byte[] {2, 5, 0, 0, 0, 0};
    private static final byte[] BYPASS_ON =     new byte[] { 2, 5, 0, 5, (byte)255, 0};
    private static final byte[] BYPASS_OFF=     new byte[] {2, 5, 0, 5, 0, 0};

    private static final byte[] CHECK_MOTOR=    new byte[] {2, 2, 0, 0, 0 , 1};
    private static final byte[] CHECK_BYPASS=   new byte[] {2, 1, 0, 5, 0 , 1};

    public void turnOff() throws Exception {
        log.info("Turning off all");
        turnOffBypass();
        turnOffEngine();
    }

    public void turnOffBypass() throws Exception {
        log.info("Turning off bypass");
        sendFrameToDevice(BYPASS_OFF);
        readFrameFromDevice();
    }

    public void turnOffEngine() throws Exception {
        log.info("Turning off engine");
        sendFrameToDevice(MOTOR_OFF);
        readFrameFromDevice();
    }

    public void turnOn() throws Exception {
        log.info("Turning on");
        sendFrameToDevice(MOTOR_ON);
        readFrameFromDevice();
     }

    public void turnOnVentilation() throws Exception {
        log.info("Turning on ventilation");
        turnOn();
        sendFrameToDevice(BYPASS_OFF);
        readFrameFromDevice();
    }

    public void turnOnFilter() throws Exception {
        log.info("Turning on filter");
        turnOn();
        sendFrameToDevice(BYPASS_ON);
        readFrameFromDevice();
    }

    public void switchToVentilation() throws Exception {
        log.info("Switch to ventilation");
        sendFrameToDevice(BYPASS_OFF);
        readFrameFromDevice();
    }

    public void switchToFilter() throws Exception {
        log.info("Switch to filter");
        sendFrameToDevice(BYPASS_ON);
        readFrameFromDevice();
    }

    public void handleSiu() throws Exception {
        sendFrameToDevice(CHECK_MOTOR);
        byte[] motor = readFrameFromDevice();
        sendFrameToDevice(CHECK_BYPASS);
        byte[] bypass = readFrameFromDevice();
        ventilationDevice.setBypass((int) bypass[3]);
        ventilationDevice.setMotor((int) motor[3]);

        String frame = getVentilationFrameForSiu(ventilationDevice);
        udpService.sendDatagram(frame);
    }

    public void setVentilationDevice(VentilationDevice ventilationDevice) {
        this.ventilationDevice = ventilationDevice;
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
        byte[] receivedBytes = serialPort.readBytes();
        System.out.print("Received bytes from ventilation: ");
        for(int i = 0;i<receivedBytes.length;i++){
            System.out.print(String.format("0x%02X",receivedBytes[i])+" ");
        }
        System.out.println();
        return receivedBytes;
    }
}
