package com.monkeyj.object;

import java.util.Objects;

public class HashPair {

    private Obj key;
    private Obj value;

    public HashPair(final Obj key, final Obj value) {
        this.key = key;
        this.value = value;
    }

    public HashPair() {
    }

    public Obj getKey() {
        return key;
    }

    public void setKey(final Obj key) {
        this.key = key;
    }

    public Obj getValue() {
        return value;
    }

    public void setValue(final Obj value) {
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
        final HashPair hashPair = (HashPair) o;
        return Objects.equals(key, hashPair.key) && Objects.equals(value, hashPair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "HashPair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
