package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;

import java.util.List;

public final class Evaluator {

    private Evaluator() {}

    public static Obj eval(final Node node) {
        if (node instanceof Program) {
            final var program = (Program) node;
            return evalStatements(program.getStatements());
        } else if (node instanceof ExpressionStatement) {
            final var expr = (ExpressionStatement) node;
            return eval(expr.getExpression());
        } else if (node instanceof IntegerLiteral) {
            final IntegerLiteral literal = (IntegerLiteral) node;
            final var intObj = new Int();
            intObj.setValue(literal.getValue());

            return intObj;
        }

        return null;
    }

    private static Obj evalStatements(final List<Statement> stmts) {
        Obj result = null;

        for (final var statement : stmts) {
            result = eval(statement);
        }

        return result;
    }

}
