package com.moon.portal.core.context;

import com.moon.portal.common.rule.Rule;
import com.moon.portal.common.utils.AssertUtil;
import com.moon.portal.core.request.GatewayRequest;
import com.moon.portal.core.response.GatewayResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class GatewayContext extends BaseContext {

    private GatewayRequest request;

    private GatewayResponse response;

    private Rule rule;

    public GatewayContext(String protocol, ChannelHandlerContext nettyCtx, boolean keepAlive,
                          GatewayRequest request, Rule rule) {
        super(protocol, nettyCtx, keepAlive);
        this.request = request;
        this.rule = rule;
    }

    /**
     * 获取必要上下文参数
     *
     * @param key key
     * @param <T> T
     * @return value
     */
    public <T> T getRequestAttribute(String key) {
        @SuppressWarnings("unchecked")
        T value = (T) getAttribute(key);
        AssertUtil.nonNull(value, "缺乏必要参数");
        return value;
    }

    /**
     * 获取必要上下文参数
     *
     * @param key key
     * @param <T> T
     * @return value
     */
    @SuppressWarnings("unchecked")
    public <T> T getRequestAttribute(String key, T defaultValue) {
        return (T) attributes.getOrDefault(key, defaultValue);
    }

    /**
     * 获取规则过滤器
     *
     * @param filterId filterId
     * @return filter
     */
    public Rule.FilterConfig getFilterConfig(String filterId) {
        return rule.getFilterConfig(filterId);
    }

    public String getUniqueId() {
        return request.getUniqueId();
    }

    /**
     * 释放资源
     */
    @Override
    public boolean releaseRequest() {
        if (requestRelease.compareAndSet(false, true)) {
            ReferenceCountUtil.release(request.getFullHttpRequest());
        }
        return true;
    }

    /**
     * 获取原始请求对象的方法
     *
     * @return request
     */
    public GatewayRequest getOriginRequest() {
        return request;
    }

    @Override
    public GatewayRequest getRequest() {
        return request;
    }

    public void setRequest(GatewayRequest request) {
        this.request = request;
    }

    @Override
    public GatewayResponse getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = (GatewayResponse) response;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public static class Builder {
        private String protocol;

        private ChannelHandlerContext nettyCtx;

        private GatewayRequest request;

        private Rule rule;

        private boolean keepAlive;

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setNettyCtx(ChannelHandlerContext nettyCtx) {
            this.nettyCtx = nettyCtx;
            return this;
        }

        public Builder setRequest(GatewayRequest request) {
            this.request = request;
            return this;
        }

        public Builder setRule(Rule rule) {
            this.rule = rule;
            return this;
        }

        public Builder setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public GatewayContext build() {
            AssertUtil.nonNull(protocol, "protocol 不能为空");
            AssertUtil.nonNull(nettyCtx, "nettyCtx 不能为空");
            AssertUtil.nonNull(rule, "rule 不能为空");
            AssertUtil.nonNull(request, "request 不能为空");
            return new GatewayContext(protocol, nettyCtx, keepAlive, request, rule);
        }
    }
}
