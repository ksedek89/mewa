package com.mewa.model.repository;

import com.mewa.model.entity.ThresholdValue;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.Table;


public interface ThresholdValueRepository extends JpaRepository<ThresholdValue, Long> {
}
