package com.eatio.config.junit;

import com.eatio.config.spring.Config;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : liming
 * @version : 1.0.0
 * @since : 1.0.0 2018/4/25 16:35
 */
@Service
public class TestS {
    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Config("/interval")
    private Integer config;

    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(config);
            }
        }, 5L, 2L, TimeUnit.SECONDS);
    }

}
