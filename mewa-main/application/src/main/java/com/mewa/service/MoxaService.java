package com.mewa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MoxaService {
    @Value(value = "${moxa.ip}")
    private String ip;
    @Value(value = "${moxa.port}")
    private String port;
    @Value(value = "${moxa.script-path}")
    private String scriptPath;
    @Value(value = "${moxa.config-file-path}")
    private String moxaConfigFilePath;



    public Map<Integer, String> initMoxa() throws Exception{
        Map<Integer, String> moxaPortMap = new HashMap();
        boolean theSame = false;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(moxaConfigFilePath));
        } catch (Exception e) {
            reinstallMoxa();
            br = new BufferedReader(new FileReader(moxaConfigFilePath));
        }
        String line;
        boolean startPort = false;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            if (line.startsWith("0")) {
                if (!tokens[1].equals(ip)) {
                    theSame = true;
                    reinstallMoxa();
                    br = new BufferedReader(new FileReader(moxaConfigFilePath));
                    break;
                }
                startPort = true;
            }
            if (startPort) {
                moxaPortMap.put(Integer.valueOf(tokens[0])+1, tokens[6]);
            }
        }
        if (theSame) {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (line.startsWith("0")) {
                    startPort = true;
                }
                if (startPort) {
                    moxaPortMap.put(Integer.valueOf(tokens[0])+1, tokens[6]);
                }

            }
        }
        return moxaPortMap;
    }

    private void reinstallMoxa(){
        try {
            log.debug("Starting reinstalling moxa driver");
            Process proc =  Runtime.getRuntime().exec(scriptPath);
            BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            proc.waitFor();
            while (read.ready()) {
                log.info(read.readLine());
            }
            log.debug("Ending reinstalling moxa driver");
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }



}
