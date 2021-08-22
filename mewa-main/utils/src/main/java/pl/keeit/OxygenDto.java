package pl.keeit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OxygenDto {
    private int id;
    private String oxygen;
    private String oxygenThreshold;
    private String co2;
    private String co2Threshold;


}
