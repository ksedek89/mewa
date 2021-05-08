package com.kormoran.sensors.device;


import com.kormoran.filterventilation.Ventilation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.PrintWriter;

import java.util.Properties;

import org.apache.log4j.Logger;


import com.kormoran.sensors.RsDevice;

import com.kormoran.udp.Client;
import com.kormoran.udp.SendData;
        //filtrowentylacja
public class CarfsMessage implements RsDevice {
    private final static Logger logger = Logger.getLogger(CarfsMessage.class);

    private String installationId = "1";
    private String workState = "N";
    private String workMode = "V";
    private String efficiency = "0";
    private String initialResistance = "0";
    private String resistance = "0";
    
    private String readFromFilename;
    private Ventilation ventilation;

    private int counter = 0;
    
    private static boolean changingState;
    private static int changingStateCtn;
    
    public CarfsMessage() {
        super();
        try {
            Properties prop = new Properties();
            String propFileName = "filter.properties";
            File jarPath = new File(Ventilation.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            prop.load(new FileInputStream(propertiesPath + "/" + propFileName));
            readFromFilename = prop.getProperty("read.file");
            ventilation = new Ventilation("status");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        
    }

    @Override
    public void setId(int id){
    }

    public int getId() {
        return 0;
    }
    
    @Override
    public boolean needRequest() {
        return false;
    }

    @Override
    public void sendFrame(String comPort) throws Exception {
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
    try {
        checkStatus();   
        if(changingStateCtn>15){
            changingState = false;
            changingStateCtn = 0;
        }
        if(changingState){
            changingStateCtn++;
            return;
        }else{
            changingStateCtn = 0;           
        }
        Properties prop = new Properties();
        prop.load(new FileInputStream(readFromFilename));

        if(prop.getProperty("filter_res")!=null){
            resistance = String.valueOf(Float.parseFloat(prop.getProperty("filter_res")) * 1000);
        }
        
        if(prop.getProperty("resistance")!=null){
            initialResistance = String.valueOf(Float.parseFloat(prop.getProperty("resistance")) * 1000);
        }
        
        if(prop.getProperty("workMode")!=null){
            workMode =prop.getProperty("workMode");
        }
        
        if(prop.getProperty("workState")!=null){
            workState =prop.getProperty("workState");
        }
        
        if(prop.getProperty("efficiency")!=null){
        }
        if(prop.getProperty("efficiency")!=null && !prop.getProperty("efficiency").equalsIgnoreCase("0.0")){
            efficiency =prop.getProperty("efficiency");
            counter = 0;
        }else{
            counter++;
        }

        PrintWriter writer = new PrintWriter(readFromFilename);
        writer.print("");
        writer.close();
    } catch (FileNotFoundException e) {
        logger.error(e.getMessage(), e);
    } catch (IOException e) {
        logger.error(e.getMessage(), e);
    }
}

    @Override
    public void setData(byte[] data) {
    }

    @Override
    public String getUdpFrame() {
        return new SendData().sendCarfs(this);
    }

    @Override
    public void sendDataOverUdp(String frame) {
        if(counter < 10){
            new Thread(new Client(frame)).start();
        }
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setInitialResistance(String initialResistance) {
        this.initialResistance = initialResistance;
    }

    public String getInitialResistance() {
        return initialResistance;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public String getResistance() {
        return resistance;
    }
    
    public void checkStatus(){
        new Thread(ventilation).start();
    }


    public String getUdp2Frame() {
        return null;
    }



    public String setThreshold(String threshold) {
        return null;
    }

    public static void setChangingState(boolean changingState) {
        CarfsMessage.changingState = changingState;
    }

    public static boolean isChangingState() {
        return changingState;
    }

    public static void setChangingStateCtn(int changingStateCtn) {
        CarfsMessage.changingStateCtn = changingStateCtn;
    }

    public static int getChangingStateCtn() {
        return changingStateCtn;
    }
}
