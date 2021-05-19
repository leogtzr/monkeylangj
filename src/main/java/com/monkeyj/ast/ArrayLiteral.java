package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteral implements Node, Expression {

    private Token token;
    private List<Expression> elements;

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public List<Expression> getElements() {
        return elements;
    }

    public void setElements(final List<Expression> elements) {
        this.elements = elements;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();

        final List<String> args = new ArrayList<>();
        for (final Expression el : this.elements) {
            args.add(el.toString());
        }

        out.append("[");
        out.append(String.join(", ", args));
        out.append("]");

        return out.toString();
    }
}
