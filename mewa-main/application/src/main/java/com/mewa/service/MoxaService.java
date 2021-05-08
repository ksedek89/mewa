package com.mewa.service;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
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
    @Value(value = "${moxa.config-file-path}")
    private String moxaConfigFilePath;

    public Map<Integer, String> initMoxa() throws Exception{
        Map<Integer, String> moxaPortMap = new HashMap();
        BufferedReader br = new BufferedReader(new FileReader(moxaConfigFilePath));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            if (StringUtils.isNumeric(tokens[0])) {
                moxaPortMap.put(Integer.valueOf(tokens[0])+1, "/dev/" + tokens[6]);
            }
        }
        Thread.sleep(1000);
        return moxaPortMap;
    }


}
