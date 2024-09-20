package io.github.dribble312.common.web.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.dribble312.common.web.enums.HttpResultCode;
import io.github.dribble312.common.web.utils.HttpResultUtils;
import lombok.*;
import org.springframework.http.ResponseEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author dribble312
 */
@Getter
@Value
@ToString
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Result code for http request
     */
    @NonNull
    HttpResultCode code;

    /**
     * Message for request
     */
    String msg;

    /**
     * Result data
     */
    T data;


    /**
     * Make this as {@code ResponseEntity}'s body, return a {@code ResponseEntity}.
     *
     * @return {@code ResponseEntity}
     */
    public ResponseEntity<HttpResult<T>> toResponseEntity() {
        return HttpResultUtils.asResponseEntity(this);
    }
}
