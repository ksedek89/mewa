package com.mewa;

import com.mewa.properties.MoxaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({MoxaProperties.class})
@EnableAsync
/*
Główna klasa uruchomieniowa. Spring inicjuje wiele rzeczy m.in tworzy controllery, serwisy i repozytoria,
*/
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
