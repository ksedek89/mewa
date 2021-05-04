package com.kormoran.exception;

public class WrongChecksumException extends Throwable {
    @SuppressWarnings("compatibility:-5906028870699231276")
    private static final long serialVersionUID = 1L;

    public WrongChecksumException(Throwable throwable) {
        super(throwable);
    }

    public WrongChecksumException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public WrongChecksumException(String string) {
        super(string);
    }

    public WrongChecksumException() {
        super();
    }
}
