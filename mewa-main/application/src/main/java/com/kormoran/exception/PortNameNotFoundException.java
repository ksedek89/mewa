package com.kormoran.exception;

public class PortNameNotFoundException extends Throwable {
    private static final long serialVersionUID = 1L;

    public PortNameNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public PortNameNotFoundException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public PortNameNotFoundException(String string) {
        super(string);
    }

    public PortNameNotFoundException() {
        super();
    }
}
