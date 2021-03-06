package com.monkeyj.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Program implements Node {

    private List<Statement> statements;

    public Program() {
        this.statements = new ArrayList<>();
    }

    public Program(final List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(this.statements);
    }

    public void addStatement(final Statement stmt) {
        this.statements.add(stmt);
    }

    @Override
    public String tokenLiteral() {
        if (!this.statements.isEmpty()) {
            return this.statements.get(0).tokenLiteral();
        }
        return "";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Program program = (Program) o;
        return Objects.equals(statements, program.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder();

        this.statements.forEach(out::append);

        return out.toString();
    }
}
