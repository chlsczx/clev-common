package io.github.dribble312.common.web.utils;

import io.github.dribble312.common.web.enums.HttpResultCode;
import io.github.dribble312.common.web.vo.HttpResult;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * HttpResults contains a bunch of static utilities method which are to be used to simplify the construction of
 * {@link ResponseEntity} based on that {@link HttpResult} has generally same internal type as {@link ResponseEntity}
 */
public class HttpResultUtils {

    public static <T> HttpResult<T> serverError(String msg, T data) {
        return HttpResult.of(
                HttpResultCode.INTERNAL_SERVER_ERROR,
                Objects.requireNonNullElse(msg, "An unexpected error occurred. Please try again later."),
                data
        );
    }

    public static HttpResult<?> serverError() {
        return serverError(null, null);
    }

    /**
     * HttpRequestBody caused error
     *
     * @return HttpResult
     */
    public static HttpResult<String> serverError(String msg) {
        return serverError(msg, null);
    }

    /**
     * Server recognize the request as bad, can not proceed.
     * @param msg msg
     * @param data data
     * @return HttpResult
     * @param <T> data T
     */
    public static <T> HttpResult<T> badRequest(String msg, T data) {
        return HttpResult.of(
                HttpResultCode.BAD_REQUEST, Objects.requireNonNullElse(msg, "Bad request."), data
        );
    }

    public static HttpResult<Void> badRequest() {
        return badRequest(null, null);
    }

    public static HttpResult<Void> badRequest(String msg) {
        return badRequest(msg, null);
    }

    public static <T> HttpResult<T> badRequest(T data) {
        return badRequest("Bad request.", data);
    }

    /**
     * HttpRequestBody succeed
     *
     * @param msg  msg
     * @param data data
     * @param <T>  type
     *
     * @return HttpResult
     */
    public static <T> HttpResult<T> ok(String msg, T data) {
        return HttpResult.of(HttpResultCode.OK, msg, data);
    }

    /**
     * HttpRequestBody succeed
     *
     * @return HttpResult
     */
    public static HttpResult<Void> ok() {
        return HttpResult.of(HttpResultCode.OK, "succeed", null);
    }

    /**
     * HttpRequestBody mentioned data not found
     *
     * @param msg  msg
     * @param data data
     * @param <T>  type
     *
     * @return HttpResult
     */
    public static <T> HttpResult<T> notFound(String msg, T data) {
        return HttpResult.of(HttpResultCode.NOT_FOUND, msg, data);
    }

    public static HttpResult<Void> notFound() {
        return HttpResult.of(HttpResultCode.NOT_FOUND, "not found", null);
    }

    public static <T> HttpResult<T> notFound(T data) {
        return HttpResult.of(HttpResultCode.NOT_FOUND, "not found", data);
    }

    /**
     * HttpRequestBody should be warned
     *
     * @param msg  msg
     * @param data data
     * @param <T>  type
     *
     * @return HttpResult
     */
    public static <T> HttpResult<T> warn(String msg, T data) {
        return HttpResult.of(HttpResultCode.WARN, msg, data);
    }

    /**
     * Shorthand for success
     *
     * @param data data
     * @param <T>  type
     *
     * @return HttpResult
     */
    public static <T> HttpResult<T> ok(T data) {
        return ok("success", data);
    }

    /**
     * Convert a {@code HttpResult} to {@code ResponseEntity}
     *
     * @param httpResult httpResult
     * @param <T>        httpResult's data type
     *
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<HttpResult<T>> asResponseEntity(HttpResult<T> httpResult) {
        return ResponseEntity.status(httpResult.getCode().getHttpStatus()).body(httpResult);
    }

    /**
     * From boolean to gen {@code HttpResult}
     *
     * @param bool boolean
     *
     * @return {@code HttpResult}
     */
    public static HttpResult<Void> fromBoolean(boolean bool) {
        return bool ? ok(null) : notFound(null);
    }

    /**
     * From boolean to gen {@code HttpResult}
     *
     * @param bool boolean
     * @param data data to attach
     *
     * @return {@code HttpResult}
     */
    public static <T> HttpResult<T> fromBoolean(boolean bool, T data) {
        return bool ? ok(data) : notFound(data);
    }

    /**
     * From data to gen {@code HttpResult}
     *
     * @param data data
     *
     * @return {@code HttpResult}
     */
    public static <T> HttpResult<T> fromData(T data) {
        if (ObjectUtils.isEmpty(data)) {
            return notFound(null);
        }

        return ok(data);
    }

    /**
     * From number to gen {@code HttpResult}
     *
     * @param num number
     *
     * @return {@code HttpResult}
     */
    public static HttpResult<Number> fromNumber(Number num) {
        if (ObjectUtils.isEmpty(num) && num.doubleValue() != 0) {
            return ok(num);
        }

        return notFound(num);
    }

}
