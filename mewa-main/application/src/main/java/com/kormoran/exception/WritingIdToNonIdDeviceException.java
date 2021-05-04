package com.kormoran.exception;

public class WritingIdToNonIdDeviceException extends Throwable {
    private static final long serialVersionUID = 1L;

    public WritingIdToNonIdDeviceException(Throwable throwable) {
        super(throwable);
    }

    public WritingIdToNonIdDeviceException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public WritingIdToNonIdDeviceException(String string) {
        super(string);
    }

    public WritingIdToNonIdDeviceException() {
        super();
    }
}
