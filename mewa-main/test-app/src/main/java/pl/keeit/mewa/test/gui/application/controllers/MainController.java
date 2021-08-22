package pl.keeit.mewa.test.gui.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;
import pl.keeit.PressureDto;
import pl.keeit.RequestDto;
import pl.keeit.mewa.test.gui.application.client.ActionHandler;
import pl.keeit.mewa.test.gui.application.dto.DirDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.DpoDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.MoxaDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.OxygenDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.PressureDeviceDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MainController {
    private ActionHandler actionHandler;


    //pressure - start
    @FXML private TextField pressureValue1;
    @FXML private TextField pressureValue2;
    @FXML private TextField pressureValue3;
    @FXML private TextField pressureValue4;
    @FXML private TextField pressureValue5;
    @FXML private TextField pressureValue6;
    @FXML private TextField pressureValue7;
    @FXML private TextField pressureValue8;
    @FXML private TextField pressureValue9;
    @FXML private TextField pressureValue10;
    @FXML private TextField pressureValue11;
    @FXML private TextField pressureValue12;
    @FXML private TextField pressureValue13;
    @FXML private TextField pressureValue14;
    @FXML private TextField pressureValue15;
    @FXML private TextField pressureValue16;

    @FXML private TextField pressureThresholdValue1;
    @FXML private TextField pressureThresholdValue2;
    @FXML private TextField pressureThresholdValue3;
    @FXML private TextField pressureThresholdValue4;
    @FXML private TextField pressureThresholdValue5;
    @FXML private TextField pressureThresholdValue6;
    @FXML private TextField pressureThresholdValue7;
    @FXML private TextField pressureThresholdValue8;
    @FXML private TextField pressureThresholdValue9;
    @FXML private TextField pressureThresholdValue10;
    @FXML private TextField pressureThresholdValue11;
    @FXML private TextField pressureThresholdValue12;
    @FXML private TextField pressureThresholdValue13;
    @FXML private TextField pressureThresholdValue14;
    @FXML private TextField pressureThresholdValue15;
    @FXML private TextField pressureThresholdValue16;

    @FXML private CheckBox pressureEnabled1;
    @FXML private CheckBox pressureEnabled2;
    @FXML private CheckBox pressureEnabled3;
    @FXML private CheckBox pressureEnabled4;
    @FXML private CheckBox pressureEnabled5;
    @FXML private CheckBox pressureEnabled6;
    @FXML private CheckBox pressureEnabled7;
    @FXML private CheckBox pressureEnabled8;
    @FXML private CheckBox pressureEnabled9;
    @FXML private CheckBox pressureEnabled10;
    @FXML private CheckBox pressureEnabled11;
    @FXML private CheckBox pressureEnabled12;
    @FXML private CheckBox pressureEnabled13;
    @FXML private CheckBox pressureEnabled14;
    @FXML private CheckBox pressureEnabled15;
    @FXML private CheckBox pressureEnabled16;

    @FXML private CheckBox checkAllPressure;

    @FXML private TextField pressureForEvery;
    @FXML private TextField pressureForSelected;
    @FXML private Button pressureForEveryButton;
    @FXML private Button pressureForSelectedButton;
    @FXML private Button clearAllPressureButton;
    @FXML private Button randomPressureForSelectedButton;

    @FXML private TextField pressureThresholdForEvery;
    @FXML private TextField pressureThresholdForSelected;
    @FXML private Button pressureThresholdForEveryButton;
    @FXML private Button pressureThresholdForSelectedButton;
    @FXML private Button clearAllPressureThresholdButton;
    @FXML private Button randomPressureThresholdForSelectedButton;

    @FXML private CheckBox checkRandomPressure;
    //pressure - end

    //oxygen - start
    @FXML private CheckBox oxygenEnabled1;
    @FXML private TextField oxygenValue1;
    @FXML private TextField oxygenThresholdValue1;
    @FXML private TextField co2Value1;
    @FXML private TextField co2ThresholdValue1;
    @FXML private Button clearAllOxygenButton;

    //oxygen - end

    //dpo - start
    @FXML private TextField dpoPowerValue1;
    @FXML private TextField dpoPowerUnitValue1;
    @FXML private TextField dpoPowerValue2;
    @FXML private TextField dpoPowerUnitValue2;
    @FXML private TextField dpoPowerValue3;
    @FXML private TextField dpoPowerUnitValue3;
    @FXML private TextField dpoTotalDosageValue1;
    @FXML private TextField dpoTotalDosageUnitValue1;
    @FXML private TextField dpoTotalDosageValue2;
    @FXML private TextField dpoTotalDosageUnitValue2;
    @FXML private TextField dpoTotalDosageValue3;
    @FXML private TextField dpoTotalDosageUnitValue3;

    @FXML private CheckBox dpoEnabled1;
    @FXML private CheckBox dpoEnabled2;
    @FXML private CheckBox dpoEnabled3;
    //dpo - end


    //dir - start
    @FXML private TextField dirPowerValue1;
    @FXML private TextField dirPowerValue2;
    @FXML private TextField dirPowerValue3;
    @FXML private TextField dirPowerValue4;
    @FXML private TextField dirPowerValue5;
    @FXML private TextField dirPowerUnit1;
    @FXML private TextField dirPowerUnit2;
    @FXML private TextField dirPowerUnit3;
    @FXML private TextField dirPowerUnit4;
    @FXML private TextField dirPowerUnit5;
    @FXML private TextField dirNeutronsValue1;
    @FXML private TextField dirNeutronsValue2;
    @FXML private TextField dirNeutronsValue3;
    @FXML private TextField dirNeutronsValue4;
    @FXML private TextField dirNeutronsValue5;
    @FXML private TextField dirInitNeutronsValue1;
    @FXML private TextField dirInitNeutronsValue2;
    @FXML private TextField dirInitNeutronsValue3;
    @FXML private TextField dirInitNeutronsValue4;
    @FXML private TextField dirInitNeutronsValue5;


    @FXML private CheckBox dirEnabled1;
    @FXML private CheckBox dirEnabled2;
    @FXML private CheckBox dirEnabled3;
    @FXML private CheckBox dirEnabled4;
    @FXML private CheckBox dirEnabled5;
    //dir -end
    //moxa - start
    @FXML private CheckBox moxaEnabled1;
    @FXML private CheckBox moxaEnabled2;
    @FXML private CheckBox moxaEnabled3;
    //moxa - end

    @FXML private Button sendButton;



    public MainController() {
        actionHandler = new ActionHandler();
    }

    public void sendButtonClicked(Event event) throws Exception{
        actionHandler.sendButtonClicked();
    }

    public void checkAllPressureEvent(Event event) {
        actionHandler.checkAllPressureEvent(checkAllPressure.isSelected());
    }

    public void pressureForEveryButtonEvent(Event event) {
        actionHandler.pressureForEveryButtonEvent(pressureForEvery.getText());
    }

    public void pressureForSelectedButtonEvent(Event event) {
        actionHandler.pressureForSelectedButtonEvent(pressureForSelected.getText());
    }

    public void clearAllPressureButtonEvent(Event event) {
        actionHandler.clearAllPressureButtonEvent();
    }

    public void randomPressureForSelectedButtonEvent(Event event) {
        actionHandler.randomPressureForSelectedButtonEvent();
    }

    public void pressureThresholdForEveryButtonEvent(Event event) {
        actionHandler.pressureThresholdForEveryButtonEvent(pressureThresholdForEvery.getText());
    }

    public void pressureThresholdForSelectedButtonEvent(Event event) {
        actionHandler.pressureThresholdForSelectedButtonEvent(pressureThresholdForSelected.getText());
    }

    public void clearAllPressureThresholdButtonEvent(Event event) {
        actionHandler.clearAllPressureThresholdButtonEvent();
    }

    public void randomPressureThresholdForSelectedButtonEvent(Event event) {
        actionHandler.randomPressureThresholdForSelectedButtonEvent();
    }

    public void clearAllOxygenButtonEvent(Event event) {
        actionHandler.clearAllOxygenButtonEvent();
    }




    @FXML
    public void initialize() {
        int pressureCounter = 1;

        actionHandler.setPressureDeviceList(Arrays.asList(
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue1).pressureValue(pressureValue1).enabledCheckbox(pressureEnabled1).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue2).pressureValue(pressureValue2).enabledCheckbox(pressureEnabled2).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue3).pressureValue(pressureValue3).enabledCheckbox(pressureEnabled3).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue4).pressureValue(pressureValue4).enabledCheckbox(pressureEnabled4).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue5).pressureValue(pressureValue5).enabledCheckbox(pressureEnabled5).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue6).pressureValue(pressureValue6).enabledCheckbox(pressureEnabled6).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue7).pressureValue(pressureValue7).enabledCheckbox(pressureEnabled7).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue8).pressureValue(pressureValue8).enabledCheckbox(pressureEnabled8).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue9).pressureValue(pressureValue9).enabledCheckbox(pressureEnabled9).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue10).pressureValue(pressureValue10).enabledCheckbox(pressureEnabled10).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue11).pressureValue(pressureValue11).enabledCheckbox(pressureEnabled11).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue12).pressureValue(pressureValue12).enabledCheckbox(pressureEnabled12).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue13).pressureValue(pressureValue13).enabledCheckbox(pressureEnabled13).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue14).pressureValue(pressureValue14).enabledCheckbox(pressureEnabled14).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue15).pressureValue(pressureValue15).enabledCheckbox(pressureEnabled15).id(pressureCounter++).build(),
            PressureDeviceDto.builder().pressureThreshold(pressureThresholdValue16).pressureValue(pressureValue16).enabledCheckbox(pressureEnabled16).id(pressureCounter++).build()
        ));
        actionHandler.setOxygenDeviceList(Arrays.asList(
            OxygenDeviceDto.builder().co2Threshold(co2ThresholdValue1).co2Value(co2Value1).oxygenThreshold(oxygenThresholdValue1).oxygenValue(oxygenValue1).enabledCheckbox(oxygenEnabled1).id(1).build()
        ));
        int dpoCounter = 1;
        actionHandler.setDpoDeviceList(Arrays.asList(
            DpoDeviceDto.builder().dpoPower(dpoPowerValue1).dpoPowerUnit(dpoPowerUnitValue1).dpoTotalDosage(dpoTotalDosageValue1).dpoTotalDosageUnit(dpoTotalDosageUnitValue1).enabledCheckbox(dpoEnabled1).id(dpoCounter++).build(),
            DpoDeviceDto.builder().dpoPower(dpoPowerValue2).dpoPowerUnit(dpoPowerUnitValue2).dpoTotalDosage(dpoTotalDosageValue2).dpoTotalDosageUnit(dpoTotalDosageUnitValue2).enabledCheckbox(dpoEnabled2).id(dpoCounter++).build(),
            DpoDeviceDto.builder().dpoPower(dpoPowerValue3).dpoPowerUnit(dpoPowerUnitValue3).dpoTotalDosage(dpoTotalDosageValue3).dpoTotalDosageUnit(dpoTotalDosageUnitValue3).enabledCheckbox(dpoEnabled3).id(dpoCounter++).build()
        ));
        int dirCounter = 1;
        actionHandler.setDirDeviceList(Arrays.asList(
            DirDeviceDto.builder().powerValue(dirPowerValue1).powerValueUnit(dirPowerUnit1).neutrons(dirNeutronsValue1).initNeutrons(dirInitNeutronsValue1).enabledCheckbox(dirEnabled1).id(dirCounter++).build(),
            DirDeviceDto.builder().powerValue(dirPowerValue2).powerValueUnit(dirPowerUnit2).neutrons(dirNeutronsValue2).initNeutrons(dirInitNeutronsValue2).enabledCheckbox(dirEnabled2).id(dirCounter++).build(),
            DirDeviceDto.builder().powerValue(dirPowerValue3).powerValueUnit(dirPowerUnit3).neutrons(dirNeutronsValue3).initNeutrons(dirInitNeutronsValue3).enabledCheckbox(dirEnabled3).id(dirCounter++).build(),
            DirDeviceDto.builder().powerValue(dirPowerValue4).powerValueUnit(dirPowerUnit4).neutrons(dirNeutronsValue4).initNeutrons(dirInitNeutronsValue4).enabledCheckbox(dirEnabled4).id(dirCounter++).build(),
            DirDeviceDto.builder().powerValue(dirPowerValue5).powerValueUnit(dirPowerUnit5).neutrons(dirNeutronsValue5).initNeutrons(dirInitNeutronsValue5).enabledCheckbox(dirEnabled5).id(dirCounter++).build()
        ));
        int moxaCounter = 1;
        actionHandler.setMoxaDeviceList(Arrays.asList(
            MoxaDeviceDto.builder().enabledCheckbox(moxaEnabled1).id(moxaCounter++).build(),
            MoxaDeviceDto.builder().enabledCheckbox(moxaEnabled2).id(moxaCounter++).build(),
            MoxaDeviceDto.builder().enabledCheckbox(moxaEnabled3).id(moxaCounter++).build()
        ));
        actionHandler.initalize();

    }


}
