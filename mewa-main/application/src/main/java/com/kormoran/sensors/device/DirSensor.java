package com.kormoran.sensors.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jssc.SerialPort;

import org.apache.log4j.Logger;


import com.kormoran.sensors.Rs485;
import com.kormoran.sensors.RsDevice;



        //czujnik kierunkowy
public class DirSensor implements RsDevice {
    final static Logger logger = Logger.getLogger(DirSensor.class);

    private Rs485 rs485;    

    private final String frame = "Sk?";
    private int id;
    private String angle;

    private String direction = "0";
    private String totalDosage = "0";
    private String totalDosagePrefix = "U";
    private String radAlarm = "0";
    private static String threshold1;
    private static String prefixThreshold1;
    private static String threshold2;
    private static String prefixThreshold2;
    private static String threshold3;
    private static String prefixThreshold3;
    private String errorCode = "0";
    
    private String initNeutrons;
    private String neutrons;
    //only for main list
    List<DirSensor> dirSensorList = new ArrayList<DirSensor>();
    


    private boolean sendData = false;
    
    public DirSensor() {
        super();
        rs485 = new Rs485();
        rs485.setBaudRate(SerialPort.BAUDRATE_9600);
        rs485.setDataBits(SerialPort.DATABITS_8);
        rs485.setParityBit(SerialPort.PARITY_NONE);
        rs485.setStopBit(SerialPort.STOPBITS_1);   
    }
    
    public static boolean isConfigurationCorrect(){
        double thres1  = Double.valueOf(threshold1);
        double thres2  = Double.valueOf(threshold2);
        double thres3  = Double.valueOf(threshold3);
        double factor1 = getFactor(prefixThreshold1);
        double factor2 = getFactor(prefixThreshold2);
        double factor3 = getFactor(prefixThreshold3);
        double value1 = thres1*factor1;
        double value2 = thres2*factor2;
        double value3 = thres3*factor3;
        return (value3>=value2&&value2>=value1);
    }
    
    public  int isAlarm(){
        return 0;
    }
    
    public static int getFactor(String value1){
        return 1;
    }

    @Override
    public void setData(byte[] data) {
    }



    @Override

    public void setId(int id)  {

    }

    @Override
    public boolean needRequest() {
        return true;
    }


    @Override
    public void sendFrame(String comPort) throws Exception{

    }

    @Override
    public byte[] getFrame(String comPort) {
        return null;
    }



    @Override
    public void setSimulateData() {
    }

    @Override
    public void generateSimulationData() {
        Random random = new Random();
        direction  = String.valueOf(random.nextInt(360)-180);
        totalDosage = String.valueOf(random.nextInt(1000));
        
        int temp = new Random().nextInt(3);

        radAlarm =  String.valueOf(isAlarm());
        errorCode =  String.valueOf(random.nextInt(16));
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setTotalDosage(String totalDosage) {
        this.totalDosage = totalDosage;
    }

    public String getTotalDosage() {
        return totalDosage;
    }

    public void setTotalDosagePrefix(String totalDosagePrefix) {
        this.totalDosagePrefix = totalDosagePrefix;
    }

    public String getTotalDosagePrefix() {
        return totalDosagePrefix;
    }

    public void setRadAlarm(String radAlarm) {
        this.radAlarm = radAlarm;
    }

    public String getRadAlarm() {
        return radAlarm;
    }

    public static void setThreshold1(String threshold1) {
        DirSensor.threshold1 = threshold1;
    }

    public static String getThreshold1() {
        return threshold1;
    }

    public static void setPrefixThreshold1(String prefixThreshold1) {
        DirSensor.prefixThreshold1 = prefixThreshold1.toUpperCase();
    }

    public static String getPrefixThreshold1() {
        return prefixThreshold1;
    }

    public static void setThreshold2(String threshold2) {
        DirSensor.threshold2 = threshold2;
    }

    public static String getThreshold2() {
        return threshold2;
    }

    public static void setPrefixThreshold2(String prefixThreshold2) {
        DirSensor.prefixThreshold2 = prefixThreshold2.toUpperCase();
    }

    public static String getPrefixThreshold2() {
        return prefixThreshold2;
    }

    public static void setThreshold3(String threshold3) {
        DirSensor.threshold3 = threshold3;
    }

    public static String getThreshold3() {
        return threshold3;
    }

    public static void setPrefixThreshold3(String prefixThreshold3) {
        DirSensor.prefixThreshold3 = prefixThreshold3.toUpperCase();
    }

    public static String getPrefixThreshold3() {
        return prefixThreshold3;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getUdpFrame() {
        return null;
    }

    @Override
    public void sendDataOverUdp(String frame) {

    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getAngle() {
        return angle;
    }
    
    public int getId() {
        return id;
    }

    public void setDirSensorList(List<DirSensor> dirSensorList) {
        this.dirSensorList = dirSensorList;
    }

    public List<DirSensor> getDirSensorList() {
        return dirSensorList;
    }

    public String getUdp2Frame() {
        return null;
    }



    public String setThreshold(String threshold) {
        return null;
    }

    public void setInitNeutrons(String initNeutrons) {
        this.initNeutrons = initNeutrons;
    }

    public String getInitNeutrons() {
        return initNeutrons;
    }

    public void setNeutrons(String neutrons) {
        this.neutrons = neutrons;
    }

    public String getNeutrons() {
        return neutrons;
    }
}
