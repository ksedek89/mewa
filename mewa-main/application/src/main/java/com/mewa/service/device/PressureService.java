package com.mewa.service.device;

import com.mewa.device.MoxaDevice;
import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import com.mewa.service.UdpClientService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;

import static com.mewa.util.FrameUtil.getPressureFrameForSiu;
import static com.mewa.util.Utils.ModRtuCrc;

@Service
@Slf4j
public class PressureService {
    private static final byte[] PRESSURE_DEVICE_REQUEST_FRAME = new byte[] { 1, 4, 0, 1, 0, 1};

    @Autowired
    UdpClientService udpClientService;
    @Qualifier("taskExecutor")
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    VentilationService ventilationService;


    @Async
    public void handlePressureDevice(PressureDevice pressureDevice, List<MoxaDevice> moxaDeviceList) throws Exception{
        MoxaDevice moxaDevice = moxaDeviceList.stream().filter(e -> e.getId() == pressureDevice.getMoxaId()).findFirst().get();
        if(!"A".equals(moxaDevice.getStatus())){
            return;
        }
        if(pressureDevice.getType().equals(TypeE.SYM)){
            //jesli urządzenie jest symulowane to zamiast pobrania ramki trzeba sobie taką wygenerować
            prepareSymData(pressureDevice);
        }else {
            //jeśli jest urządzenie rzeczywiste wyślij ramkę odpytującą do urządzenia
            sendFrameToDevice(pressureDevice);
            //pobierz dane z urządzenia
            byte[] frameFromPressure = readFrameFromDevice(pressureDevice);
            //ustaw dane do obiektu urządzenia danymi otrzymanymi z ramki
            setDataToDevice(pressureDevice, frameFromPressure);
        }
        //przygotuj ramkę
        String frameForSiu =  preprareFrameForSiu(pressureDevice);
        //wyślij ramkę do siu po udp (datagram)
        udpClientService.sendDatagram(frameForSiu);
    }

    private void prepareSymData(PressureDevice pressureDevice) {
        pressureDevice.setAlarm(pressureDevice.getPressure() > pressureDevice.getThreshold() ? 0 : 1);
    }

    private void sendFrameToDevice(PressureDevice pressureDevice) throws Exception{
        SerialPort serialPort = pressureDevice.getSerialPort();
            if(!serialPort.isOpened()){
                serialPort.openPort();
                serialPort.setParams(SerialEnum.PRESSURE.getBaudRate(), SerialEnum.PRESSURE.getDataBits(), SerialEnum.PRESSURE.getStopBits(), SerialEnum.PRESSURE.getParityBits());
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
        if(!serialPort.isOpened()){
            return null;
        }
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
            VentilationDevice ventilationDevice = ventilationService.getVentilationDevice();
            if(pressureDevice.getPressure() < pressureDevice.getThreshold()
                && ventilationDevice !=null
                && ventilationDevice.getBypass().equals("F")
                && ventilationDevice.getMotor().equals("A")){
                pressureDevice.setAlarm(1);
            }else{
                pressureDevice.setAlarm(0);
            }
        }else{
            pressureDevice.setErrorCode(1);
        }
        log.debug(pressureDevice.toString());
    }

    private String preprareFrameForSiu(PressureDevice pressureDevice) {
        return getPressureFrameForSiu(pressureDevice);
    }

}
