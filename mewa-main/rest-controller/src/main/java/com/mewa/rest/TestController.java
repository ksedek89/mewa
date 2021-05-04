package com.mewa.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class TestController {



    @GetMapping
    public void test(){
        System.out.println("test");
    }
}
