package com.pru.starter.rest.prv;

import com.pru.starter.rest.model.VersionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("VersionControllerPrv")
@RequestMapping("${pru.servlet.prv-api-path}")
public class VersionController {

    @Value("${app.version}")
    private final String version = null;

    @GetMapping(value = "/version", produces = "application/json")
    public VersionResponse getVersion()  {
        return new VersionResponse(version);
    }
}
