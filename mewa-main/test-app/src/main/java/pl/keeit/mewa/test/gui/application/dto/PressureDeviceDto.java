package pl.keeit.mewa.test.gui.application.dto;


import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PressureDeviceDto {
    private TextField pressureValue;
    private TextField pressureThreshold;
    private CheckBox enabledCheckbox;
    private int id;
}
