package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashLiteral implements Node, Expression {

    private Token token;            // the '{' token
    private Map<Expression, Expression> pairs;

    public HashLiteral(final Token token, final Map<Expression, Expression> pairs) {
        this.token = token;
        this.pairs = pairs;
    }

    public HashLiteral() { }

    @Override
    public void expressionNode() {}

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Map<Expression, Expression> getPairs() {
        return pairs;
    }

    public void setPairs(final Map<Expression, Expression> pairs) {
        this.pairs = pairs;
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        final List<String> pairs = new ArrayList<>();
        this.pairs.forEach((key, value) -> pairs.add(String.format("%s:%s", key.toString(), value.toString())));

        out.append("{");
        out.append(String.join(", ", pairs));
        out.append("}");

        return out.toString();
    }
}
