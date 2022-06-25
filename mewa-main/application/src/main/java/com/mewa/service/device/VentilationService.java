package com.mewa.service.device;

import com.mewa.device.MoxaDevice;
import com.mewa.device.VentilationDevice;
import com.mewa.enums.TypeE;
import com.mewa.service.UdpClientService;
import jssc.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;

import static com.mewa.enums.SerialEnum.VENTILATION;
import static com.mewa.util.FrameUtil.getVentilationFrameForSiu;
import static com.mewa.util.Utils.ModRtuCrc;

@Service
@Slf4j
public class VentilationService {
    boolean eightTurnedOn;


    @Autowired
    private UdpClientService udpClientService;

    private VentilationDevice ventilationDevice;

    //silnik - flag, bypass - bypass
    private static final byte[] MOTOR_ON =          new byte[] { 2, 5, 0, 0, (byte)255, 0};
    private static final byte[] MOTOR_OFF =         new byte[] {2, 5, 0, 0, 0, 0};
    private static final byte[] BYPASS_ON =         new byte[] { 2, 5, 0, 5, (byte)255, 0};
    private static final byte[] BYPASS_OFF=         new byte[] {2, 5, 0, 5, 0, 0};
    private static final byte[] EIGHT_OUT_ON =         new byte[] { 2, 5, 0, 8, (byte)255, 0};
    private static final byte[] EIGHT_OUT_OFF =         new byte[] { 2, 5, 0, 8, 0, 0};

    private static final byte[] CHECK_MOTOR=    new byte[] {2, 2, 0, 0, 0, 1};
    private static final byte[] CHECK_BYPASS=   new byte[] {2, 1, 0, 5, 0, 1};
    private static final byte[] CHECK_BYPASS_MANUAL =   new byte[] {2, 2, 0, 5, 0, 1};
    private static final byte[] CHECK_PRESSURE= new byte[] {2, 4, 0, 49, 0, 1};
    private static final byte[] CHECK_EFFICIENCY= new byte[] {2, 4, 0, 48, 0, 1};

    private static final byte[] CONAMINATION_ON =      new byte[] { 2, 5, 0, 8, (byte)255, 0};
    private static final byte[] CONAMINATION_OFF =     new byte[] {2, 5, 0, 8 , 0, 0};
    private static final byte[] CHECK_CONTAMINATION =    new byte[] {2, 1, 0, 8, 0, 1};

    private static final byte[] PUNCTURE_ON =      new byte[] { 2, 5, 0, 9, (byte)255, 0};
    private static final byte[] PUNCTURE_OFF =     new byte[] {2, 5, 0, 9 , 0, 0};
    private static final byte[] CHECK_PUNCTURE =    new byte[] {2, 1, 0, 9, 0, 1};

    int counter = 0;

    public void turnOnEngine() throws Exception {
        log.info("Turning on");
        sendFrameToDevice(MOTOR_ON);
        readFrameFromDevice();
    }


    public void turnOnEightEngine() throws Exception {
        if(!eightTurnedOn){
            eightTurnedOn = true;
            log.info("Turning on eight");
            sendFrameToDevice(EIGHT_OUT_ON);
            readFrameFromDevice();
        }
    }

    public void turnOffEightEngine() throws Exception {
        if(eightTurnedOn) {
            eightTurnedOn = false;
            log.info("Turning off eight");
            sendFrameToDevice(EIGHT_OUT_OFF);
            readFrameFromDevice();
        }
    }

    public void turnOffEngine() throws Exception {
        log.info("Turning off engine");
        sendFrameToDevice(MOTOR_OFF);
        readFrameFromDevice();
    }

    public void turnOnBypass() throws Exception {
        log.info("Turning on bypass");
        sendFrameToDevice(BYPASS_ON);
        readFrameFromDevice();
    }

    public void turnOffBypass() throws Exception {
        log.info("Turning off bypass");
        sendFrameToDevice(BYPASS_OFF);
        readFrameFromDevice();
    }

    public void turnOff() throws Exception {
        log.info("Turning off");
        turnOffEngine();
        turnOffBypass();
    }

    public void turnOnVentilation() throws Exception {
        log.info("Turning on ventilation");
        turnOnEngine();
        turnOffBypass();
    }

    public void turnOnFilter() throws Exception {
        log.info("Turning on filter");
        turnOnEngine();
        turnOnBypass();
    }

    public void turnOnContamination() throws Exception {
        log.info("Turning on Contamination");
        sendFrameToDevice(CONAMINATION_ON);
        readFrameFromDevice();

    }

    public void turnOffContamination() throws Exception {
        log.info("Turning off Contamination");
        sendFrameToDevice(CONAMINATION_OFF);
        readFrameFromDevice();
    }

    public void turnOnPuncture() throws Exception {
        log.info("Turning on Contamination");
        sendFrameToDevice(PUNCTURE_ON);
        readFrameFromDevice();
    }

    public void turnOffPuncture() throws Exception {
        log.info("Turning off Contamination");
        sendFrameToDevice(PUNCTURE_OFF);
        readFrameFromDevice();
    }

    public void handleVentilationFrame(String data) throws Exception {
        String[] values = data.split("\\*")[0].split(",");
        String mode = values[2];
        if(ventilationDevice.getType().equals(TypeE.SYM)){
            if(mode.equalsIgnoreCase("O")){
                ventilationDevice.setMotor(0);
            }else if(mode.equalsIgnoreCase("F")){
                ventilationDevice.setMotor(1);
                ventilationDevice.setBypass(1);
            }else if(mode.equalsIgnoreCase("W")){
                ventilationDevice.setMotor(1);
                ventilationDevice.setBypass(0);
            }
            String frame = getVentilationFrameForSiu(ventilationDevice);
            udpClientService.sendDatagram(frame);
            return;
        }

        if(mode.equalsIgnoreCase("O")){
            turnOff();
        }else if(mode.equalsIgnoreCase("F")){
            turnOnFilter();
        }else if(mode.equalsIgnoreCase("W")){
            turnOnVentilation();
        }
        handleSiu(true);
    }
    @Async
    public void handleSiuAsync(List<MoxaDevice> moxaDeviceList) throws Exception{
        if(ventilationDevice == null){
            return;
        }
        if(ventilationDevice.getType().equals(TypeE.SYM)){
            String frame = getVentilationFrameForSiu(ventilationDevice);
            log.debug(ventilationDevice.toString());
            udpClientService.sendDatagram(frame);
            return;
        }

        MoxaDevice moxaDevice = moxaDeviceList.stream().filter(e -> e.getId() == ventilationDevice.getMoxaId()).findFirst().get();
        if(!"A".equals(moxaDevice.getStatus())){
            return;
        }
        //every 5 second
        if(counter++ >= 5 ) {
            counter = 0;
            handleSiu(true);
        }else{
            handleSiu(false);

        }
    }

    public VentilationDevice handleSiu(boolean requestDevice) throws Exception {
        if(requestDevice) {
            log.info("Request for ventilation data");
            //motor
            sendFrameToDevice(CHECK_MOTOR);
            byte[] motor = readFrameFromDevice();
            if (motor == null || motor.length < 3) {
                log.error("No data motor");
            } else {
                ventilationDevice.setMotor((int) motor[3]);
            }

            //bypass
            sendFrameToDevice(CHECK_BYPASS);
            byte[] bypass = readFrameFromDevice();

            //bypass manual
            sendFrameToDevice(CHECK_BYPASS_MANUAL);
            byte[] bypassManual = readFrameFromDevice();
            if (bypass == null || bypass.length < 3 || bypassManual == null || bypassManual.length < 3) {
                log.error("No data bypass");
            } else {
                ventilationDevice.setBypass((int) bypass[3] | (int) bypassManual[3]);
            }

            //pressure
            sendFrameToDevice(CHECK_PRESSURE);
            byte[] pressure = readFrameFromDevice();
            if (pressure == null || pressure.length < 4) {
                log.error("No data pressure");
            } else {
                ventilationDevice.setInitialResistance(calculateResistance(pressure));
            }

            //efficiency
            sendFrameToDevice(CHECK_EFFICIENCY);
            byte[] efficiency = readFrameFromDevice();
            if (efficiency == null || efficiency.length < 4) {
                log.error("No data efficiency");
            } else {
                if ("V".equals(ventilationDevice.getBypass())) {
                    ventilationDevice.setResistance(0);
                } else {
                    ventilationDevice.setResistance(calculateResistance(efficiency));
                }
                ventilationDevice.setEfficiency(calculateEfficiency(efficiency));
            }

            if(ventilationDevice.isTurnOnEight()){
                sendFrameToDevice(EIGHT_OUT_ON);
                byte[] frameEightTurnOn = readFrameFromDevice();
            }else{
                sendFrameToDevice(EIGHT_OUT_OFF);
                byte[] frameEightTurnOff = readFrameFromDevice();
            }

            //contamination
            sendFrameToDevice(CHECK_CONTAMINATION);
            byte[] contamination = readFrameFromDevice();
            if (contamination == null || contamination.length < 3) {
                log.error("No data contamination");
            } else {
                ventilationDevice.setContamination((int) contamination[3]);
            }

            //puncture
            sendFrameToDevice(CHECK_PUNCTURE);
            byte[] puncture = readFrameFromDevice();
            if (puncture == null || puncture.length < 3) {
                log.error("No data puncture");
            } else {
                ventilationDevice.setPuncture((int) puncture[3]);
            }
        }

        String frame = getVentilationFrameForSiu(ventilationDevice);
        log.info(ventilationDevice.toString());
        udpClientService.sendDatagram(frame);
        return ventilationDevice;
    }

    public void setVentilationDevice(VentilationDevice ventilationDevice) {
        this.ventilationDevice = ventilationDevice;
    }

    private double calculateResistance(byte[] pressure){
        byte firstByte = pressure[3];
        byte secondByte = pressure[4];
        double b = (firstByte << 8 &0xFF00) | (secondByte & 0x00ff);
        return (b/2764.7 - 5)*1000;
    }

    private double calculateEfficiency(byte[] efficiency){
        byte firstByte = efficiency[3];
        byte secondByte = efficiency[4];
        double b = (firstByte << 8 &0xFF00) | (secondByte & 0x00ff);
        log.info("First byte: " + firstByte + ", second byte: " + secondByte);
        log.info("raw value: " + b);
        double c = (b/2764.7 - 5);
        log.info("raw value c: " + c);
//        if(c<0){
//            return 0;
//        }
//        return ((0.0154 * Math.sqrt(2*(c*1000/1.21)))*3600);
        return 3600 * 0.082 * 0.082 * 3.14 * 1.291 * Math.sqrt(c*1000);
    }

    private void sendFrameToDevice(byte[] frame) throws Exception{
        SerialPort serialPort = ventilationDevice.getSerialPort();
        if(!serialPort.isOpened()){
            serialPort.openPort();
            serialPort.setParams(VENTILATION.getBaudRate(), VENTILATION.getDataBits(), VENTILATION.getStopBits(), VENTILATION.getParityBits());
        }
        byte[] crc = ModRtuCrc(frame, frame.length);
        ByteBuffer bb = ByteBuffer.allocate(frame.length + crc.length);
        bb.put(frame);
        bb.put(crc);
        byte[] sendingBytes = bb.array();
        serialPort.writeBytes(sendingBytes);
        Thread.sleep(500);
    }

    private byte[] readFrameFromDevice() throws Exception{
        SerialPort serialPort = ventilationDevice.getSerialPort();
        if(!serialPort.isOpened()){
            serialPort.openPort();
            serialPort.setParams(VENTILATION.getBaudRate(), VENTILATION.getDataBits(), VENTILATION.getStopBits(), VENTILATION.getParityBits());
        }
        byte[] receivedBytes = serialPort.readBytes();
        if(receivedBytes == null){
            return null;
        }

        StringBuilder builderLogger = new StringBuilder();
        builderLogger.append("Ventilation Sensor: ");
        for (int i = 0; i < receivedBytes.length; i++) {
            builderLogger.append(String.format("0x%02X", receivedBytes[i]) + " ");
        }
        log.info(builderLogger.toString());
        return receivedBytes;
    }

    public VentilationDevice getVentilationDevice() {
        return ventilationDevice;
    }
}
