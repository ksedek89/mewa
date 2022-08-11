package com.mewa.dto.oxygen;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
//dto dla sondy co2/02, do mapowania wartości z jsona na java
public class OxygenDto {
    private List<InputDto> input;
}
