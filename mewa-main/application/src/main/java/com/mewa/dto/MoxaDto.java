package com.mewa.dto;

import lombok.Data;

@Data
//urządzenie typu moxa, posiada adres ip i unikalny id
public class MoxaDto {
    private Integer id;
    private String ip;
}
