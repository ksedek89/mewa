package com.mewa.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private Integer moxaNumber;
    private Integer moxaId;
    private String deviceType;
    private Integer thresholdPressure;
    private Integer directionAngle;
}
