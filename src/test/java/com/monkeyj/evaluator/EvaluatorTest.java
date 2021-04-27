package com.monkeyj.evaluator;

import com.monkeyj.ast.Program;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.object.*;
import com.monkeyj.object.Error;
import com.monkeyj.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    @Test
    public void shouldHandleErrors() {
        record test(String input, String expectedMessage) {}

        final test[] tests = {
                new test("5 + true;", "unknown operator: INTEGER + BOOLEAN"),
                new test("5 + true; 5;", "unknown operator: INTEGER + BOOLEAN"),
                new test("-true", "unknown operator: -BOOLEAN"),
                new test("true + false;", "unknown operator: BOOLEAN + BOOLEAN"),
                new test("5; true + false; 5", "unknown operator: BOOLEAN + BOOLEAN"),
                new test("if (10 > 1) { true + false; }", "unknown operator: BOOLEAN + BOOLEAN"),
                new test(
                        """
if (10 > 1) {
    if (10 > 1) {
        return true + false;
    }
    return 1;
}""", "unknown operator: BOOLEAN + BOOLEAN"
                ),
                new test("foobar", "identifier not found: foobar"),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (evaluated instanceof Error err) {
                assertEquals(test.expectedMessage(), err.getMessage(), String.format("For input: `%s`", test.input()));
            } else {
                System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
                System.err.println(evaluated.inspect());
                System.err.println(evaluated instanceof Error);
                fail(String.format("expecting an error for '%s' input", test.input()));
            }
        }

    }

    @Test
    public void shouldEvaluateLetStatements() {
        record test(String input, Integer expected) { }

        final test[] tests = {
            new test("let a = 5; a;", 5),
            new test("let a = 5 * 5; a;", 25),
            new test("let a = 5; let b = a; b;", 5),
            new test("let a = 5; let b = a; let c = a + b + 5; c;", 15),
        };

        for (final test test : tests) {

        }
    }

    @Test
    public void shouldEvalIntegerExpression() {
        record test(String input, int expected) {}

        final test[] tests = {
            new test("5", 5),
            new test("10", 10),
            new test("-5", -5),
            new test("-10", -10),
            new test("5 + 5 + 5 + 5 - 10", 10),
            new test("2 * 2 * 2 * 2 * 2", 32),
            new test("-50 + 100 + -50", 0),
            new test("5 * 2 + 10", 20),
            new test("5 + 2 * 10", 25),
            new test("20 + 2 * -10", 0),
            new test("50 / 2 * 2 + 10", 60),
            new test("2 * (5 + 10)", 30),
            new test("3 * 3 * 3 + 10", 37),
            new test("3 * (3 * 3) + 10", 37),
            new test("(5 + 10 * 2 + 15 / 3) * 2 + -10", 50),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (!isValidIntegerObject(evaluated, test.expected())) {
                fail();
            }
        }
    }

    private static Obj testEval(final String input) {
        final var lex = new Lexer(input);
        final Parser parser = new Parser(lex);
        final Program program = parser.parseProgram();
        final var env = new Environment();

        return Evaluator.eval(program, env);
    }

    private static boolean isValidIntegerObject(final Obj obj, final int expected) {
        final boolean ok = obj instanceof Int;
        if (!ok) {
            System.err.println("obj is not Int");
            return false;
        }

        final Int result = (Int) obj;
        if (expected != result.getValue()) {
            System.err.println(String.format("object has wrong value. got=%d, want=%d", result.getValue(), expected));
            return false;
        }

        return true;
    }

    private static boolean isValidBooleanObject(final Obj obj, final boolean expected) {
        final boolean ok = obj instanceof Bool;
        if (!ok) {
            System.err.println("obj is not Bool");
            return false;
        }

        final Bool result = (Bool) obj;
        if (result.getValue() != expected) {
            System.err.println(String.format("object has wrong value. got=%b, want=%b", result.getValue(), expected));
            return false;
        }

        return true;
    }

    @Test
    public void shouldEvaluateBooleanExpression() {
        record test(String input, boolean expected) {}

        final test[] tests = {
            new test("true", true),
            new test("false", false),
            new test("1 < 2", true),
            new test("1 > 2", false),
            new test("1 < 1", false),
            new test("1 > 1", false),
            new test("1 == 1", true),
            new test("1 != 1", false),
            new test("1 == 2", false),
            new test("1 != 2", true),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (!isValidBooleanObject(evaluated, test.expected())) {
                fail();
            }
        }
    }

    @Test
    public void shouldEvaluateBangOperator() {
        record test(String input, boolean expected) {}

        final test[] tests = {
            new test("!true", false),
            new test("!false", true),
            new test("!5", false),
            new test("!!true", true),
            new test("!!false", false),
            new test("!!5", true),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (!isValidBooleanObject(evaluated, test.expected())) {
                fail();
            }
        }
    }

    private static boolean isValidNullObject(final Obj obj) {
        if (!obj.equals(Literals.NULL)) {
            System.err.println("obj is not NULL");
            return false;
        }

        return true;
    }

    @Test
    public void shouldEvaluateIfExpressions() {
        record test(String input, Object expected) {}

        final test[] tests = {
            new test("if (true) { 10 }", 10),
            new test("if (false) { 10 }", null),
            new test("if (1) { 10 }", 10),
            new test("if (1 < 2) { 10 }", 10),
            new test("if (1 > 2) { 10 }", null),
            new test("if (1 > 2) { 10 } else { 20 }", 20),
            new test("if (1 < 2) { 10 } else { 20 }", 10),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (test.expected() instanceof Integer) {
                if (!isValidIntegerObject(evaluated, (Integer)test.expected())) {
                    fail();
                }
            } else {
                if (!isValidNullObject(evaluated)) {
                    fail();
                }
            }
        }
    }

    @Test
    public void shouldEvaluateReturnStatements() {
        record test(String input, Integer expected) {}

        final test[] tests = {
            new test("return 10;", 10),
            new test("return 10; 9;", 10),
            new test("return 2 * 5; 9;", 10),
            new test("9; return 2 * 5; 9;", 10),
            new test("""
                    if (10 > 1) {
                        if (10 > 1) {
                            return 10;
                        }
                        return 1;
                    }""", 10)
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (!isValidIntegerObject(evaluated, test.expected())) {
                fail(String.format("Failed with -> [%s]", test));
            }
        }

    }

}
