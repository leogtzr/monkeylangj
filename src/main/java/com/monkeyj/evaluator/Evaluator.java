package com.monkeyj.evaluator;

import com.monkeyj.ast.*;
import com.monkeyj.ast.Bool;
import com.monkeyj.object.*;
import com.monkeyj.object.Error;

import java.util.*;

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

    private static Obj evalPrefixExpression(final String operator, final Obj right) {
        return switch (operator) {
            case "!" -> evalBangOperatorExpression(right);
            case "-" -> evalMinusPrefixOperatorExpression(right);
            default -> newError("unknown operator: %s%s", operator, right.type());
        };
    }

    private static Obj evalIntegerInfixExpression(final String operator, final Obj left, final Obj right) {
        final int leftVal = ((Int) left).getValue();
        final int rightVal = ((Int) right).getValue();

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

        if (left.type().equals(ObjConstants.STRING_OBJ) && right.type().equals(ObjConstants.STRING_OBJ)) {
            return evalStringInfixExpression(operator, left, right);
        }

        if (operator.equals("==")) {
            return nativeBoolToBooleanObject(left == right);
        }

        if (operator.equals("!=")) {
            return nativeBoolToBooleanObject(left != right);
        }

        return newError("unknown operator: %s %s %s", left.type(), operator, right.type());
    }

    private static Obj evalStringInfixExpression(final String operator, final Obj left, final Obj right) {
        if (!operator.equals("+")) {
            return newError("unknown operator: %s %s %s", left.type(), operator, right.type());
        }

        final var leftVal = ((Str) left).getValue();
        final var rightVal = ((Str) right).getValue();

        return new Str(leftVal + rightVal);
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
        } else if (node instanceof FunctionLiteral fn) {
            final var params = fn.getParameters();
            final var body = fn.getBody();

            return new Function(params, body, env);
        } else if (node instanceof StringLiteral str) {
            return new Str(str.getValue());
        } else if (node instanceof CallExpression call) {
            final var function = eval(call.getFunction(), env);
            if (isError(function)) {
                return function;
            }

            final var args = evalExpressions(call.getArguments(), env);
            if (args.size() == 1 && isError(args.get(0))) {
                return args.get(0);
            }

            return applyFunction(function, args);
        } else if (node instanceof ArrayLiteral arr) {
            final var elements = evalExpressions(arr.getElements(), env);
            if (elements.size() == 1 && isError(elements.get(0))) {
                return elements.get(0);
            }

            return new Array(elements);
        } else if (node instanceof IndexExpression) {
            final var left = eval(((IndexExpression) node).getLeft(), env);
            if (isError(left)) {
                return left;
            }

            final var index = eval(((IndexExpression) node).getIndex(), env);
            if (isError(index)) {
                return index;
            }

            return evalIndexExpression(left, index);
        } else if (node instanceof HashLiteral hash) {
            return evalHashLiteral(hash, env);
        }

        return null;
    }

    private static Obj evalHashLiteral(final HashLiteral node, final Environment env) {
        final Map<HashKey, HashPair> pairs = new HashMap<>();

        for (final Map.Entry<Expression, Expression> entry : node.getPairs().entrySet()) {
            final var key = eval(entry.getKey(), env);
            if (isError(key)) {
                return key;
            }

            if (key instanceof Hashable hashKey) {
                final var value = eval(entry.getValue(), env);
                if (isError(value)) {
                    return value;
                }

                final var hashed = hashKey.hashKey();
                pairs.put(hashed, new HashPair(key, value));
            } else {
                return newError("unusable as hash key: %s", key.type());
            }
        }

        return new Hash(pairs);
    }

    private static Obj evalIndexExpression(final Obj left, final Obj index) {
        if (left.type().equals(ObjConstants.ARRAY_OBJ) && index.type().equals(ObjConstants.INTEGER_OBJ)) {
            return evalArrayIndexExpression(left, index);
        } else if (left.type().equals(ObjConstants.HASH_OBJ)) {
            return evalHashIndexExpression(left, index);
        }
        return newError("index operator not supported: %s", left.type());
    }

    private static Obj evalHashIndexExpression(final Obj hash, final Obj index) {
        final var hashObject = (Hash) hash;
        if (!(index instanceof Hashable)) {
            return newError("unusable as hash key: %s", index.type());
        }

        final Hashable key = (Hashable) index;
        final var pair = hashObject.getPairs().get(key.hashKey());
        if (pair == null) {
            return Literals.NULL;
        }

        return pair.getValue();
    }

    private static Obj evalArrayIndexExpression(final Obj array, final Obj index) {
        final var arrayObject = (Array) array;
        final var idx = ((Int) index).getValue();
        final var max = arrayObject.getElements().size() - 1;

        if (idx < 0 || idx > max) {
            return Literals.NULL;
        }

        return arrayObject.getElements().get(idx);
    }

    private static Obj applyFunction(final Obj function, final List<Obj> args) {
        if (function instanceof Function fn) {
            final var extendedEnv = extendFunctionEnv(fn, args);
            final var evaluated = eval(fn.getBody(), extendedEnv);
            return unwrapReturnValue(evaluated);   
        }

        if (function instanceof Builtin fn) {
            return fn.getFn().apply(args);
        }

        return newError("not a function: %s", function.type());
    }

    private static Obj unwrapReturnValue(final Obj obj) {
        if (obj instanceof ReturnValue returnValue) {
            return returnValue.getValue();
        }

        return obj;
    }

    private static Environment extendFunctionEnv(final Function function, final List<Obj> args) {
        final var env = Environment.newEnclosedEnvironment(function.getEnv());

        for (int paramIdx = 0; paramIdx < function.getParameters().size(); paramIdx++) {
            final var param = function.getParameters().get(paramIdx);
            env.set(param.getValue(), args.get(paramIdx));
        }

        return env;
    }

    private static List<Obj> evalExpressions(final List<Expression> exps, final Environment env) {
        final List<Obj> result = new ArrayList<>();

        for (final var exp : exps) {
            final var evaluated = eval(exp, env);
            if (isError(evaluated)) {
                return List.of(evaluated);
            }
            result.add(evaluated);
        }

        return result;
    }

    private static Obj evalIdentifier(final Identifier node, final Environment env) {
        final var val = env.get(node.getValue());
        if (val != null) {
            return val;
        }

        final var builtin = Builtins.BUILTINS.get(node.getValue());
        if (builtin != null) {
            return builtin;
        }

        return newError("identifier not found: " + node.getValue());
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

    /*
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
     */

    public static Error newError(final String format, final Object... args) {
        return new Error(String.format(format, args));
    }

    private static boolean isError(final Obj obj) {
        if (obj != null) {
            return obj.type().equals(ObjConstants.ERROR_OBJ);
        }

        return false;
    }

}
