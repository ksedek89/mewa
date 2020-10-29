package com.pru.starter.rest.pub;

import com.pru.starter.rest.model.VersionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${pru.servlet.pub-api-path}")
public class VersionControllerPub {

    @Value("${app.version}")
    private String version = null;

    @GetMapping(value = "/version", produces = "application/json")
    public VersionResponse getVersion()  {
        return new VersionResponse(version);
    }
}
