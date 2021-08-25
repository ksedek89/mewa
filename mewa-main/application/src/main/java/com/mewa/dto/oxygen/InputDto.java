package com.mewa.dto.oxygen;

import lombok.Data;

import java.util.List;

@Data
public class InputDto {
    private String mode;
    private List<VarDto> var;
}
