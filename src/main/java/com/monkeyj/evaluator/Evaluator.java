package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;
import com.monkeyj.object.ObjConstants;
import com.monkeyj.object.ReturnValue;

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
        return switch (operator) {
            case "!" -> evalBangOperatorExpression(right);
            case "-" -> evalMinusPrefixOperatorExpression(right);
            default -> Literals.NULL;
        };
    }

    private static Obj evalIntegerInfixExpression(final String operator, final Obj left, final Obj right) {
        final var leftVal = ((Int) left).getValue();
        final var rightVal = ((Int) right).getValue();

        return switch (operator) {
            case "+" -> new Int(leftVal + rightVal);
            case "-" -> new Int(leftVal - rightVal);
            case "*" -> new Int(leftVal * rightVal);
            case "/" -> new Int(leftVal / rightVal);
            case "<" -> nativeBoolToBooleanObject(leftVal < rightVal);
            case ">" -> nativeBoolToBooleanObject(leftVal > rightVal);
            case "==" -> nativeBoolToBooleanObject(leftVal == rightVal);
            case "!=" -> nativeBoolToBooleanObject(leftVal != rightVal);
            default -> Literals.NULL;
        };
    }

    private static Obj evalInfixExpression(final String operator, final Obj left, final Obj right) {
        if (left.type().equals(ObjConstants.INTEGER_OBJ) && right.type().equals(ObjConstants.INTEGER_OBJ)) {
            return evalIntegerInfixExpression(operator, left, right);
        }
        return Literals.NULL;
    }

    private static Obj evalProgram(final Program program) {
        Obj result = null;

        for (final var stmt : program.getStatements()) {
            result = eval(stmt);

            if (result instanceof ReturnValue returnValue) {
                return returnValue.getValue();
            }
        }

        return result;
    }

    private static Obj evalBlockStatement(final BlockStatement block) {
        Obj result = null;

        for (final var stmt : block.getStatements()) {
            result = eval(stmt);

            if (result != null && result.type().equals(ObjConstants.RETURN_VALUE_OBJ)) {
                return result;
            }
        }

        return result;
    }

    public static Obj eval(final Node node) {
        if (node instanceof final Program program) {
            // return evalStatements(program.getStatements());
            return evalProgram(program);
        } else if (node instanceof final ExpressionStatement expr) {
            return eval(expr.getExpression());
        } else if (node instanceof final IntegerLiteral literal) {
            final var intObj = new Int();
            intObj.setValue(literal.getValue());

            return intObj;
        } else if (node instanceof final Bool boolLiteral) {
            return nativeBoolToBooleanObject(boolLiteral.getValue());
        } else if (node instanceof final PrefixExpression prefix) {
            final var right = eval(prefix.getRight());
            return evalPrefixExpression(prefix.getOperator(), right);
        } else if (node instanceof final InfixExpression infix) {
            final var left = Evaluator.eval(infix.getLeft());
            final var right = Evaluator.eval(infix.getRight());
            return evalInfixExpression(infix.getOperator(), left, right);
        } else if (node instanceof BlockStatement blockStmt) {
            // return evalStatements(blockStmt.getStatements());
            // return evalStatements(blockStmt.getStatements());
            return evalBlockStatement(blockStmt);
        } else if (node instanceof IfExpression ifExpression) {
            return evalIfExpression(ifExpression);
        } else if (node instanceof ReturnStatement returnStmt) {
            final var val = eval(returnStmt.getReturnValue());
            return new ReturnValue(val);
        }

        return null;
    }

    private static Obj evalIfExpression(final IfExpression ifExpression) {
        final var condition = eval(ifExpression.getCondition());

        if (isTruthy(condition)) {
            return eval(ifExpression.getConsequence());
        } else if (ifExpression.getAlternative() != null) {
            return eval(ifExpression.getAlternative());
        } else {
            return Literals.NULL;
        }
    }

    private static boolean isTruthy(final Obj obj) {
        return !obj.equals(Literals.NULL) && !obj.equals(Literals.FALSE);
    }

    private static com.monkeyj.object.Bool nativeBoolToBooleanObject(final boolean input) {
        return input ? Literals.TRUE : Literals.FALSE;
    }

    private static Obj evalStatements(final List<Statement> statements) {
        Obj result = null;

        for (final var stmt : statements) {
            result = eval(stmt);

            if (result instanceof ReturnValue returnValue) {
                return returnValue.getValue();
            }
        }

        return result;
    }

}
