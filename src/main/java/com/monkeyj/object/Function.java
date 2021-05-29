package com.monkeyj.object;

import com.monkeyj.ast.BlockStatement;
import com.monkeyj.ast.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Function implements Obj {

    private List<Identifier> parameters;
    private BlockStatement body;
    private Environment env;

    public Function(
            final List<Identifier> parameters
            , final BlockStatement body
            , final Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Identifier> parameters) {
        this.parameters = parameters;
    }

    public BlockStatement getBody() {
        return body;
    }

    public void setBody(final BlockStatement body) {
        this.body = body;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(final Environment env) {
        this.env = env;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Function function = (Function) o;
        return Objects.equals(parameters, function.parameters) &&
                Objects.equals(body, function.body) && Objects.equals(env, function.env);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters, body, env);
    }

    @Override
    public String toString() {
        return "Function{" +
                "parameters=" + parameters +
                ", body=" + body +
                ", env=" + env +
                '}';
    }

    @Override
    public String type() {
        return ObjConstants.FUNCTION_OBJ;
    }

    @Override
    public String inspect() {
        final StringBuilder out = new StringBuilder();

        final List<String> params = new ArrayList<>();
        for (final Identifier idParam : this.parameters) {
            params.add(idParam.toString());
        }

        out.append("fn");
        out.append("(");
        out.append(String.join(", ", params));
        out.append(") {\n");
        out.append(this.body.toString());
        out.append("\n}");

        return out.toString();
    }
}
