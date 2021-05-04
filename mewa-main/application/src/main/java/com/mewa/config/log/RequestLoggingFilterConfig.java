package com.mewa.config.log;

import com.mewa.config.filter.RequestAndResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public RequestAndResponseLoggingFilter logFilter() {
        return new RequestAndResponseLoggingFilter();
    }
}
