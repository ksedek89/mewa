package com.kormoran.reader;

import com.kormoran.exception.IllegalConfigurationException;

import com.kormoran.exception.PortNameNotFoundException;
import com.kormoran.exception.WritingIdToNonIdDeviceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.InputStreamReader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.log4j.Logger;

import com.kormoran.sensors.Device;

import com.kormoran.sensors.RsDevice;

import com.kormoran.sensors.Threshold;

import com.kormoran.sensors.device.CarisMessage;
import com.kormoran.sensors.device.DirSensor;
import com.kormoran.sensors.device.DptMod;
import com.kormoran.sensors.device.Label480;

import com.kormoran.sensors.device.RadSensor;

import com.kormoran.sensors.propertiesenum.DeviceType;
import com.kormoran.sensors.propertiesenum.PortType;

import com.kormoran.udp.Server;
import com.kormoran.udp.Client;
import com.kormoran.udp.TelnetClient;


public class Configuration {
    private final static Logger logger = Logger.getLogger(Configuration.class);

    private Map<Integer, String> portMap;
    private List<Device> deviceList;

    private boolean linux;
    private String pathToCf;
    private String pathToReinstallMoxaScript;
    
    public Configuration() {
        try {
            initMoxaDriver(initNetwork());
            initThreshold();
            initDevices();
            new Thread(new TelnetClient()).start();
        } catch (IllegalConfigurationException e) {
            logger.error(e.getMessage(), e);
        } catch (WritingIdToNonIdDeviceException e) {
            logger.error(e.getMessage(), e);
        } catch (PortNameNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch(Exception e){
            logger.error(e.getMessage(), e);                
        }
        deviceListToString();
    }

    //pobranie parametrow sieci + sciezki do plikow
    public String initNetwork() throws IllegalConfigurationException, FileNotFoundException, IOException {
        try {
            //setting network properties
            Properties prop = new Properties();

            File jarPath = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            prop.load(new FileInputStream(propertiesPath + "/" + "network.properties"));

            Client.setIp(prop.getProperty("outcoming.udp.ip"));
            Client.setPort(Integer.valueOf(prop.getProperty("outcoming.udp.port")));
            Server.setIncomingPort(Integer.valueOf(prop.getProperty("incoming.udp.port")));
            Reader.setTimestamp(Integer.valueOf(prop.getProperty("frame.send.timestamp"))*1000);
            Threshold.setCheckingChecksum(Boolean.valueOf(prop.getProperty("checkingChecksum")));
            linux = prop.getProperty("operating.system").equalsIgnoreCase("linux");

            pathToCf = prop.getProperty("npreal.path");
            pathToReinstallMoxaScript = prop.getProperty("script.path");
            String moxaIp = prop.getProperty("moxa.ip");

            if (moxaIp == null) {
                throw new IllegalConfigurationException();
            } else {
                return moxaIp;
            }

        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        }
    }


    public void initMoxaDriver(String moxaIp) throws FileNotFoundException, IOException {
/*        reinstallMoxaDriver(moxaIp);
        CarisMessage.MOXA_IP = moxaIp;
        boolean theSame = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pathToCf));
        } catch (Exception e) {
            reinstallMoxaDriver(moxaIp);
            br = new BufferedReader(new FileReader(pathToCf));
        }
        String line;
        portMap = new HashMap<Integer, String>();
        boolean startPort = false;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            if (line.startsWith("0")) {
                if (!tokens[1].equals(moxaIp)) {
                    theSame = true;
                    reinstallMoxaDriver(moxaIp);
                    br = new BufferedReader(new FileReader(pathToCf));
                    break;
                }
                startPort = true;
            }
            if (startPort) {
                portMap.put(Integer.valueOf(tokens[0]), tokens[6]);
            }
        }
        if (theSame){
            while((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if(line.startsWith("0")) {
                    startPort = true;
                }
                if (startPort){
                    portMap.put(Integer.valueOf(tokens[0]), tokens[6]);
                }
            }
        }*/
    }

    public void reinstallMoxaDriver(String moxaIp) {
        if(linux){
            try {
                logger.info("Starting reinstalling moxa driver");
                Process proc =  Runtime.getRuntime().exec(pathToReinstallMoxaScript  + " /"); //Whatever you want to execute
                BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                proc.waitFor();
                while (read.ready()) {
                    read.readLine();
                }
                logger.info("Ending reinstalling moxa driver");
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void initDevices() throws IllegalConfigurationException, WritingIdToNonIdDeviceException, PortNameNotFoundException, FileNotFoundException, IOException {
        try {
            //setting device properties
            Properties prop = new Properties();
            String propFileName = "device.properties";
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            //Do paczki
            File jarPath = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            prop.load(new FileInputStream(propertiesPath + "/" + propFileName));

            deviceList = new ArrayList<Device>();
            int counter = 0;
            for(int i = 1; i != 32; i++) {
                try{
                    if(prop.getProperty("port" + i + ".type") != null) {
                        PortType type = PortType.valueOf(prop.getProperty("port" + i + ".type").toUpperCase());
                        counter++;
                        deviceList.add(new Device());
                        deviceList.get(counter - 1).setType(type);
                    }else{
                        continue;
                    }
                    DeviceType deviceType = DeviceType.valueOf(prop.getProperty("port" + i + ".name").toUpperCase());
                    deviceList.get(counter - 1).setDeviceType(deviceType);
                    deviceList.get(counter - 1).setRsDevice(getRsDeviceFromEnum(deviceType));
                    deviceList.get(counter - 1).setPortNumber(i - 1);
                    deviceList.get(counter - 1).setPortName(getPortNameFromInteger(i - 1));

                    if(prop.getProperty("port" + i + ".id") != null) {
                        int portNumber = Integer.valueOf(prop.getProperty("port" + i + ".id"));
                        deviceList.get(counter - 1).getRsDevice().setId(portNumber);
                    }
                    
                    if(prop.getProperty("port" + i + ".pressure") != null) {
                        deviceList.get(counter - 1).getRsDevice().setThreshold(prop.getProperty("port" + i + ".pressure"));
                    }
                    
                }catch(WritingIdToNonIdDeviceException e) {
                    throw new WritingIdToNonIdDeviceException("Trying write id to " + deviceList.get(counter - 1).getDeviceType());
                }catch(Exception e) {
                    throw new IllegalConfigurationException("Wrong configuration for port number: " + i, e);
                }catch(PortNameNotFoundException ef) {
                    throw ef;
                }
            }
        }catch (FileNotFoundException fnfe) {
            throw fnfe;

        } catch (IOException ioe) {

            throw ioe;
        }


    }
    //inicjalizacja wartości progowych
    private void initThreshold() throws IOException, FileNotFoundException, IllegalConfigurationException {
        try {
            Properties prop = new Properties();
            String propFileName = "threshold.properties";

            File jarPath = new File(Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            prop.load(new FileInputStream(propertiesPath + "/" + propFileName));

            String[] properties1;
            String[] properties2;
            String[] properties3;

            properties1 = prop.getProperty("threshold1").split(" ");
            properties2 = prop.getProperty("threshold2").split(" ");
            properties3 = prop.getProperty("threshold3").split(" ");

            if (properties1.length != 2 || properties2.length != 2 || properties3.length != 2) {
                throw new IllegalConfigurationException("No spaces between threshold values");
            }

            DirSensor.setThreshold1(properties1[0]);
            DirSensor.setPrefixThreshold1(properties1[1].toUpperCase());
            DirSensor.setThreshold2(properties2[0]);
            DirSensor.setPrefixThreshold2(properties2[1].toUpperCase());
            DirSensor.setThreshold3(properties3[0]);
            DirSensor.setPrefixThreshold3(properties3[1].toUpperCase());

            if (!DirSensor.isConfigurationCorrect()) {
                DirSensor.setThreshold1(null);
                DirSensor.setPrefixThreshold1(null);
                DirSensor.setThreshold2(null);
                DirSensor.setPrefixThreshold2(null);
                DirSensor.setThreshold3(null);
                DirSensor.setPrefixThreshold3(null);
                throw new IllegalConfigurationException("Threshold 3 must be the greatest, Threshold 1 must be the smalest");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private RsDevice getRsDeviceFromEnum(DeviceType deviceType) {
        if (deviceType.equals(DeviceType.CO2)) {
            return new Label480();
        } else if (deviceType.equals(DeviceType.DIR)) {
            return new DirSensor();
        } else if (deviceType.equals(DeviceType.PRESS)) {
            return new DptMod();
        } else if (deviceType.equals(DeviceType.RAD)) {
            return new RadSensor();
        }
        return null;
    }

    private String getPortNameFromInteger(int portNumber) throws PortNameNotFoundException {
        for (Map.Entry a : portMap.entrySet()) {
            if (((Integer)a.getKey()) == portNumber)
                return linux ? "/dev/" + (String)a.getValue() : (String)a.getValue();
        }
        throw new PortNameNotFoundException();
    }



    private void deviceListToString() {
        if(deviceList!=null){
            for (Device a : deviceList) {
                logger.debug("Port Number: " + a.getPortNumber());
                logger.debug("Device Type: " + a.getDeviceType());
                logger.debug("Port Name: " + a.getPortName());
                logger.debug("Rodzaj urzadzenia: " + a.getRsDevice().getClass());
                logger.debug("Typ urzadzenia: " + a.getType());
            }
        }
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }


}
