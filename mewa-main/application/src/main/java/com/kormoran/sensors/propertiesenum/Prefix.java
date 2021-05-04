package com.kormoran.sensors.propertiesenum;

public enum Prefix {
    n(0, "N"),
    u(1, "U"),
    m(2, "M");
    private static final long serialVersionUID = 1L;


    private int value;
    private String name;

    Prefix(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
