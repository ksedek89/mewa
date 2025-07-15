package pl.keeit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DpoDto {
    private int id;
    private String dpoPower;
    private String dpoPowerUnit;
    private String dpoTotalDosage;
    private String dpoTotalDosageUnit;


}
