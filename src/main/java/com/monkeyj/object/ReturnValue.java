package com.monkeyj.object;

public class ReturnValue implements Obj {
    private Obj value;

    public ReturnValue(final Obj value) {
        this.value = value;
    }

    public ReturnValue() {}

    public Obj getValue() {
        return value;
    }

    public void setValue(final Obj value) {
        this.value = value;
    }

    @Override
    public String type() {
        return ObjConstants.RETURN_VALUE_OBJ;
    }

    @Override
    public String inspect() {
        return this.value.inspect();
    }

    @Override
    public String toString() {
        return "ReturnValue{" +
                "value=" + value +
                '}';
    }
}
