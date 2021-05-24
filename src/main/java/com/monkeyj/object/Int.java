package com.monkeyj.object;

import java.util.Objects;

public class Int implements Obj, Hashable {

    private Integer value;

    public static Int of(final Integer value) {
        return new Int(value);
    }

    public Int(final Integer value) {
        this.value = value;
    }

    public Int() {}

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Int that = (Int) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "IntegerObj{" +
                "value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String type() {
        return ObjConstants.INTEGER_OBJ;
    }

    @Override
    public String inspect() {
        return this.value + "";
    }

    @Override
    public HashKey hashKey() {
        return new HashKey(this.type(), this.value);
    }
}
