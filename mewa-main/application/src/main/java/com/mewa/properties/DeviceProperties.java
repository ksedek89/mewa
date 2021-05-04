package com.mewa.properties;

import com.mewa.dto.DeviceDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(value = "moxa")
public class DeviceProperties {
    private List<DeviceDto> devices;
}
