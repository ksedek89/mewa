package com.mewa.dto.oxygen;

import lombok.Data;

@Data
//dto dla sondy co2/02, do mapowania wartości z jsona na java
public class VarDto {
    private Double v;
    private String u;
}
