package com.eatio.config.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConfigValueWatcher {
    protected static Logger log = LoggerFactory.getLogger(ConfigValueWatcher.class);
    private static Map<String, String> configFieldValueCache = new ConcurrentHashMap<>();
    private static MultiValueMap<String, ConfigFieldWrapper> configFieldWrapperMap = new LinkedMultiValueMap<>();
    private ConfigValueConverter configValueConverter = new ConfigValueConverter();

    public void register(String key, ConfigFieldWrapper configFieldWrapper) {
        if (configFieldWrapperMap.containsKey(key)) {
            configFieldWrapperMap.get(key).add(configFieldWrapper);
        } else {
            configFieldWrapperMap.put(key, Arrays.asList(configFieldWrapper));
        }
        try {
            configFieldWrapper.getField().set(configFieldWrapper.getBean(), configValueConverter.convert(configFieldWrapper.getField().getType(), evaluateValue(key)));
        } catch (IllegalAccessException e) {
            log.warn("illegal access exception count when update value : {},{}", configFieldWrapper.getBean().getClass().getName(), configFieldWrapper.getField().getName());
        } catch (Exception e) {
            log.warn("illegal access exception count when update value : {},{}", configFieldWrapper.getBean().getClass().getName(), configFieldWrapper.getField().getName());
        }
    }

    public void updateValue(String key, String value) {
        if (configFieldWrapperMap.containsKey(key)) {
            List<ConfigFieldWrapper> configFieldWrappers = configFieldWrapperMap.get(key);
            for (ConfigFieldWrapper configFieldWrapper : configFieldWrappers) {
                try {
                    configFieldWrapper.getField().set(configFieldWrapper.getBean(), configValueConverter.convert(configFieldWrapper.getField().getType(), value));
                } catch (IllegalAccessException e) {
                    log.warn("illegal access exception count when update value : {},{}", configFieldWrapper.getBean().getClass().getName(), configFieldWrapper.getField().getName());
                }
            }
        }
    }

    public String evaluateValue(String key) throws Exception {
        if (!configFieldValueCache.containsKey(key)) {
            configFieldValueCache.put(key, fetchValue(key));
        }
        return configFieldValueCache.get(key);
    }

    public abstract String fetchValue(String key) throws Exception;
}
