package com.mewa.dto.oxygen;

import lombok.Data;

import java.util.List;

@Data
//dto dla sondy co2/02, do mapowania wartości z jsona na java
public class InputDto {
    private String mode;
    private List<VarDto> var;
}
