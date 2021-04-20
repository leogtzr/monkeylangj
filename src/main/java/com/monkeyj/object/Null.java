package com.monkeyj.object;

public class Null implements Obj {
    @Override
    public String type() {
        return ObjConstants.NULL_OBJ;
    }

    @Override
    public String inspect() {
        return "null";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
