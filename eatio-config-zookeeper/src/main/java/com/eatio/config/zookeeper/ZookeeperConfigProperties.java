package com.eatio.config.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.config.zookeeper")
public class ZookeeperConfigProperties {
    private boolean enable = true;
    private String context = "/default";
    private String namespace = "default";
    private String connection = "zookeeper://127.0.0.1:2181";
    private int retries = 3;
    private int retryInterval = 1000;
    private int connectTimeout = 15 * 1000;
    private int sessionTimeout = 60 * 1000;
}
