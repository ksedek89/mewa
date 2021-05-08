package com.mewa.service.device;

import com.mewa.device.DirectionDevice;
import com.mewa.enums.SerialEnum;
import com.mewa.service.ThresholdValuesService;
import com.mewa.service.UdpClientService;
import com.mewa.service.UdpService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;


import static com.mewa.util.FrameUtil.getDirectionFrameForSiu;

@Service
@Slf4j
public class DirectionService {
    private final String DIRECTION_DEVICE_REQUEST_FRAME = "Sk?";

    @Autowired
    private UdpClientService udpClientService;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Async
    public Future<DirectionDevice> handleDirectionDevice(DirectionDevice directionDevice) throws Exception {
        sendFrameToDevice(directionDevice);
        byte[] bytes = readFrameFromDevice(directionDevice);
        setDataToDevice(directionDevice, bytes);
        return new AsyncResult(directionDevice);
    }

    @Async
    public void handleSiuFrame(List<DirectionDevice> directionDeviceList){
        DirectionDevice maxDosageDirectionDevice = directionDeviceList.stream().max(Comparator.comparing(DirectionDevice::getTotalDosage)).get();
        String frameForSiu = getDirectionFrameForSiu(maxDosageDirectionDevice, thresholdValuesService);
        udpClientService.sendDatagram(frameForSiu);
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

    private void setDataToDevice(DirectionDevice directionDevice, byte[] data){
        if(data==null) {
            directionDevice.setErrorCode(String.valueOf(1 << (directionDevice.getId() - 1)));
            directionDevice.setTotalDosage(0);
            return;
        }
//        System.out.println("Dir sensor:"+" ");
//        for(int i = 0;i<data.length;i++){
//            System.out.print(String.format("0x%02X",data[i])+" ");
//
//        }
//        System.out.println();
        for(int i = 0;i<data.length-12;i++){
            if((0xFF&data[i]) ==0x01&& ((0xFF&data[i+1]) == 0x1f|| (0xFF&data[i+1])  == 0xaa)){
                String value = String.valueOf((char)data[i+4]).concat(String.valueOf((char)data[i+5])).
                    concat(String.valueOf((char)data[i+6])).concat(String.valueOf((char)data[i+7])).
                    concat(String.valueOf((char)data[i+8])).concat(String.valueOf((char)data[i+9])).
                    concat(String.valueOf((char)data[i+10])).concat(String.valueOf((char)data[i+11]));
                value = value.replaceAll("\\s", "0");
                directionDevice.setTotalDosage((int)(Double.valueOf(value)*1000));
                directionDevice.setTotalDosagePrefix("N");
                directionDevice.setRadAlarm(isAlarm(directionDevice));
                directionDevice.setErrorCode("0");
                directionDevice.setNeutrons(data[i+25]);
                if(directionDevice.getInitNeutrons() == 0){
                    directionDevice.setInitNeutrons(directionDevice.getNeutrons());
                }
                break;
            }
        }
    }

    private  int isAlarm(DirectionDevice directionDevice){
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
