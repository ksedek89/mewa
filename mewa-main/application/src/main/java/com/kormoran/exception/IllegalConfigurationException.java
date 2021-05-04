package com.kormoran.exception;

public class IllegalConfigurationException extends Throwable {
    private static final long serialVersionUID = 1L;

    public IllegalConfigurationException(Throwable throwable) {
        super(throwable);
    }

    public IllegalConfigurationException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public IllegalConfigurationException(String string) {
        super(string);
    }

    public IllegalConfigurationException() {
        super();
    }
}
