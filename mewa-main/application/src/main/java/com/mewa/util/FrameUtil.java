package com.mewa.util;


import com.mewa.device.DirectionDevice;
import com.mewa.device.DpoDevice;
import com.mewa.device.MoxaDevice;
import com.mewa.device.OxygenDevice;
import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.enums.ThresholdE;
import com.mewa.model.entity.ThresholdValue;
import com.mewa.service.ThresholdValuesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.mewa.util.Utils.calculateCheckSumForSiu;
import static com.mewa.util.Utils.calculateToMicro;
import static com.mewa.util.Utils.getCurrentDateForSiu;

public class FrameUtil {
    @Autowired
    ThresholdValuesService thresholdValuesService;

    //czujnik cisnienia
    public static String getPressureFrameForSiu(PressureDevice pressureDevice) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARPS,");
        frame.append(getCurrentDateForSiu());
        frame.append(pressureDevice.getId() + ",");
        frame.append(pressureDevice.getPressure()+ ",");
        frame.append(pressureDevice.getThreshold()+ ",");
        frame.append(pressureDevice.getAlarm()+ ",");
        frame.append(pressureDevice.getErrorCode()+ "*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }


    //czujnik o2/co2
    public static String getOxygenFrameForSiu(OxygenDevice oxygenDevice) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARAC,");
        frame.append(getCurrentDateForSiu());
        frame.append(oxygenDevice.getOxygen()+ ",");
        frame.append(oxygenDevice.getOxygenThreshold()+ ",");
        frame.append(oxygenDevice.getOxygenAlarm()+ ",");
        frame.append(oxygenDevice.getCo2()+ ",");
        frame.append(oxygenDevice.getCo2Threshold()+ ",");
        frame.append(oxygenDevice.getCo2Alarm()+ ",");
        frame.append(oxygenDevice.getErrorCode()+ "*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }

    //czujnik kierunkowy
    public static String getDirectionFrameForSiu(DirectionDevice directionDevice, ThresholdValuesService thresholdValuesService) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARDS,");
        frame.append(getCurrentDateForSiu());
        frame.append(directionDevice.getDirectionAngle() + ",");
        frame.append(directionDevice.getTotalDosage() + ",");
        frame.append(directionDevice.getTotalDosagePrefix() + ",");
        frame.append(directionDevice.getRadAlarm() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue1() * calculateToMicro(thresholdValuesService.getThresholdValue().getUnit1()) + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue2() * calculateToMicro(thresholdValuesService.getThresholdValue().getUnit2()) + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue3() * calculateToMicro(thresholdValuesService.getThresholdValue().getUnit3()) + ",");

        frame.append(directionDevice.getNeutrons() + ",");
        frame.append(directionDevice.getInitNeutrons() + ",");

        frame.append(directionDevice.getErrorCode() + "*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }


    //filtrowentylacja
    public static String getVentilationFrameForSiu(VentilationDevice ventilationDevice) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARFS,");
        frame.append(getCurrentDateForSiu());

        frame.append(ventilationDevice.getMotor()+",");
        frame.append(ventilationDevice.getBypass()+",");
        frame.append(ventilationDevice.getEfficiency()+",");
        frame.append(ventilationDevice.getInitialResistance()+",");
        frame.append(ventilationDevice.getResistance()+"*");

        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }

    //moxa
    public static String getMoxaFrameForSiu(List<MoxaDevice> moxaDeviceList){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARIS,");
        frame.append(getCurrentDateForSiu());
        frame.append(moxaDeviceList.get(0).getStatus()+",");
        frame.append(moxaDeviceList.get(1).getStatus()+",");
        frame.append(moxaDeviceList.get(2).getStatus());
        frame.append("*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }


    public static String getThresholdsFrameForSiu(ThresholdValue thresholdValue) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARCA,");
        frame.append(getCurrentDateForSiu());
        frame.append(thresholdValue.getValue1()+",");
        frame.append(thresholdValue.getUnit1()+",");
        frame.append(thresholdValue.getValue2()+",");
        frame.append(thresholdValue.getUnit2()+",");
        frame.append(thresholdValue.getValue3()+",");
        frame.append(thresholdValue.getUnit3()+"*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }

    public static String getDpoFrameForSiu(DpoDevice dpoDevice, ThresholdValuesService thresholdValuesService){
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARRC,");
        frame.append(getCurrentDateForSiu());

        frame.append(dpoDevice.getId()+",");
        frame.append(dpoDevice.getDosage()+",");
        frame.append(ThresholdE.N + ",");
        frame.append(dpoDevice.getPower()+",");
        frame.append(ThresholdE.N + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue1() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue2() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue3() + ",");
        frame.append(dpoDevice.getAlarm()+",");
        frame.append(dpoDevice.getErrorCode()+"*");
        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }


}
