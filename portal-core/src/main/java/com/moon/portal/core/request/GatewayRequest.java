package com.moon.portal.core.request;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.moon.portal.common.constants.BasicConst;
import com.moon.portal.common.utils.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class GatewayRequest implements IGatewayRequest {

    /**
     * 服务唯一ID
     */
    @Getter
    private final String uniqueId;

    /**
     * 进出网关的时间
     */
    @Getter
    private final long beginTime;
    @Getter
    private final long endTime;


    /**
     * 字符集
     */
    private final Charset charset;

    /**
     * 客户端的IP
     */
    @Getter
    private final String clientIp;

    /**
     * 服务端主机
     */
    @Getter
    private final String host;

    /**
     * 请求路径 /xxx/xx/x
     */
    @Getter
    private final String path;

    /**
     * 统一资源标识符 /XXX/XX/XX?key=value&key2=value
     */
    @Getter
    private final String uri;

    /**
     * 请求方式 POST GET PUT DELETE...
     */
    @Getter
    private final HttpMethod httpMethod;

    /**
     * 请求格式
     */
    @Getter
    private final HttpMethod contentType;

    /**
     * 请求头
     */
    @Getter
    private final HttpHeaders httpHeaders;

    /**
     * 参数解析器
     */
    @Getter
    private final QueryStringDecoder queryStringDecoder;

    /**
     * 是否是合法的http请求协议
     */
    @Getter
    private final FullHttpRequest fullHttpRequest;

    /**
     * 请求体
     */
    private String body;

    /**
     * Cookie
     */
    private Map<String, Cookie> cookieMap;

    /**
     * Post请求参数
     */
    private Map<String, List<String>> postParameters;

    /**
     * 可修改的Scheme， 默认为Http://
     */
    private String modifyScheme;

    /**
     * 可修改的主机名
     */
    private String modifyHost;

    /**
     * 可修改的请求路径
     */
    private String modifyPath;

    /**
     * 构建下游请求时的Http构建器
     */
    private final RequestBuilder requestBuilder;

    public GatewayRequest(String uniqueId,
                          long endTime,
                          Charset charset,
                          String clientIp,
                          String host,
                          String uri,
                          HttpMethod httpMethod,
                          HttpMethod contentType,
                          HttpHeaders httpHeaders,
                          QueryStringDecoder queryStringDecoder,
                          FullHttpRequest fullHttpRequest) {
        this.uniqueId = uniqueId;
        this.beginTime = TimeUtil.currentTimeMillis();
        this.endTime = endTime;
        this.charset = charset;
        this.clientIp = clientIp;
        this.httpMethod = httpMethod;
        this.contentType = contentType;
        this.httpHeaders = httpHeaders;
        this.queryStringDecoder = new QueryStringDecoder(uri, charset);
        this.fullHttpRequest = fullHttpRequest;
        this.requestBuilder = new RequestBuilder();
        this.host = host;
        this.path = queryStringDecoder.path();
        this.uri = uri;

        this.modifyHost = host;
        this.modifyPath = path;
        this.modifyScheme = BasicConst.HTTP_PREFIX_SEPARATOR;
        this.requestBuilder.setMethod(getHttpMethod().name());
        this.requestBuilder.setHeaders(getHttpHeaders());
        this.requestBuilder.setQueryParams(queryStringDecoder.parameters());

        ByteBuf contentBuffer = fullHttpRequest.content();
        if (Objects.nonNull(contentBuffer)) {
            this.requestBuilder.setBody(contentBuffer.nioBuffer());
        }
    }

    /**
     * 获取请求体
     *
     * @return body
     */
    public String getBody() {
        if (StringUtils.isEmpty(body)) {
            body = fullHttpRequest.content().toString(charset);
        }
        return body;
    }

    /**
     * 获取Cookie
     *
     * @param name name
     * @return cookie
     */
    public Cookie getCookie(String name) {
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
            String cookieStr = getHttpHeaders().get(HttpHeaderNames.COOKIE);
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.name(), cookie);
            }
        }

        return cookieMap.get(name);
    }

    /**
     * 获取指定名称的参数值
     *
     * @param name name
     * @return value
     */
    public List<String> getQueryParametersMultiple(String name) {
        return queryStringDecoder.parameters().get(name);
    }

    /**
     * 获取指定名称的参数值
     *
     * @param name name
     * @return value
     */
    public List<String> getPostParametersMultiple(String name) {
        String strBody = getBody();
        if (isFormPost()) {
            if (postParameters == null) {
                QueryStringDecoder paramDecoder = new QueryStringDecoder(strBody, false);
                postParameters = paramDecoder.parameters();
            } else if (postParameters.isEmpty()) {
                return null;
            } else {
                return postParameters.get(name);
            }
        } else if (isJsonPost()) {
            return Lists.newArrayList(JsonPath.read(strBody, name).toString());
        }
        return null;
    }

    private boolean isFormPost() {
        return HttpMethod.POST.equals(httpMethod)
                && (contentType.asciiName().startsWith(HttpHeaderValues.FORM_DATA.toString()) ||
                contentType.asciiName().startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()));
    }

    private boolean isJsonPost() {
        return HttpMethod.POST.equals(httpMethod) &&
                contentType.asciiName().startsWith(HttpHeaderValues.APPLICATION_JSON.toString());
    }

    @Override
    public void setModifyScheme(String scheme) {
        this.modifyScheme = scheme;
    }

    @Override
    public String getModifyScheme() {
        return this.modifyScheme;
    }

    @Override
    public void setModifyHost(String host) {
        this.modifyHost = host;
    }

    @Override
    public String getModifyHost() {
        return this.modifyHost;
    }

    @Override
    public void setModifyPath(String path) {
        this.modifyPath = path;
    }

    @Override
    public String getModifyPath() {
        return this.modifyPath;
    }

    @Override
    public void addHeader(CharSequence name, String value) {
        requestBuilder.addHeader(name, value);
    }

    @Override
    public void setHeader(CharSequence name, String value) {
        requestBuilder.setHeader(name, value);
    }

    @Override
    public void addQueryParam(String name, String value) {
        requestBuilder.addQueryParam(name, value);
    }

    @Override
    public void addFormParam(String name, String value) {
        if (isFormPost()) {
            requestBuilder.addFormParam(name, value);
        }
    }

    @Override
    public void addOrReplaceCookie(Cookie cookie) {
        requestBuilder.addOrReplaceCookie(cookie);
    }

    @Override
    public void setRequestTimeout(int requestTimeout) {
        requestBuilder.setRequestTimeout(requestTimeout);
    }

    @Override
    public String getFinalUrl() {
        return this.modifyScheme + this.modifyHost + this.modifyPath;
    }

    @Override
    public Request build() {
        return requestBuilder.setUrl(getFinalUrl()).build();
    }
}
