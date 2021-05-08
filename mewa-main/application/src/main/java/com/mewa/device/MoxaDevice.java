package com.mewa.device;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoxaDevice {
    private int id;
    private String ip;
    private String status;
    private int errorCode;
}
