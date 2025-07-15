package pl.keeit.mewa.test.gui.application.dto;




import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DpoDeviceDto {
    private TextField dpoPower;
    private TextField dpoPowerUnit;
    private TextField dpoTotalDosage;
    private TextField dpoTotalDosageUnit;
    private CheckBox enabledCheckbox;
    private int id;


}
