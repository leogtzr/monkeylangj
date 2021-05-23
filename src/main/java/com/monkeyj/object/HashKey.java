package com.monkeyj.object;

import java.util.Objects;

public class HashKey {
    private String type;
    private Integer value;

    public HashKey() { }

    public HashKey(final String type, final Integer value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HashKey{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HashKey hashKey = (HashKey) o;
        return Objects.equals(type, hashKey.type) && Objects.equals(value, hashKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
