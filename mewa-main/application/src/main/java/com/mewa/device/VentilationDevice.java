package com.mewa.device;

import com.mewa.enums.SerialEnum;
import com.mewa.enums.TypeE;
import jssc.SerialPort;
import jssc.SerialPortException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class VentilationDevice implements Device{
    //bypass mowi czy jest wlaczona filtrowentylacja czy wentylacja, motor czy jest wlaczony silnik.
    // Zeby dzialala w jakimkolwiek trybie silnik musi byc wlaczony. Bypass on mowi ze pracuje w trybie filtrowentylacji.
    // Bypass off oznacza wentylacje

    private int id;
    private String bypass = "F";
    private String motor ="F";
    //wydajność
    private double efficiency;
    //opoery filtro pochłaniczy
    private double initialResistance;
    //opory wstępne
    private double resistance;
    private double contamination;
    private double puncture;

    private int moxaId;
    private TypeE type;

    private SerialPort serialPort;

    public VentilationDevice(String portName, int moxaId, TypeE type) {
        this.serialPort = new SerialPort(portName);
        this.type = type;
        this.id = 1;
        this.moxaId = moxaId;

    }

    public void setBypass(int bypass) {
        this.bypass = bypass >= 1 ? "F" : "V";
    }

    public void setMotor(int motor) {
        this.motor = motor == 1 ? "A" : "N";
    }

    @Override
    public String toString() {
        return "VentilationDevice{" +
            "bypass='" + bypass + '\'' +
            ", motor='" + motor + '\'' +
            ", efficiency=" + efficiency +
            ", initialResistance=" + initialResistance +
            ", resistance=" + resistance +
            '}';
    }

    public void setEfficiency(double efficiency) {
        if(efficiency > 2000|| efficiency < 0 || Double.isNaN(efficiency)){
            return;
        }
        this.efficiency = efficiency;
    }

    public void setInitialResistance(double initialResistance) {
        if(initialResistance > 6000 || initialResistance < 0 || Double.isNaN(initialResistance)){
            return;
        }
        this.initialResistance = initialResistance;
    }

    public void setResistance(double resistance) {
        if(resistance > 6000 || resistance < 0 || Double.isNaN(resistance)){
            return;
        }
        this.resistance = resistance;
    }
}
