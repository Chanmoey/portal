package com.moon.portal.core.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moon.portal.common.enums.ResponseCode;
import com.moon.portal.common.utils.JSONUtil;
import io.netty.handler.codec.http.*;
import lombok.Setter;
import org.asynchttpclient.Response;


/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class GatewayResponse {

    /**
     * 默认响应头
     */
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    /**
     * 额外的响应头
     */
    private HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();

    /**
     * 响应内容
     */
    @Setter
    private String content;

    /**
     * 返回响应状态码
     */
    @Setter
    private HttpResponseStatus httpResponseStatus;

    /**
     * 异步返回对象
     */
    @Setter
    private Response futureResponse;

    public GatewayResponse() {

    }

    /**
     * 设置请求头
     *
     * @param key   k
     * @param value v
     */
    public void putHeader(CharSequence key, CharSequence value) {
        responseHeaders.add(key, value);
    }

    /**
     * 构建异步响应对象
     *
     * @param futureResponse futureResponse
     * @return GatewayResponse
     */
    public static GatewayResponse buildGatewayResponse(Response futureResponse) {
        GatewayResponse response = new GatewayResponse();
        response.setFutureResponse(futureResponse);
        response.setHttpResponseStatus(HttpResponseStatus.valueOf(futureResponse.getStatusCode()));
        return response;
    }

    /**
     * 构建错误响应对象
     *
     * @param code code
     * @return GatewayResponse
     */
    public static GatewayResponse buildGatewayResponse(ResponseCode code, Object... args) {

        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, code.getStatus().code());
        objectNode.put(JSONUtil.CODE, code.getCode());
        objectNode.put(JSONUtil.MESSAGE, code.getMessage());

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(code.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(JSONUtil.toJONString(objectNode));
        return response;
    }

    /**
     * 构建成功响应对象
     *
     * @param data data
     * @return GatewayResponse
     */
    public static GatewayResponse buildGatewayResponse(Object data) {

        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS, ResponseCode.SUCCESS.getStatus().code());
        objectNode.put(JSONUtil.CODE, ResponseCode.SUCCESS.getCode());
        objectNode.put(JSONUtil.MESSAGE, ResponseCode.SUCCESS.getMessage());
        objectNode.putPOJO(JSONUtil.DATA, data);

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(JSONUtil.toJONString(objectNode));
        return response;
    }
}
