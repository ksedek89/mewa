package pl.keeit.mewa.test.gui.application.dto;


import javafx.scene.control.CheckBox;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoxaDeviceDto {
    private CheckBox enabledCheckbox;
    private int id;
}
