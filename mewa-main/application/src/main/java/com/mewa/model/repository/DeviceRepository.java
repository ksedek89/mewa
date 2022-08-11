package com.mewa.model.repository;

import com.mewa.model.entity.Device;
import com.mewa.model.entity.ThresholdValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//repozytorium. Taki interfejs springowy któ©y pozwala na pobieranie danych z bazy
public interface DeviceRepository extends JpaRepository<Device, Long> {
    //pobranie wszystkich urządzeń w których active się zgqadza
    List<Device> findAllByActiveEquals(String active);
}
