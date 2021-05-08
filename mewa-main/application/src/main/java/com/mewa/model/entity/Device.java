package com.mewa.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
@Data
public class Device{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer moxaNumber;
    private Integer moxaId;
    private String deviceType;
    private Integer directionAngle;
    private Integer thresholdPressure;
    private String active;
}
