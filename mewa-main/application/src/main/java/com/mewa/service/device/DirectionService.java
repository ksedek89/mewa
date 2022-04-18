package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import com.mewa.service.ThresholdValuesService;
import com.mewa.service.UdpClientService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;


import static com.mewa.util.FrameUtil.getDirectionFrameForSiu;
import static com.mewa.util.Utils.getRandomIntBetween;

@Service
@Slf4j
public class DirectionService {
    private final String DIRECTION_DEVICE_REQUEST_FRAME = "Sk?";

    @Autowired
    private UdpClientService udpClientService;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Async
    public void handleDirectionDevice(DirectionDevice directionDevice){
        try {
            if(directionDevice.getType().equals(TypeE.SYM)) {
                prepareSymData(directionDevice);
            }else{
                sendFrameToDevice(directionDevice);
                byte[] bytes = readFrameFromDevice(directionDevice);
                setDataToDevice(directionDevice, bytes);
            }

            String frameForSiu = getDirectionFrameForSiu(directionDevice, thresholdValuesService);
            udpClientService.sendDatagram(frameForSiu);
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }


    private void prepareSymData(DirectionDevice directionDevice) {
        int symIteration = directionDevice.getSymIteration();
        if(symIteration > 30){
            symIteration = 1;
        }
        if(symIteration <=20){
            directionDevice.setTotalDosage(getRandomIntBetween(1000, 2500));
        }else if(symIteration <= 30){
            directionDevice.setTotalDosage(getRandomIntBetween(25000, 30000));
        }
        directionDevice.setRadAlarm(isAlarm(directionDevice));
        directionDevice.setNeutrons(0);
        directionDevice.setInitNeutrons(0);
        directionDevice.setRadAlarm(isAlarm(directionDevice));
        directionDevice.setSymIteration(++symIteration);
    }


    private void sendFrameToDevice(DirectionDevice directionDevice) throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(DIRECTION_DEVICE_REQUEST_FRAME.length());
        bb.put(DIRECTION_DEVICE_REQUEST_FRAME.getBytes());
        byte[] sendingBytes = bb.array();
        SerialPort serialPort = directionDevice.getSerialPort();
        if(!serialPort.isOpened()){
            serialPort.openPort();
            serialPort.setParams(SerialEnum.DIRECTION.getBaudRate(), SerialEnum.DIRECTION.getDataBits(), SerialEnum.DIRECTION.getStopBits(), SerialEnum.DIRECTION.getParityBits());
        }
        serialPort.writeBytes(sendingBytes);
    }

    private byte[] readFrameFromDevice(DirectionDevice directionDevice) throws Exception{
        SerialPort serialPort = directionDevice.getSerialPort();
        if(!serialPort.isOpened()){
            return null;
        }
        return serialPort.readBytes();
    }

    private void setDataToDevice(DirectionDevice directionDevice, byte[] bytes){
        if (bytes != null) {
/*            System.out.println("Data from sensor: " + +directionDevice.getId());
            for (int i = 0; i < bytes.length; i++) {
                System.out.print(String.format("0x%02X", bytes[i]) + " ");
            }*/
            StringBuilder sb = new StringBuilder();
            for (int i = 19; i >=12; i--) {
                sb.append(String.format("%02X", bytes[i]));
            }
            long dosageInNano = Long.parseLong(sb.toString(), 16) / 10000;

            sb = new StringBuilder();
            for (int i = 11; i >= 4; i--) {
                sb.append(String.format("%02X", bytes[i]));
            }
            long totalDosageInNano = Long.parseLong(sb.toString(), 16) / 10000;
            sb = new StringBuilder();
            for (int i = 3; i >= 2; i--) {
                sb.append(String.format("%02X", bytes[i]));
            }
            long currentNeutrons = Long.parseLong(sb.toString(), 16);

            sb = new StringBuilder();
            for (int i = 1; i >= 0; i--) {
                sb.append(String.format("%02X", bytes[i]));
            }
            long initNeutrons = Long.parseLong(sb.toString(), 16)               ;
            directionDevice.setErrorCode(0);
            directionDevice.setInitNeutrons(initNeutrons);
            directionDevice.setNeutrons(currentNeutrons);
            directionDevice.setTotalDosage(dosageInNano);
            directionDevice.setRadAlarm(isAlarm(directionDevice));
        }else{
            directionDevice.setErrorCode(1);
        }
    }

    public int isAlarm(DirectionDevice directionDevice){
        double total = directionDevice.getTotalDosage() * getFactor(directionDevice.getTotalDosagePrefix());
        if(total>thresholdValuesService.getThresholdValue().getValue3() * getFactor(thresholdValuesService.getThresholdValue().getUnit3())){
            return 3;
        }else if(total>thresholdValuesService.getThresholdValue().getValue2() * getFactor(thresholdValuesService.getThresholdValue().getUnit2())){
            return 2;
        }else if(total>thresholdValuesService.getThresholdValue().getValue1() * getFactor(thresholdValuesService.getThresholdValue().getUnit1())){
            return 1;
        }else{
            return 0;
        }
    }

    private static int getFactor(String unit){
         if(unit.equalsIgnoreCase("m")){
            return 1000000;
        }else if(unit.equalsIgnoreCase("u")){
            return  1000;
        }else if(unit.equalsIgnoreCase("n")){
            return 1;
        }
        return 1;
    }
}
