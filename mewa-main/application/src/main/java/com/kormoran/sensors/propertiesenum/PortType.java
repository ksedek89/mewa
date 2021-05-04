package com.kormoran.sensors.propertiesenum;

public enum PortType {

    REAL,
    SYM;
    private static final long serialVersionUID = 1L;

    public String value() {
        return name();
    }

    public static PortType fromValue(String v) {
        return valueOf(v);
    }

}
