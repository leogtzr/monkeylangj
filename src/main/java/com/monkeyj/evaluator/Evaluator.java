package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.ast.Bool;
import com.monkeyj.object.*;
import com.monkeyj.object.Error;

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
            return newError("unknown operator: -%s", right.type());
        }

        final var value = ((Int) right).getValue();
        return new Int(-(value));
    }

    private static com.monkeyj.object.Obj evalPrefixExpression(final String operator, final com.monkeyj.object.Obj right) {
        return switch (operator) {
            case "!" -> evalBangOperatorExpression(right);
            case "-" -> evalMinusPrefixOperatorExpression(right);
            default -> newError("unknown operator: %s%s", operator, right.type());
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
            default -> newError("unknown operator: %s %s %s", left.type(), operator, right.type());
        };
    }

    private static Obj evalInfixExpression(final String operator, final Obj left, final Obj right) {
        if (left.type().equals(ObjConstants.INTEGER_OBJ) && right.type().equals(ObjConstants.INTEGER_OBJ)) {
            return evalIntegerInfixExpression(operator, left, right);
        }
        return newError("unknown operator: %s %s %s", left.type(), operator, right.type());
    }

    private static Obj evalProgram(final Program program, final Environment env) {
        Obj result = null;

        for (final var stmt : program.getStatements()) {
            result = eval(stmt, env);

            if (result instanceof ReturnValue returnValue) {
                return returnValue.getValue();
            } else if (result instanceof Error error) {
                return error;
            }
        }

        return result;
    }

    private static Obj evalBlockStatement(final BlockStatement block, final Environment env) {
        Obj result = null;

        for (final var stmt : block.getStatements()) {
            result = eval(stmt, env);
//            if (result != null && result.type().equals(ObjConstants.RETURN_VALUE_OBJ)) {
//                return result;
//            }
            if (result != null) {
                final var rt = result.type();
                if (rt.equals(ObjConstants.RETURN_VALUE_OBJ) || rt.equals(ObjConstants.ERROR_OBJ)) {
                    return result;
                }
            }
        }

        return result;
    }

    public static Obj eval(final Node node, final Environment env) {
        if (node instanceof final Program program) {
            // return evalStatements(program.getStatements());
            return evalProgram(program, env);
        } else if (node instanceof final ExpressionStatement expr) {
            return eval(expr.getExpression(), env);
        } else if (node instanceof final IntegerLiteral literal) {
            final var intObj = new Int();
            intObj.setValue(literal.getValue());

            return intObj;
        } else if (node instanceof final Bool boolLiteral) {
            return nativeBoolToBooleanObject(boolLiteral.getValue());
        } else if (node instanceof final PrefixExpression prefix) {
            final var right = eval(prefix.getRight(), env);
            return isError(right) ? right : evalPrefixExpression(prefix.getOperator(), right);
        } else if (node instanceof final InfixExpression infix) {
            final var left = eval(infix.getLeft(), env);
            if (isError(left)) {
                return left;
            }
            final var right = eval(infix.getRight(), env);
            if (isError(right)) {
                return right;
            }
            return evalInfixExpression(infix.getOperator(), left, right);
        } else if (node instanceof BlockStatement blockStmt) {
            // return evalStatements(blockStmt.getStatements());
            return evalBlockStatement(blockStmt, env);
        } else if (node instanceof IfExpression ifExpression) {
            return evalIfExpression(ifExpression, env);
        } else if (node instanceof ReturnStatement returnStmt) {
            final var val = eval(returnStmt.getReturnValue(), env);
            return isError(val) ? val : new ReturnValue(val);
        } else if (node instanceof LetStatement letStmt) {
            final var val = eval(letStmt.getValue(), env);
            if (isError(val)) {
                return val;
            }
            env.set(letStmt.getName().getValue(), val);
        } else if (node instanceof Identifier identifier) {
            return evalIdentifier(identifier, env);
        }

        return null;
    }

    private static Obj evalIdentifier(final Identifier node, final Environment env) {
        if (!env.exists(node.getValue())) {
            return newError("identifier not found: " + node.getValue());
        }

        return env.get(node.getValue());
    }

    private static Obj evalIfExpression(final IfExpression ifExpression, final Environment env) {
        final var condition = eval(ifExpression.getCondition(), env);
        if (isError(condition)) {
            return condition;
        }

        if (isTruthy(condition)) {
            return eval(ifExpression.getConsequence(), env);
        } else if (ifExpression.getAlternative() != null) {
            return eval(ifExpression.getAlternative(), env);
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

    private static Obj evalStatements(final List<Statement> statements, final Environment env) {
        Obj result = null;

        for (final var stmt : statements) {
            result = eval(stmt, env);

            if (result instanceof ReturnValue returnValue) {
                return returnValue.getValue();
            }
        }

        return result;
    }

    private static Error newError(final String format, final Object... args) {
        return new Error(String.format(format, args));
    }

    private static boolean isError(final Obj obj) {
        if (obj != null) {
            return obj.type().equals(ObjConstants.ERROR_OBJ);
        }

        return false;
    }

}
