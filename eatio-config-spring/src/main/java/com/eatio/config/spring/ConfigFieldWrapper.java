package com.eatio.config.spring;

import java.lang.reflect.Field;

public class ConfigFieldWrapper {
    private Object bean;
    private Field field;

    public ConfigFieldWrapper(Object bean, Field field) {
        this.bean = bean;
        this.field = field;
    }

    public Object getBean() {
        return bean;
    }

    public ConfigFieldWrapper setBean(Object bean) {
        this.bean = bean;
        return this;
    }

    public Field getField() {
        return field;
    }

    public ConfigFieldWrapper setField(Field field) {
        this.field = field;
        return this;
    }
}
