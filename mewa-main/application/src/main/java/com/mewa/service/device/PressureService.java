package com.mewa.service.device;

import com.mewa.device.PressureDevice;
import com.mewa.service.UdpClientService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

import static com.mewa.util.FrameUtil.getPressureFrameForSiu;
import static com.mewa.util.Utils.ModRtuCrc;

@Service
@Slf4j
public class PressureService {
    private static final byte[] PRESSURE_DEVICE_REQUEST_FRAME = new byte[] { 1, 4, 0, 1, 0, 1};

    @Autowired
    UdpClientService udpClientService;

    @Async
    public void handlePressureDevice(PressureDevice pressureDevice) throws Exception{
        sendFrameToDevice(pressureDevice);
        byte[] frameFromPressure = readFrameFromDevice(pressureDevice);
        setDataToDevice(pressureDevice, frameFromPressure);
        String frameForSiu =  preprareFrameForSiu(pressureDevice);
        udpClientService.sendDatagram(frameForSiu);
    }

    private void sendFrameToDevice(PressureDevice pressureDevice) throws Exception{
            SerialPort serialPort = pressureDevice.getSerialPort();
            if(!serialPort.isOpened()){
                serialPort.openPort();
                serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            }
            byte[] crc = ModRtuCrc(PRESSURE_DEVICE_REQUEST_FRAME, PRESSURE_DEVICE_REQUEST_FRAME.length);
            ByteBuffer bb = ByteBuffer.allocate(PRESSURE_DEVICE_REQUEST_FRAME.length + crc.length);
            bb.put(PRESSURE_DEVICE_REQUEST_FRAME);
            bb.put(crc);
            byte[] sendingBytes = bb.array();
            serialPort.writeBytes(sendingBytes);
            Thread.sleep(500);
    }

    private byte[] readFrameFromDevice(PressureDevice pressureDevice) throws Exception{
        SerialPort serialPort = pressureDevice.getSerialPort();
        byte[] receivedBytes = serialPort.readBytes();
        return receivedBytes;
    }

    private void setDataToDevice(PressureDevice pressureDevice, byte[] data){
        if(data != null) {
            pressureDevice.setErrorCode(0);
            int c = (data[3] << 8 & 0xFF00) | (data[4] & 0x00ff);
            if (c > 32768) {
                pressureDevice.setPressure(c - 65536);
            } else {
                pressureDevice.setPressure(c);
            }
            pressureDevice.setAlarm(pressureDevice.getPressure() > pressureDevice.getThreshold() ? 1 : 0);
        }else{
            pressureDevice.setErrorCode(1);
        }
        log.debug(pressureDevice.toString());
    }

    private String preprareFrameForSiu(PressureDevice pressureDevice) {
        return getPressureFrameForSiu(pressureDevice);
    }

}
