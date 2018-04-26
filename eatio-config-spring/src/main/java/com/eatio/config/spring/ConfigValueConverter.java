package com.eatio.config.spring;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ConfigValueConverter {

    public Object convert(Class clazz, String value) {
        if (StringUtils.isEmpty(value)) return null;
        else if (String.class == clazz) return value;
        else if (Boolean.class == clazz) return Boolean.parseBoolean(value);
        else if (Integer.class == clazz) return Integer.parseInt(value);
        else if (Long.class == clazz) return Long.parseLong(value);
        else if (Double.class == clazz) return Double.parseDouble(value);
        else if (BigDecimal.class == clazz) return new BigDecimal(value);
        else return null;
    }
}
