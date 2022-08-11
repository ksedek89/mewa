package com.mewa.service;

import com.mewa.device.MoxaDevice;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.mewa.util.FrameUtil.getMoxaFrameForSiu;

@Service
@Slf4j
//obsługa urzązdeń moxy
public class MoxaService {
    @Value(value = "${moxa.config-file-path}")
    private String moxaConfigFilePath;

    @Autowired
    UdpClientService clientService;

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

    @Async
    public Future<MoxaDevice> handleMoxaDevices(MoxaDevice moxaDevice) throws Exception{
        boolean reachable = InetAddress.getByName(moxaDevice.getIp()).isReachable(1000);
        moxaDevice.setStatus(reachable? "A": "F");
        return new AsyncResult(moxaDevice);
    }

    public void handleSiuFrame(List<MoxaDevice> moxaDeviceList) {
        String frame = getMoxaFrameForSiu(moxaDeviceList);
        clientService.sendDatagram(frame);
    }
}
