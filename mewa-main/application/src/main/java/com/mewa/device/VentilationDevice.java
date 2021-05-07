package com.mewa.device;

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
    private String bypass;
    private String motor;
    private double efficiency;
    private double initialResistance;
    private double resistance;

    private SerialPort serialPort;

    public VentilationDevice(String portName) {
        this.serialPort = new SerialPort(portName);
        try {
            id = 1;
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (SerialPortException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setBypass(int bypass) {
        this.bypass = bypass == 1 ? "F" : "V";
    }

    public void setMotor(int motor) {
        this.motor = motor == 1 ? "T" : "N";
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
}
