package com.kormoran.sensors.propertiesenum;


    public enum DeviceType{
        PRESS,
        RAD,
        CO2,
        DIR;
    private static final long serialVersionUID = 1L;

    public String value() {
            return name();
        }

        public static DeviceType fromValue(String v) {
            return valueOf(v);
        }

    }
