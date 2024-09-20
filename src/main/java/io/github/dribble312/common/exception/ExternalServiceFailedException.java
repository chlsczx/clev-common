package io.github.dribble312.common.exception;

public class ExternalServiceFailedException extends Exception {

    public ExternalServiceFailedException() {
        super("Service failed.");
    }

    public ExternalServiceFailedException(String msg) {
        super(msg);
    }
}
