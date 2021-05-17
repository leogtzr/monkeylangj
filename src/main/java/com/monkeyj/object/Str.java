package com.monkeyj.object;

public class Str implements Obj {

    private String value;

    public Str(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String type() {
        return ObjConstants.STRING_OBJ;
    }

    @Override
    public String inspect() {
        return this.value;
    }
}
