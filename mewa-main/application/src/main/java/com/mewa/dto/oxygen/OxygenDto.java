package com.mewa.dto.oxygen;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OxygenDto {
    private List<InputDto> input;
}
