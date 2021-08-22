package pl.keeit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private List<PressureDto> pressureDevices;
    private List<OxygenDto> oxygenDevices;
    private List<DpoDto> dpoDevices;
    private List<DirDto> dirDevices;
    private List<MoxaDto> moxaDevices;
    private List<FilterDto> filterDevices;
}
