package io.github.dribble312.common.exception.unchecked;

public class BadRequestRtException extends RuntimeException {
    public BadRequestRtException() {
        super("Unsupported type, please check again.");
    }

    public BadRequestRtException(String msg) {
        super(msg);
    }
}
