package pl.keeit.mewa.test.gui.application.dto;




import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class OxygenDeviceDto {
    private TextField oxygenValue;
    private TextField oxygenThreshold;
    private TextField co2Value;
    private TextField co2Threshold;
    private CheckBox enabledCheckbox;
    private int id;


}
