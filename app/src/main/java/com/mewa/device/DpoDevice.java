package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
//czujnik dpo
public class DpoDevice implements Device{
    private int id;
    private int siuId;
    private String dosage;
    private String power;
    private int alarm;
    private int errorCode;
    private TypeE type;

    private int moxaId;

    private int errorCounter;
    private int symIteration = 1;


    private SerialPort serialPort;

    public DpoDevice(String portName, int id, int moxaId, TypeE typeE) {
        if(id == 1){
            siuId = 3;
        }else if(id == 3){
            siuId = 1;
        }else{
            siuId = 2;
        }
        this.type = typeE;
        this.moxaId = moxaId;
        this.id = id;
        this.serialPort = new SerialPort(portName);
        this.dosage = "0";
        this.power = "0";
    }

    @Override
    public String toString() {
        return "DpoDevice{" +
            "id=" + id +
            ", dosage='" + dosage + '\'' +
            ", power='" + power + '\'' +
            ", alarm=" + alarm +
            ", errorCode=" + errorCode +
            '}';
    }

    public void setDosage(String dosage) {
        try {
            Double value = Double.valueOf(dosage);
            if(value > 10000000 || (dosage.equals("0") &&  errorCode != 1)){
                return;
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        this.dosage = dosage;
    }

    public void setPower(String power) {
        try {
            Double value = Double.valueOf(power);
            if(value > 10000000 || (power.equals("0") &&  errorCode != 1)){
                return;
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        this.power = power;
    }
}
