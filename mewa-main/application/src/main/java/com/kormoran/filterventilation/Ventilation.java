package com.kormoran.filterventilation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import com.kormoran.sensors.device.CarfsMessage;
public class Ventilation implements Runnable {
    private final static Logger logger = Logger.getLogger(Ventilation.class);
    private String writeToFilename;
    private String value;
    
    public Ventilation(String value) {
        super();
        try {
            if(value.equalsIgnoreCase("off")){
                logger.info("Konstruktor");
            }
            this.value = value;
            Properties prop = new Properties();
            String propFileName = "filter.properties";
            File jarPath = new File(Ventilation.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            prop.load(new FileInputStream(propertiesPath + "/" + propFileName));
            writeToFilename = prop.getProperty("write.file");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() { 
        saveToFile(value);  
    }
    
    public void saveToFile(String command){
        try {
            if(command.equalsIgnoreCase("off")){
                logger.info("Zapisuje off");
            }
            if(command.equalsIgnoreCase("status") && CarfsMessage.isChangingState()){
                return;
            }
            PrintWriter writer;
            writer = new PrintWriter(writeToFilename, "UTF-8");
            writer.println(command);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
