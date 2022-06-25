package com.mewa.service.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mewa.device.OxygenDevice;
import com.mewa.dto.oxygen.OxygenDto;
import com.mewa.enums.TypeE;
import com.mewa.service.UdpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.mewa.util.FrameUtil.getOxygenFrameForSiu;

@Service
@Slf4j
public class OxygenService {
    @Autowired
    UdpClientService udpClientService;

    private HttpClient client;
    HttpRequest request;
    @Autowired
    private ObjectMapper objectMapper;

    public OxygenService(@Value("${oxygen.url}") String oxygenUrl) {
        client = HttpClient.newBuilder().build();
        request = HttpRequest.newBuilder()
            .uri(URI.create(oxygenUrl))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .GET()
            .build();
    }

    @Async
    public void handlOxygenDevice(OxygenDevice oxygenDevice){
        if(oxygenDevice.getType().equals(TypeE.SYM)){
            prepareSymData(oxygenDevice);
        }else{
            getData(oxygenDevice);
        }
        String frameForSiu =  preprareFrameForSiu(oxygenDevice);
        udpClientService.sendDatagram(frameForSiu);
    }

    private void getData(OxygenDevice oxygenDevice){
        oxygenDevice.setErrorCode("0");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            OxygenDto varDto = objectMapper.readValue(body, OxygenDto.class);
            oxygenDevice.setCo2(varDto.getInput().stream().filter(e -> e.getMode().startsWith("co2")).findFirst().get().getVar().get(0).getV());
            oxygenDevice.setOxygen(varDto.getInput().stream().filter(e -> e.getMode().startsWith("o2")).findFirst().get().getVar().get(0).getV());
            oxygenDevice.setCo2Alarm(oxygenDevice.getCo2() > oxygenDevice.getCo2Threshold() ? "1" : "0");
            oxygenDevice.setOxygenAlarm(oxygenDevice.getOxygen() < oxygenDevice.getOxygenThreshold() ? "1" : "0");
            oxygenDevice.setErrorCode("0");
        }catch (Exception e){
            log.debug(e.getMessage(), e);
            oxygenDevice.setErrorCode("1");
        }
    }

    private void prepareSymData(OxygenDevice oxygenDevice) {
        oxygenDevice.setErrorCode("0");
        oxygenDevice.setCo2Alarm(oxygenDevice.getCo2() > oxygenDevice.getCo2Threshold() ? "1" : "0");
        oxygenDevice.setOxygenAlarm(oxygenDevice.getOxygen() < oxygenDevice.getOxygenThreshold() ? "1" : "0");
    }

    private String preprareFrameForSiu(OxygenDevice oxygenDevice) {
        return getOxygenFrameForSiu(oxygenDevice);
    }

}
