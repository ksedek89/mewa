package pl.keeit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirDto {
    private int id;
    private String dirPower;
    private String dirPowerUnit;
    private String dirNeutrons;
    private String dirInitNeutrons;



}
