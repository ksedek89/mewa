package com.mewa.service;


import com.mewa.model.entity.ThresholdValue;
import com.mewa.model.repository.ThresholdValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.mewa.util.Utils.calculateCheckSum;

@Service
@Slf4j
public class ThresholdValuesService {
    private  ThresholdValue thresholdValue;

    @Autowired
    private ThresholdValueRepository thresholdValueRepository;

    public void init(){
        thresholdValue = thresholdValueRepository.findAll().get(0);
    }

    public ThresholdValue getThresholdValue() {
        return thresholdValue;
    }

    public void updateThresholdValues(String datagram) {
        //0 - data, 1 - checksum
        String [] dataAndChecksum = datagram.split("\\*");
        String [] data = dataAndChecksum[0].split(",");
        if(!(calculateCheckSum(dataAndChecksum[0].concat("*")) == Integer.parseInt(dataAndChecksum[1], 16))){
            log.error("Wrong checksum for: " + datagram);
            return;
        }

        thresholdValue.setValue1(Double.parseDouble(data[2]));
        thresholdValue.setValue2(Double.parseDouble(data[4]));
        thresholdValue.setValue3(Double.parseDouble(data[6]));
        thresholdValue.setUnit1(data[3]);
        thresholdValue.setUnit2(data[5]);
        thresholdValue.setUnit3(data[7]);
        thresholdValueRepository.save(thresholdValue);
    }
}
