package com.moon.portal.core;

import lombok.Getter;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class Config {

    private int port = 8888;

    private String applicationName = "portal-application";

    private String registryAddress = "127.0.0.1";

    private String env = "dev";

    /**
     * Netty 配置
     */
    private int eventLoopGroupBossNum = 1;

    private int eventLoopGroupWorkerNum = Runtime.getRuntime().availableProcessors();

    private int maxContentLength = 64 * 1024 * 1024;

    private boolean whenComplete = true;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getEventLoopGroupBossNum() {
        return eventLoopGroupBossNum;
    }

    public void setEventLoopGroupBossNum(int eventLoopGroupBossNum) {
        this.eventLoopGroupBossNum = eventLoopGroupBossNum;
    }

    public int getEventLoopGroupWorkerNum() {
        return eventLoopGroupWorkerNum;
    }

    public void setEventLoopGroupWorkerNum(int eventLoopGroupWorkerNum) {
        this.eventLoopGroupWorkerNum = eventLoopGroupWorkerNum;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public boolean isWhenComplete() {
        return whenComplete;
    }

    public void setWhenComplete(boolean whenComplete) {
        this.whenComplete = whenComplete;
    }
}
