package com.mewa.model.entity;

import com.mewa.enums.TypeE;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
@Data
//endcja do bazy, zwiera te samo pola co tabela bazodanowa
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
    private Integer deviceId;
    @Enumerated(EnumType.STRING)
    private TypeE type;
}
