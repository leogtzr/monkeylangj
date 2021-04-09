package com.monkeyj.ast;

import com.monkeyj.token.Token;

import java.util.ArrayList;
import java.util.List;

public class CallExpression implements Expression, Node {

    private Token token;                        // The '(' token
    private Expression function;                // Identifier or FunctionLiteral
    private List<Expression> arguments;

    public CallExpression() {
        this.arguments = new ArrayList<>();
    }

    @Override
    public void expressionNode() { }

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

    public Expression getFunction() {
        return function;
    }

    public void setFunction(final Expression function) {
        this.function = function;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public void setArguments(final List<Expression> arguments) {
        this.arguments = arguments;
    }

    public void addArgument(final Expression argument) {
        this.arguments.add(argument);
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();

        final List<String> args = new ArrayList<>();
        for (final Expression arg : this.arguments) {
            args.add(arg.toString());
        }

        out.append(this.function.toString());
        out.append("(");
        out.append(String.join(", ", args));
        out.append(")");

        return out.toString();
    }
}
