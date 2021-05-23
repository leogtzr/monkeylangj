package com.monkeyj.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Hash implements Obj {
    private Map<HashKey, HashPair> pairs;

    public Hash(final Map<HashKey, HashPair> pairs) {
        this.pairs = pairs;
    }

    public Hash() { }

    public Map<HashKey, HashPair> getPairs() {
        return pairs;
    }

    public void setPairs(final Map<HashKey, HashPair> pairs) {
        this.pairs = pairs;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Hash hash = (Hash) o;
        return Objects.equals(pairs, hash.pairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairs);
    }

    @Override
    public String toString() {
        return "Hash{" +
                "pairs=" + pairs +
                '}';
    }

    @Override
    public String type() {
        return ObjConstants.HASH_OBJ;
    }

    @Override
    public String inspect() {
        final StringBuilder out = new StringBuilder();

        final List<String> pairsStrs = new ArrayList<>();
        this.pairs.forEach((key, pair) -> pairsStrs.add(String.format("%s: %s", pair.getKey().inspect(), pair.getValue().inspect())));

        out.append("{");
        out.append(String.join(", ", pairsStrs));
        out.append("}");

        return out.toString();
    }
}
