package com.moon.portal.core;

/**
 * @author Chanmoey
 * @date 2023年06月01日
 */
public class Bootstrap {

    public static void main(String[] args) {
        // 加载静态配置
        Config config = ConfigLoader.getInstance().load(args);
        System.out.println(config.getPort());
        // 初始化网关插件

        // 加载配置中心管理器，获取动态配置，监听配置中心

        // 启动容器

        // 连接注册中心，将注册中心的实例加载到本地

        // 服务关机
    }
}
