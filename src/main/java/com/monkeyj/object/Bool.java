package com.monkeyj.object;

import java.util.Objects;

public class Bool implements Obj, Hashable {

    private boolean value;

    public static Bool of(final boolean value) {
        return new Bool(value);
    }

    public Bool(final boolean value) {
        this.value = value;
    }

    public Bool() {}

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean value) {
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
        final Bool bool = (Bool) o;
        return value == bool.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String type() {
        return ObjConstants.BOOLEAN_OBJ;
    }

    @Override
    public String inspect() {
        return String.format("%b", this.value);
    }

    @Override
    public HashKey hashKey() {
        int value = this.value ? 1 : 0;
        return new HashKey(this.type(), value);
    }

    @Override
    public String toString() {
        return "Bool{" +
                "value=" + value +
                '}';
    }
}
