package com.monkeyj.object;

public class Str implements Obj, Hashable {

    private String value;

    public static final Str of(final String value) {
        return new Str(value);
    }

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

    @Override
    public HashKey hashKey() {
        // TODO: fix
        return new HashKey(this.type(), this.value.hashCode());
    }
}
