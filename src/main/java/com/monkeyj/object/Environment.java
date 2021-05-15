package com.monkeyj.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Environment {
    private final Map<String, Obj> store;
    private final Environment outer;

    public Environment() {
        this.store = new HashMap<>();
        this.outer = null;
    }

    public static Environment newEnclosedEnvironment(final Environment outer) {
        final var env = new Environment(outer);

        return env;
    }

    private Environment(final Environment outer) {
        this.store = new HashMap<>();
        this.outer = outer;
    }

    public Obj get(final String name) {
        if (this.store.get(name) == null && this.outer != null) {
            return this.outer.get(name);
        }
        return this.store.get(name);
    }

    public Obj set(final String name, final Obj val) {
        return this.store.put(name, val);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Environment that = (Environment) o;
        return Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store);
    }

    @Override
    public String toString() {
        return "Environment{" +
                "store=" + store +
                '}';
    }

}
