package com.mewa.model.repository;

import com.mewa.model.entity.Device;
import com.mewa.model.entity.ThresholdValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByActiveEquals(String active);
}
