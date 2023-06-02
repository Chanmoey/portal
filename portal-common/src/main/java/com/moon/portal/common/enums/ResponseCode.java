package com.moon.portal.common.enums;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public enum ResponseCode {

    SUCCESS(200, HttpResponseStatus.OK, "请求成功"),

    SERVER_ERROR(500, HttpResponseStatus.INTERNAL_SERVER_ERROR, "服务器异常");

    @Getter
    private final int code;

    @Getter
    private final HttpResponseStatus status;

    @Getter
    private final String message;

    ResponseCode(int code, HttpResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
