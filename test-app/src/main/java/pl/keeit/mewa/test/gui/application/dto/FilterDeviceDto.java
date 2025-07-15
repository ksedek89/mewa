package pl.keeit.mewa.test.gui.application.dto;


import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FilterDeviceDto {
    private int id;
    private CheckBox filterEnabled;
    private TextField filterEfficiency;
    private TextField filterInitialResistance;
    private TextField filterResistance;

}
