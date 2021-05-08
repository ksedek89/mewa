package com.mewa.properties;

import com.mewa.dto.MoxaDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(value = "moxa.init")
public class MoxaProperties {
    private List<MoxaDto> configuration;
}
