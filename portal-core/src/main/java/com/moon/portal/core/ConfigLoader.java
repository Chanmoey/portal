package com.moon.portal.core;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.moon.portal.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
@Slf4j
public class ConfigLoader {

    private static final String CONFIG_FILE = "portal.properties";

    private static final String ENV_PREFIX = "PORTAL_";

    private static final String JVM_PREFIX = "portal.";

    private static final String PROPERTIES_PREFIX = "portal.";

    private static final ConfigLoader INSTANCE = new ConfigLoader();

    private ConfigLoader() {
    }

    public static ConfigLoader getInstance() {
        return INSTANCE;
    }

    private Config config;

    public static Config getConfig() {
        return INSTANCE.config;
    }

    /**
     * 1. 运行参数
     * 2. JVM参数
     * 3. 环境变量
     * 4. 配置文件
     * 5. 配置对象默认值
     *
     * @param args args
     * @return config
     */
    public Config load(String[] args) {
        config = new Config();

        loadFromConfigFile();

        loadFromEnv();

        loadFromJvm();

        loadFromArgs(args);

        return config;
    }

    private void loadFromConfigFile() {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        if (inputStream != null) {
            Properties properties = new Properties();
            try (inputStream) {
                properties.load(inputStream);
                PropertiesUtil.properties2Object(properties, config, PROPERTIES_PREFIX);
            } catch (IOException e) {
                log.warn("load config file {} error!", CONFIG_FILE, e);
            }
        }
    }

    private void loadFromEnv() {
        Map<String, String> env = System.getenv();
        Properties properties = new Properties();
        properties.putAll(env);
        PropertiesUtil.properties2Object(properties, config, ENV_PREFIX);
    }

    private void loadFromJvm() {
        Properties properties = System.getProperties();
        PropertiesUtil.properties2Object(properties, config, JVM_PREFIX);
    }

    private void loadFromArgs(String[] args) {
        // --port=1234
        if (args != null && args.length > 0) {
            Properties properties = new Properties();
            for (String arg : args) {
                if (arg.startsWith("--") && arg.contains("=")) {
                    int equalIdx = arg.indexOf("=");
                    properties.put(arg.substring(2, equalIdx), arg.substring(equalIdx + 1));
                }
            }
            PropertiesUtil.properties2Object(properties, config);
        }

    }
}

























