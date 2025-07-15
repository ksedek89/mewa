package com.mewa.model.repository;

import com.mewa.model.entity.ThresholdValue;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Table;


//repozytorium. Taki interfejs springowy któ©y pozwala na pobieranie danych z bazy
public interface ThresholdValueRepository extends JpaRepository<ThresholdValue, Long> {
}
