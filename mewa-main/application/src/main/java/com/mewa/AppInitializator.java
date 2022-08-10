package com.mewa;


import com.mewa.service.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
//klasa która uruchamia metodę init() inicujującą wszysktie urządzenia
class AppInitializator {

    @Autowired
    InitService initService;

    @PostConstruct
    private void init() throws Exception {
        log.info("Starting init services");
        //inicjacja wszystkich urządzeń
        initService.init();
    }
}
