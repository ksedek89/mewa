package com.mewa.device;

import com.mewa.enums.TypeE;
import jssc.SerialPort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Data
@Slf4j
public class DirectionDevice implements Device{

    private int id;
    private int directionAngle;
    private String totalDosagePrefix;
    private int radAlarm;
    private long errorCode;

    private int moxaId;
    private TypeE type;
    private SerialPort serialPort;

    //dawka całkowita
    private long totalDosage;
    private int totalDosageSubnormalCount;

    private long neutrons;
    private boolean neutronsSubnormalValue;

    private long initNeutrons;
    private boolean initNeutronsSubnormalValue;

    private int symIteration = 1;


    public DirectionDevice(String portName, int id, int moxaId, Integer directionAngle, TypeE typeE) {
        this.type = typeE;
        this.moxaId = moxaId;
        this.id = id;
        this.directionAngle = directionAngle;
        this.totalDosagePrefix = "N";
        this.serialPort = new SerialPort(portName);
    }

    public void setInitNeutrons(long initNeutrons) {
        if(normalInitNeutronsValue(initNeutrons)){
            this.initNeutrons = initNeutrons;
        }
    }

    public void setNeutrons(long neutrons) {
        if(normalNeutronsValue(neutrons)){
            this.neutrons = neutrons;
        }
    }

    public void setTotalDosage(long totalDosage) {
        if(normalDosageValue(totalDosage)){
            this.totalDosage = totalDosage;
        }
    }

    private boolean normalDosageValue(long newDosage) {
        if(newDosage > totalDosage + 10000){
            if(totalDosageSubnormalCount < 5){
                totalDosageSubnormalCount++;
               return false;
            }else{
                return true;
            }
        }else{
            totalDosageSubnormalCount = 0;
            return true;
        }
    }

    private boolean normalNeutronsValue(long newNeutrons) {
        if(newNeutrons > neutrons + 10){
            if(neutronsSubnormalValue){
                return true;
            }else{
                neutronsSubnormalValue = true;
                return false;
            }
        }else{
            return true;
        }
    }

    private boolean normalInitNeutronsValue(long newInitNeutrons) {
        if(newInitNeutrons > initNeutrons + 10){
            if(initNeutronsSubnormalValue){
                return true;
            }else{
                initNeutronsSubnormalValue = true;
                return false;
            }
        }else{
            return true;
        }
    }

}
