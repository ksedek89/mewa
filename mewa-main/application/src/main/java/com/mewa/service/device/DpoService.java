package com.mewa.service.device;

import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
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
import java.util.Arrays;
import java.util.List;

import static com.mewa.util.FrameUtil.getDpoFrameForSiu;
import static com.mewa.util.Utils.NANO;
import static com.mewa.util.Utils.getFactorCompareToNano;
import static com.mewa.util.Utils.getNumericValueFromByte;
import static com.mewa.util.Utils.ieee32Format;


@Service
@Slf4j
public class DpoService {
    private static final byte[] REQUEST_STATUS_FRAME = "#1@".getBytes();
    private static final byte[] REQUEST_POWER_FRAME = "#2@".getBytes();
    private static final byte[] REQUEST_DOSAGE_FRAME = "#3@".getBytes();

    int counter = 0;

    @Autowired
    private ThresholdValuesService thresholdValuesService;

    @Autowired
    UdpClientService udpClientService;

    @Async
    public void handleDpoDevice(List<DpoDevice> dpoDeviceList, List<MoxaDevice> moxaDeviceList) throws Exception{
        if(dpoDeviceList.size()==0 || dpoDeviceList.get(0) == null){
            return;
        }
        MoxaDevice moxaDevice = moxaDeviceList.stream().filter(e -> e.getId() == dpoDeviceList.get(0).getMoxaId()).findFirst().get();
        if(!"A".equals(moxaDevice.getStatus())){
            return;
        }

        for(DpoDevice dpoDevice: dpoDeviceList) {
            if (dpoDevice.getType().equals(TypeE.SYM)) {
                prepareSymData(dpoDevice);
                String frameForSiu =  preprareFrameForSiu(dpoDevice);
                udpClientService.sendDatagram(frameForSiu);
            }
        }
        if(dpoDeviceList.stream().anyMatch(e->e.getType().equals(TypeE.SYM))){
            return;
        }

        if(counter++ > 30){
            counter = 0;
            sendFrameToDevice(dpoDeviceList.get(0), REQUEST_STATUS_FRAME);
            byte[] statusFrameFromDpo = readFrameFromDevice(dpoDeviceList.get(0));
            log.info("30 seconds");
            return;
        }

        sendFrameToDevice(dpoDeviceList.get(0), REQUEST_DOSAGE_FRAME);
        byte[] dosageFrameFromDpo = readFrameFromDevice(dpoDeviceList.get(0));
        sendFrameToDevice(dpoDeviceList.get(0), REQUEST_POWER_FRAME);
        byte[] powerFrameFromDpo = readFrameFromDevice(dpoDeviceList.get(0));

        for(DpoDevice dpoDevice: dpoDeviceList) {
            setDataToDevice(dpoDevice, dosageFrameFromDpo, powerFrameFromDpo);
            String frameForSiu =  preprareFrameForSiu(dpoDevice);
            udpClientService.sendDatagram(frameForSiu);
        }
    }

    private void prepareSymData(DpoDevice dpoDevice) {
        if(dpoDevice.getPower() == null){
            dpoDevice.setPower("0");
            dpoDevice.setDosage("0");
            return;
        }
        Double power = Double.valueOf(dpoDevice.getPower());
        if (power > thresholdValuesService.getThresholdValue().getValue3() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit3())) {
            dpoDevice.setAlarm(3);
        } else if (power > thresholdValuesService.getThresholdValue().getValue2() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit2())) {
            dpoDevice.setAlarm(2);
        } else if (power > thresholdValuesService.getThresholdValue().getValue1() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit1())) {
            dpoDevice.setAlarm(1);
        }else{
            dpoDevice.setAlarm(0);
        }
    }

    private void sendFrameToDevice(DpoDevice dpoDevice, byte[] frame) throws Exception{
            SerialPort serialPort = dpoDevice.getSerialPort();
            if(!serialPort.isOpened()){
                serialPort.openPort();
                serialPort.setParams(SerialEnum.DPO.getBaudRate(), SerialEnum.DPO.getDataBits(), SerialEnum.DPO.getStopBits(), SerialEnum.DPO.getParityBits());
            }
            ByteBuffer bb = ByteBuffer.allocate(frame.length);
            bb.put(frame);
            byte[] sendingBytes = bb.array();
            serialPort.writeBytes(sendingBytes);
            Thread.sleep(500);
    }

    private byte[] readFrameFromDevice(DpoDevice dpoDevice) throws Exception{
        SerialPort serialPort = dpoDevice.getSerialPort();
        if(!serialPort.isOpened()){
            return null;
        }
        byte[] receivedBytes = serialPort.readBytes();
/*        if(receivedBytes != null) {
            System.out.println("Received data:" + " ");
            for (int i = 0; i < receivedBytes.length; i++) {
                System.out.print(String.format("0x%02X", receivedBytes[i]) + " ");

            }
            System.out.println();
            System.out.println(new String(receivedBytes));
        }*/
        return receivedBytes;
    }

    private void setDataToDevice(DpoDevice dpoDevice,  byte[] dosageFrameFromDpo, byte[] powerFrameFromDpo){
        if(dosageFrameFromDpo == null || powerFrameFromDpo == null || dosageFrameFromDpo.length == 1 || powerFrameFromDpo.length == 1){
            dpoDevice.setErrorCode(1);
            dpoDevice.setDosage("0");
            dpoDevice.setPower("0");
            return;
        }
            dpoDevice.setErrorCode(0);
        int intexL = 7;
        int indexH = 11;
        if(getNumericValueFromByte(dosageFrameFromDpo, 2) == 2 && dpoDevice.getId() == 1){
            intexL = 17;
            indexH = 21;
        }else if(getNumericValueFromByte(dosageFrameFromDpo, 2) == 1 && (dpoDevice.getId() == 1 || dpoDevice.getId() == 2)){
            if((getNumericValueFromByte(dosageFrameFromDpo, 4) == 0 && dpoDevice.getId() == 2) ||
                getNumericValueFromByte(dosageFrameFromDpo, 4) == 1 && dpoDevice.getId() == 1 ){
                dpoDevice.setErrorCode(1);
                return;
            }
        }

        try {
            byte[] dossage = Arrays.copyOfRange(dosageFrameFromDpo, intexL, indexH);
            byte[] power = Arrays.copyOfRange(powerFrameFromDpo, intexL, indexH);
            dpoDevice.setDosage(String.format("%.0f", ieee32Format(dossage) * NANO));
            dpoDevice.setPower(String.format("%.0f", ieee32Format(power) * NANO));
            if(dpoDevice.getPower().equalsIgnoreCase("0") || dpoDevice.getPower().startsWith("-")){
                dpoDevice.setErrorCode(1);
            }

            int alarm = 0;
            if (ieee32Format(power) * NANO >
                thresholdValuesService.getThresholdValue().getValue3() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit3())) {
                alarm = 3;
            } else if (ieee32Format(power) * NANO >
                thresholdValuesService.getThresholdValue().getValue2() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit2())) {
                alarm = 2;
            } else if (ieee32Format(power) * NANO >
                thresholdValuesService.getThresholdValue().getValue1() * getFactorCompareToNano(thresholdValuesService.getThresholdValue().getUnit1())) {
                alarm = 1;
            }
            dpoDevice.setAlarm(alarm);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

    private String preprareFrameForSiu(DpoDevice dpoDevice) {
        return getDpoFrameForSiu(dpoDevice, thresholdValuesService);
    }



}
