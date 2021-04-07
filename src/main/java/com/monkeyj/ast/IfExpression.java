package com.monkeyj.ast;

import com.monkeyj.token.Token;

public class IfExpression implements Expression, Node {

    private Token token;
    private Expression condition;
    private BlockStatement consequence;
    private BlockStatement alternative;

    public IfExpression() {}

    public IfExpression(
            final Token token
            , final Expression condition
            , final BlockStatement consequence
            , final BlockStatement alternative) {
        this.token = token;
        this.condition = condition;
        this.consequence = consequence;
        this.alternative = alternative;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(final Expression condition) {
        this.condition = condition;
    }

    public BlockStatement getConsequence() {
        return consequence;
    }

    public void setConsequence(final BlockStatement consequence) {
        this.consequence = consequence;
    }

    public BlockStatement getAlternative() {
        return alternative;
    }

    public void setAlternative(final BlockStatement alternative) {
        this.alternative = alternative;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.token.literal();
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        out.append("if").append(this.condition.toString()).append(" ").append(this.consequence.toString());

        if (this.alternative != null) {
            out.append("else ").append(this.alternative.toString());
        }

        return out.toString();
    }
}
