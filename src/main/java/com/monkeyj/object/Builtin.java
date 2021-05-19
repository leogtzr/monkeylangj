package com.monkeyj.object;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Builtin implements Obj {

    private Function<List<Obj>, Obj> fn;

    public Builtin(final Function<List<Obj>, Obj> fn) {
        this.fn = fn;
    }

    @Override
    public String type() {
        return ObjConstants.BUILTIN_OBJ;
    }

    @Override
    public String inspect() {
        return "builtin function";
    }

    public Function<List<Obj>, Obj> getFn() {
        return fn;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Builtin builtin = (Builtin) o;
        return Objects.equals(fn, builtin.fn);
    }

    @Override
    public String toString() {
        return "Builtin{" +
                "fn=" + fn +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(fn);
    }
}
