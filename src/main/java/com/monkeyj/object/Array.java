package com.monkeyj.object;

import java.util.ArrayList;
import java.util.List;

public class Array implements Obj {

    private final List<Obj> elements;

    public Array(final List<Obj> elements) {
        this.elements = elements;
    }

    public List<Obj> getElements() {
        return elements;
    }

    @Override
    public String type() {
        return ObjConstants.ARRAY_OBJ;
    }

    @Override
    public String inspect() {
        final var out = new StringBuilder();
        final List<String> elements = new ArrayList<>();

        for (final var e : this.elements) {
            elements.add(e.inspect());
        }

        out.append("[");
        out.append(String.join(", ", elements));
        out.append("]");

        return out.toString();
    }

    @Override
    public String toString() {
        return this.inspect();
    }

}
