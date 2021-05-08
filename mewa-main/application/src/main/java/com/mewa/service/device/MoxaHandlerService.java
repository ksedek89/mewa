package com.mewa.service.device;

import com.mewa.device.MoxaDevice;
import com.mewa.service.MoxaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Slf4j
public class MoxaHandlerService {

    @Autowired
    MoxaService moxaService;

    @Async
    public void handleMoxaDevice(List<MoxaDevice> moxaDeviceList) throws Exception {
            List<Future<MoxaDevice>> futureList = new ArrayList<>();
            for(MoxaDevice moxaDevice : moxaDeviceList){
                futureList.add(moxaService.handleMoxaDevices(moxaDevice));
            }
            for(Future future: futureList){
                future.get();
            }
            moxaService.handleSiuFrame(moxaDeviceList);
    }
}
