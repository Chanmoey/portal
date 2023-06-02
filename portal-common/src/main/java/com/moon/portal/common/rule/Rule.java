package com.moon.portal.common.rule;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 规则对象
 *
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class Rule implements Comparable<Rule>, Serializable {

    /**
     * 规则唯一ID
     */
    @Getter
    private String id;

    /**
     * 规则名
     */
    @Getter
    private String name;

    /**
     * 协议
     */
    @Getter
    private String protocol;

    /**
     * 规则优先级
     */
    @Getter
    private Integer order;


    private Set<FilterConfig> filterConfigs = new HashSet<>();

    public Rule() {
        super();
    }

    public Rule(String id, String name, String protocol, int order, Set<FilterConfig> filterConfigs) {
        super();
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.order = order;
        this.filterConfigs = filterConfigs;
    }

    /**
     * 添加配置信息
     *
     * @param filterConfig config
     */
    public boolean addFilterConfig(FilterConfig filterConfig) {
        return filterConfigs.add(filterConfig);
    }

    /**
     * 根据配置id获取规则
     *
     * @param configId id
     * @return filterConfig
     */
    public FilterConfig getFilterConfig(String configId) {
        for (FilterConfig filterConfig : filterConfigs) {
            if (filterConfig.getId().equalsIgnoreCase(configId)) {
                return filterConfig;
            }
        }

        return null;
    }

    /**
     * 根据配置id，查看规则是否存在
     *
     * @param configId configId
     * @return boolean
     */
    public boolean hashId(String configId) {
        return getFilterConfig(configId) != null;
    }

    @Override
    public int compareTo(Rule o) {
        int orderCompare = Integer.compare(getOrder(), o.getOrder());
        if (orderCompare == 0) {
            return getId().compareTo(o.getId());
        }
        return orderCompare;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return id.equals(((Rule) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class FilterConfig {
        /**
         * 规则配置ID
         */
        private String id;

        /**
         * 配置信息
         */
        private String config;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (this == o) {
                return true;
            }

            return id.equals(((FilterConfig) o).id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
