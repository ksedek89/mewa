package pl.keeit.mewa.test.gui.application.dto;


import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DirDeviceDto {
    private TextField powerValue;
    private TextField powerValueUnit;
    private TextField neutrons;
    private TextField initNeutrons;
    private CheckBox enabledCheckbox;
    private int id;
}
