package com.mewa.service;

import com.mewa.device.DirectionDevice;
import com.mewa.device.PressureDevice;
import com.mewa.dto.DeviceDto;
import com.mewa.model.entity.ThresholdValue;
import com.mewa.model.repository.ThresholdValueRepository;
import com.mewa.properties.DeviceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
}
