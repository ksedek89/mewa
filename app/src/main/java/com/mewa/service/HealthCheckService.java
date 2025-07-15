package com.mewa.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data
//sprawdzenie dostępnych wątków, monitoring
public class HealthCheckService {
    @Qualifier("taskExecutor")
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Scheduled(cron = "0 * * * * *")
    public void healthCheck(){
        log.debug("All thread active count thread:" + Thread.activeCount());
    }
}
