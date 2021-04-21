package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;
import com.monkeyj.object.ObjConstants;

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

    private static Obj evalMinusPrefixOperatorExpression(final Obj right) {
        if (!right.type().equals(ObjConstants.INTEGER_OBJ)) {
            return Literals.NULL;
        }

        final var value = ((Int) right).getValue();
        return new Int(-(value));
    }

    private static com.monkeyj.object.Obj evalPrefixExpression(final String operator, final com.monkeyj.object.Obj right) {
        switch (operator) {
            case "!":
                return evalBangOperatorExpression(right);
            case "-":
                return evalMinusPrefixOperatorExpression(right);
            default:
                return Literals.NULL;
        }
    }

    private static Obj evalIntegerInfixExpression(final String operator, final Obj left, final Obj right) {
        final var leftVal = ((Int) left).getValue();
        final var rightVal = ((Int) right).getValue();

        switch (operator) {
            case "+":
                return new Int(leftVal + rightVal);
            case "-":
                return new Int(leftVal - rightVal);
            case "*":
                return new Int(leftVal * rightVal);
            case "/":
                return new Int(leftVal / rightVal);
            case "<":
                return nativeBoolToBooleanObject(leftVal < rightVal);
            case ">":
                return nativeBoolToBooleanObject(leftVal > rightVal);
            case "==":
                return nativeBoolToBooleanObject(leftVal == rightVal);
            case "!=":
                return nativeBoolToBooleanObject(leftVal != rightVal);
            default:
                return Literals.NULL;
        }
    }

    private static Obj evalInfixExpression(final String operator, final Obj left, final Obj right) {
        if (left.type().equals(ObjConstants.INTEGER_OBJ) && right.type().equals(ObjConstants.INTEGER_OBJ)) {
            return evalIntegerInfixExpression(operator, left, right);
        }
        return Literals.NULL;
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
        } else if (node instanceof InfixExpression) {
            final var infix = (InfixExpression) node;
            final var left = Evaluator.eval(infix.getLeft());
            final var right = Evaluator.eval(infix.getRight());
            return evalInfixExpression(infix.getOperator(), left, right);
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
