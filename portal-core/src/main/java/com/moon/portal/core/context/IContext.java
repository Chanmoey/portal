package com.moon.portal.core.context;

import io.netty.channel.ChannelHandlerContext;

import java.util.function.Consumer;

/**
 * @author Chanmoey
 * @date 2023年06月01日
 */
public interface IContext {

    /**
     * 上下文生命周期: 运行中
     */
    int RUNNING = 1;

    /**
     * 上下文生命周期: 运行中，发生错误，对其进行标记，告诉请求已经结束，需要返回客户端
     */
    int WRITTEN = 0;

    /**
     * 上下文生命周期: 表示写回成功，防止并发下的多次写回
     */
    int COMPLETED = 2;

    /**
     * 上下文生命周期: 表示网关请求结束
     */
    int TERMINATED = 3;

    /**
     * 设置上下文状态为运行中
     */
    void setRunning();

    /**
     * 设置上下文状态为写回
     */
    void setWritten();

    /**
     * 设置上下文状态为写回成功
     */
    void setCompleted();

    /**
     * 设置上下文状态为请求结束
     */
    void setTerminated();

    /**
     * 判断状态
     */
    boolean isRunning();

    boolean isWritten();

    boolean isCompleted();

    boolean isTerminated();

    /**
     * 获取协议
     *
     * @return 协议
     */
    String getProtocol();

    /**
     * 获取请求对象
     *
     * @return request
     */
    Object getRequest();

    /**
     * 获取请求对象
     *
     * @return response
     */
    Object getResponse();

    /**
     * 设置响应对象
     *
     * @param response resp
     */
    void setResponse(Object response);

    /**
     * 获取异常
     *
     * @return 异常
     */
    Throwable getThrowable();

    /**
     * 设置异常
     *
     * @param throwable 异常
     */
    void setThrowable(Throwable throwable);

    /**
     * 获取Netty的传输上下文
     *
     * @return ctx
     */
    ChannelHandlerContext getNettyCtx();

    /**
     * 判断连接是否还存活
     *
     * @return is keep alive
     */
    boolean isKeepAlive();

    /**
     * 是否释放连接资源
     *
     * @return is release request
     */
    boolean isReleaseRequest();

    /**
     * 设置异步回调函数
     *
     * @param consumer 异步回调函数
     */
    void setCompletedCallBack(Consumer<IContext> consumer);

    /**
     * 回调异步响应函数
     */
    void invokeCompletedCallBack();
}
