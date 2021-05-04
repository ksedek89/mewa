package com.mewa.util;


import com.mewa.device.PressureDevice;

import static com.mewa.util.Utils.calculateCheckSumForSiu;
import static com.mewa.util.Utils.getCurrentDateForSiu;

public class FrameUtil {
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
        frame.append("\r");
        frame.append("\n");
        return frame.toString();
    }
}
