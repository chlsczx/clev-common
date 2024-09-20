package io.github.dribble312.common.web.enums;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HttpResultCode {

    OK(0, HttpStatus.OK),

    WARN(300, HttpStatus.OK),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST),

    NOT_FOUND(404, HttpStatus.NOT_FOUND),

    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED),

    FORBIDDEN(403, HttpStatus.FORBIDDEN),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;

    private final transient HttpStatus httpStatus;

    HttpResultCode(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

}
