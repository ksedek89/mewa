package com.mewa.device;

import com.mewa.enums.TypeE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoxaDevice {
    private int id;
    private String ip;
    private String status;
    private TypeE type;
}
