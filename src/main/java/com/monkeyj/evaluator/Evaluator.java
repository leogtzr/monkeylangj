package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;

import java.util.List;

public final class Evaluator {

    private Evaluator() {}

    private static Obj evalBangOperatorExpression(final Obj right) {
        if (right.equals(Literals.TRUE)) {
            return Literals.FALSE;
        } else if (right.equals(Literals.FALSE)) {
            return Literals.TRUE;
        } else if (right.equals(Literals.NULL)) {
            return Literals.TRUE;
        }
        return Literals.FALSE;
    }

    private static com.monkeyj.object.Obj evalPrefixExpression(final String operator, final com.monkeyj.object.Obj right) {
        switch (operator) {
            case "!":
                return evalBangOperatorExpression(right);
            default:
                return Literals.NULL;
        }
    }

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
        } else if (node instanceof com.monkeyj.ast.Bool) {
            final com.monkeyj.ast.Bool boolLiteral = (com.monkeyj.ast.Bool) node;
//            final var boolObj = new com.monkeyj.object.Bool(boolLiteral.getValue());
//            return boolObj;
            return nativeBoolToBooleanObject(boolLiteral.getValue());
        } else if (node instanceof PrefixExpression) {
            final var prefix = (PrefixExpression) node;
            // final var prefixNode =
            final var right = eval(prefix.getRight());
            return evalPrefixExpression(prefix.getOperator(), right);
        }

        return null;
    }

    private static com.monkeyj.object.Bool nativeBoolToBooleanObject(final boolean input) {
        return input ? Literals.TRUE : Literals.FALSE;
    }

    private static Obj evalStatements(final List<Statement> stmts) {
        Obj result = null;

        for (final var statement : stmts) {
            result = eval(statement);
        }

        return result;
    }

}
