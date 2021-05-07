package com.mewa.util;


import com.mewa.device.DirectionDevice;
import com.mewa.device.PressureDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.service.ThresholdValuesService;
import org.springframework.beans.factory.annotation.Autowired;


import static com.mewa.util.Utils.calculateCheckSumForSiu;
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

    //czujnik kierunkowy
    public static String getDirectionFrameForSiu(DirectionDevice directionDevice, ThresholdValuesService thresholdValuesService) {
        StringBuilder frame = new StringBuilder();
        frame.append("$PCARDS,");
        frame.append(getCurrentDateForSiu());
        frame.append(directionDevice.getDirectionAngle() + ",");
        frame.append(directionDevice.getTotalDosage() + ",");
        frame.append(directionDevice.getTotalDosagePrefix() + ",");
        frame.append(directionDevice.getRadAlarm() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue1() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue2() + ",");
        frame.append(thresholdValuesService.getThresholdValue().getValue3() + ",");

        frame.append("0" + ",");
        frame.append(directionDevice.getNeutrons() + ",");
        frame.append((directionDevice.getNeutrons() - directionDevice.getInitNeutrons()) + ",");
        frame.append((directionDevice.getNeutrons() - directionDevice.getInitNeutrons()) + ",");

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

        frame.append(ventilationDevice.getId()+",");
        frame.append(ventilationDevice.getMotor()+",");
        frame.append(ventilationDevice.getBypass()+",");
        frame.append(ventilationDevice.getEfficiency()+",");
        frame.append(ventilationDevice.getInitialResistance()+",");
        frame.append(ventilationDevice.getResistance()+"*");

        frame.append(calculateCheckSumForSiu(frame.toString()));
        frame.append("\r\n");
        return frame.toString();
    }

}
