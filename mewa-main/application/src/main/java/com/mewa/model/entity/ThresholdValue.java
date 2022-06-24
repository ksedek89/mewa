package com.mewa.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "THRESHOLD_VALUE")
@Data
public class ThresholdValue{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private long value1;
    private long value2;
    private long value3;
    private String unit1;
    private String unit2;
    private String unit3;
}
