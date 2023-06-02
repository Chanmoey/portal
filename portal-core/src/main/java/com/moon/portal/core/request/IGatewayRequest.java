package com.moon.portal.core.request;

import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.Request;

/**
 * 提供可修改的Request参数操作接口
 *
 * @author Chanmoey
 * @date 2023年06月02日
 */
public interface IGatewayRequest {

    public void setModifyScheme(String scheme);

    public String getModifyScheme();

    /**
     * 修改目标服务主机
     *
     * @param host host
     */
    void setModifyHost(String host);

    /**
     * 获取目标服务主机地址
     *
     * @return host
     */
    String getModifyHost();

    /**
     * 设置目标服务的访问路径
     *
     * @param path path
     */
    void setModifyPath(String path);

    /**
     * 获取目标服务的访问路径
     *
     * @return path
     */
    String getModifyPath();

    /**
     * 添加请求头
     *
     * @param name  name
     * @param value value
     */
    void addHeader(CharSequence name, String value);

    /**
     * 设置请求头
     *
     * @param name  name
     * @param value value
     */
    void setHeader(CharSequence name, String value);

    /**
     * 添加URL请求参数
     *
     * @param name  name
     * @param value value
     */
    void addQueryParam(String name, String value);

    /**
     * 添加表单请求参数
     *
     * @param name  name
     * @param value value
     */
    void addFormParam(String name, String value);

    /**
     * 添加或者替换Cookie
     *
     * @param Cookie cookie
     */
    void addOrReplaceCookie(Cookie cookie);

    /**
     * 设置超时时间
     *
     * @param requestTimeout 超时时间
     */
    void setRequestTimeout(int requestTimeout);

    /**
     * 获取最终的请求路径，包含请求参数
     * 例如：Http://localhost:8080/api/admin?name=root
     *
     * @return url
     */
    String getFinalUrl();

    Request build();
}
