package pl.keeit.mewa.test.gui.application.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import pl.keeit.DirDto;
import pl.keeit.DpoDto;
import pl.keeit.FilterDto;
import pl.keeit.MoxaDto;
import pl.keeit.OxygenDto;
import pl.keeit.PressureDto;
import pl.keeit.RequestDto;
import pl.keeit.mewa.test.gui.application.dto.DirDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.DpoDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.FilterDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.MoxaDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.OxygenDeviceDto;
import pl.keeit.mewa.test.gui.application.dto.PressureDeviceDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Data
public class ActionHandler {
    private final static String MEWA_URL = "http://localhost:8085/mewa/pressure";

    private ObjectMapper objectMapper;
    private HttpClient client;
    private List<PressureDeviceDto> pressureDeviceList;
    private List<OxygenDeviceDto> oxygenDeviceList;
    private List<DpoDeviceDto> dpoDeviceList;
    private List<DirDeviceDto> dirDeviceList;
    private List<MoxaDeviceDto> moxaDeviceList;
    private List<FilterDeviceDto> filterDeviceList;

    public ActionHandler() {
        objectMapper = new ObjectMapper();
        client = HttpClient.newBuilder().build();
    }

    public void sendButtonClicked() throws Exception {
        RequestDto requestObject = RequestDto
            .builder()
            .pressureDevices(pressureDeviceList
                .stream()
                .filter(e->e.getEnabledCheckbox().isSelected())
                .map(e-> PressureDto.builder().threshold(Integer.valueOf(e.getPressureThreshold().getText()))
                    .pressure(Integer.valueOf(e.getPressureValue().getText()))
                    .id(e.getId())
                    .build()).collect(Collectors.toList()))
            .oxygenDevices(oxygenDeviceList
                .stream()
                .filter(e->e.getEnabledCheckbox().isSelected())
                .map(e-> OxygenDto
                    .builder()
                    .co2(e.getCo2Value().getText())
                    .co2Threshold(e.getCo2Threshold().getText())
                    .oxygen(e.getOxygenValue().getText())
                    .oxygenThreshold(e.getOxygenThreshold().getText())
                    .id(e.getId())
                    .build())
                .collect(Collectors.toList()))
            .dpoDevices(dpoDeviceList
                .stream()
                .filter(e->e.getEnabledCheckbox().isSelected())
                .map(e-> DpoDto
                    .builder()
                    .dpoPower(e.getDpoPower().getText())
                    .dpoPowerUnit(e.getDpoPowerUnit().getText())
                    .dpoTotalDosage(e.getDpoTotalDosage().getText())
                    .dpoTotalDosageUnit(e.getDpoTotalDosageUnit().getText())
                    .id(e.getId())
                    .build())
                .collect(Collectors.toList()))
            .dirDevices(dirDeviceList
                .stream()
                .filter(e->e.getEnabledCheckbox().isSelected())
                .map(e-> DirDto
                    .builder()
                    .dirPower(e.getPowerValue().getText())
                    .dirPowerUnit(e.getPowerValueUnit().getText())
                    .dirNeutrons(e.getNeutrons().getText())
                    .dirInitNeutrons(e.getInitNeutrons().getText())
                    .id(e.getId())
                    .build())
                .collect(Collectors.toList()))
            .filterDevices(filterDeviceList
            .stream()
            .filter(e->e.getFilterEnabled().isSelected())
            .map(e-> FilterDto
                .builder()
                .efficiency(e.getFilterEfficiency().getText())
                .resistance(e.getFilterResistance().getText())
                .initialResistance(e.getFilterInitialResistance().getText())
                .id(e.getId())
                .build()).collect(Collectors.toList()))
            .moxaDevices(moxaDeviceList.stream().map(e-> MoxaDto.builder().enabled(e.getEnabledCheckbox().isSelected()).id(e.getId()).build()).collect(Collectors.toList()))
            .build();
        String requestBody = objectMapper.writeValueAsString(requestObject);
        System.out.println(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(MEWA_URL))
            .timeout(Duration.ofSeconds(5))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public void initalize() {
        pressureDeviceList.stream().forEach(e->{
                e.getPressureThreshold().setText("0");
                e.getPressureValue().setText("0");
            }
        );
        oxygenDeviceList.stream().forEach(e->{
                e.getCo2Threshold().setText("0");
                e.getCo2Value().setText("0");
                e.getOxygenValue().setText("0");
                e.getOxygenThreshold().setText("0");
                e.getEnabledCheckbox().setSelected(false);
            }
        );
        dpoDeviceList.stream().forEach(e->{
            e.getDpoPower().setText("0");
            e.getDpoPowerUnit().setText("n");
            e.getDpoTotalDosage().setText("0");
            e.getDpoTotalDosageUnit().setText("n");
        });
        dirDeviceList.stream().forEach(e->{
            e.getInitNeutrons().setText("0");
            e.getNeutrons().setText("0");
            e.getPowerValue().setText("0");
            e.getPowerValueUnit().setText("n");
        });
        moxaDeviceList.stream().forEach(e->{
            e.getEnabledCheckbox().setSelected(true);
        });
        filterDeviceList.stream().forEach(e->{
            e.getFilterEfficiency().setText("0");
            e.getFilterResistance().setText("0");
            e.getFilterInitialResistance().setText("0");
            e.getFilterEnabled().setSelected(false);
        });
    }

    public void checkAllPressureEvent(boolean selected) {
        pressureDeviceList.stream().forEach(e->e.getEnabledCheckbox().setSelected(selected));
    }

    public void pressureForEveryButtonEvent(String text) {
        pressureDeviceList.stream().forEach(e->e.getPressureValue().setText(text));
    }

    public void pressureForSelectedButtonEvent(String text) {
        pressureDeviceList.stream().filter(e->e.getEnabledCheckbox().isSelected()).forEach(e->e.getPressureValue().setText(text));
    }

    public void clearAllPressureButtonEvent() {
        pressureDeviceList.stream().forEach(e->e.getPressureValue().setText("0"));
    }

    public void randomPressureForSelectedButtonEvent() {
        pressureDeviceList.stream().filter(e->e.getEnabledCheckbox().isSelected()).forEach(e->e.getPressureValue().setText(String.valueOf(new Random().nextInt(10000))));
    }

    public void pressureThresholdForEveryButtonEvent(String text) {
        pressureDeviceList.stream().forEach(e->e.getPressureThreshold().setText(text));
    }

    public void pressureThresholdForSelectedButtonEvent(String text) {
        pressureDeviceList.stream().filter(e->e.getEnabledCheckbox().isSelected()).forEach(e->e.getPressureThreshold().setText(text));
    }

    public void clearAllPressureThresholdButtonEvent() {
        pressureDeviceList.stream().forEach(e->e.getPressureThreshold().setText("0"));
    }

    public void randomPressureThresholdForSelectedButtonEvent() {
        pressureDeviceList.stream().filter(e->e.getEnabledCheckbox().isSelected()).forEach(e->e.getPressureThreshold().setText(String.valueOf(new Random().nextInt(10000))));
    }

    public void clearAllOxygenButtonEvent() {
        oxygenDeviceList.stream().forEach(e->{
            e.getOxygenValue().setText("0");
            e.getOxygenThreshold().setText("0");
            e.getCo2Value().setText("0");
            e.getCo2Threshold().setText("0");

        });
    }
}
