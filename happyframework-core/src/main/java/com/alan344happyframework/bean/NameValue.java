package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author AlanSun
 * @date 2020/10/31 12:27
 */
@Getter
@Setter
@NoArgsConstructor
public class NameValue {
    private String name;
    private Object value;

    public NameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public NameValue(String name, Object value, List<NameValue> subNameValues) {
        this.name = name;
        this.value = value;
        this.subNameValues = subNameValues;
    }

    private List<NameValue> subNameValues;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameValue nameValue = (NameValue) o;

        return value.equals(nameValue.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
