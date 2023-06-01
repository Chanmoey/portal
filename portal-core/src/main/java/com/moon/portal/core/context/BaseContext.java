package com.moon.portal.core.context;

import io.netty.channel.ChannelHandlerContext;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class BaseContext implements IContext {

    /**
     * 转发协议
     */
    protected final String protocol;

    /**
     * 状态,volatile保证可见性
     */
    protected volatile int status = IContext.RUNNING;

    /**
     * Netty上下文
     */
    protected final ChannelHandlerContext nettyCtx;

    /**
     * Netty上下文参数
     */
    protected final Map<String, Object> attributes = new HashMap<>();

    /**
     * 异常
     */
    protected Throwable throwable;

    /**
     * 是否保持长连接
     */
    protected final boolean keepAlive;

    /**
     * 回调函数
     */
    protected List<Consumer<IContext>> completedCallBacks;

    /**
     * 资源是否已经释放
     */
    protected final AtomicBoolean releaseRequest = new AtomicBoolean(false);

    public BaseContext(String protocol, ChannelHandlerContext nettyCtx, boolean keepAlive) {
        this.protocol = protocol;
        this.nettyCtx = nettyCtx;
        this.keepAlive = keepAlive;
    }

    @Override
    public void setRunning() {
        status = IContext.RUNNING;
    }

    @Override
    public void setWritten() {
        status = IContext.WRITTEN;
    }

    @Override
    public void setCompleted() {
        status = IContext.COMPLETED;
    }

    @Override
    public void setTerminated() {
        status = IContext.TERMINATED;
    }

    @Override
    public boolean isRunning() {
        return status == IContext.RUNNING;
    }

    @Override
    public boolean isWritten() {
        return status == IContext.WRITTEN;
    }

    @Override
    public boolean isCompleted() {
        return status == IContext.COMPLETED;
    }

    @Override
    public boolean isTerminated() {
        return status == IContext.TERMINATED;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public void setResponse(Object response) {

    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ChannelHandlerContext getNettyCtx() {
        return this.nettyCtx;
    }

    @Override
    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    @Override
    public boolean isReleaseRequest() {
        return this.releaseRequest.get();
    }

    @Override
    public void setCompletedCallBack(Consumer<IContext> consumer) {
        if (completedCallBacks == null) {
            completedCallBacks = new ArrayList<>();
        }
        completedCallBacks.add(consumer);
    }

    @Override
    public void invokeCompletedCallBack() {
        if (completedCallBacks != null) {
            completedCallBacks.forEach(callback -> callback.accept(this));
        }
    }
}
