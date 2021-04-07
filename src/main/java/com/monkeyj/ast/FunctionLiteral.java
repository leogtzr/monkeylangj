package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionLiteral implements Expression, Node {

    private Token token;
    private List<Identifier> parameters = new ArrayList<>();
    private BlockStatement body;

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Identifier> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(final Identifier identifier) {
        this.parameters.add(identifier);
    }

    public BlockStatement getBody() {
        return body;
    }

    public void setBody(final BlockStatement body) {
        this.body = body;
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

        final List<String> params = new ArrayList<>();
        for (final Identifier p : this.parameters) {
            params.add(p.toString());
        }

        out.append(this.tokenLiteral());
        out.append("(");
        out.append(String.join(", ", params));
        out.append(") ");
        out.append(this.body.toString());

        return out.toString();
    }

}
