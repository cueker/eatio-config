package com.eatio.config.zookeeper;

import com.eatio.config.spring.ConfigValueWatcher;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.StringUtils;

public class ZookeeperConfigValueWatcher extends ConfigValueWatcher {
    private String context;
    private String namespace;
    private CuratorFramework curatorFramework;

    public ZookeeperConfigValueWatcher(String namespace, String context, CuratorFramework curatorFramework) {
        this.context = context;
        this.namespace = namespace;
        this.curatorFramework = curatorFramework;
    }

    public void init() {
        curatorFramework.start();
        curatorFramework.usingNamespace(namespace);
        try {
            if (curatorFramework.checkExists().forPath(context) == null)
                curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(context);
            PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, context, true);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    String key = pathChildrenCacheEvent.getData().getPath().replace(context, "");
                    String data = new String(pathChildrenCacheEvent.getData().getData());
                    updateValue(key, data);
                }
            });
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        curatorFramework.close();
    }

    @Override
    public String fetchValue(String key) throws Exception {
        if (curatorFramework.checkExists().forPath(context + key) == null) {
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(context + key, "".getBytes());
        }
        String value = new String(curatorFramework.getData().forPath(context + key));
        return StringUtils.isEmpty(value) ? null : value;
    }

}
