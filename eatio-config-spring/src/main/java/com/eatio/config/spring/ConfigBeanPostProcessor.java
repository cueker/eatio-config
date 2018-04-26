package com.eatio.config.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class ConfigBeanPostProcessor implements BeanPostProcessor {
    private ConfigValueWatcher configValueWatcher;

    public ConfigBeanPostProcessor(ConfigValueWatcher configValueWatcher) {
        this.configValueWatcher = configValueWatcher;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Config.class)) {
                field.setAccessible(true);
                Config config = field.getAnnotation(Config.class);
                configValueWatcher.register(config.value(), new ConfigFieldWrapper(bean, field));
            }
        }
        return bean;
    }
}
