package com.eatio.config.zookeeper;

import com.eatio.config.spring.ConfigBeanPostProcessor;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZookeeperConfigProperties.class)
@ConditionalOnProperty(name = "spring.config.zookeeper.enable", havingValue = "true")
public class ZookeeperConfigAutoConfiguration {
    @Autowired
    private ZookeeperConfigProperties zookeeperConfigProperties;

    @Bean
    public RetryPolicy retryPolicy() {
        return new RetryNTimes(zookeeperConfigProperties.getRetries(), zookeeperConfigProperties.getRetryInterval());
    }

    @Bean
    public CuratorFramework curatorFramework(RetryPolicy retryPolicy) {
        return CuratorFrameworkFactory.newClient(zookeeperConfigProperties.getConnection(), zookeeperConfigProperties.getSessionTimeout(), zookeeperConfigProperties.getConnectTimeout(), retryPolicy);
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public ZookeeperConfigValueWatcher configValueWatcher(CuratorFramework curatorFramework) {
        return new ZookeeperConfigValueWatcher(zookeeperConfigProperties.getNamespace(), zookeeperConfigProperties.getContext(), curatorFramework);
    }

    @Bean
    public ConfigBeanPostProcessor configBeanPostProcessor(ZookeeperConfigValueWatcher configValueWatcher) {
        return new ConfigBeanPostProcessor(configValueWatcher);
    }
}
