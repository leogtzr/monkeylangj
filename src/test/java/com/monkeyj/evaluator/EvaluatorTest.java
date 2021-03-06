package com.monkeyj.evaluator;

import com.monkeyj.ast.Program;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.object.*;
import com.monkeyj.object.Error;
import com.monkeyj.parser.Parser;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
                new test("""
                        "Hello" - "World"
                        """, "unknown operator: STRING - STRING"),
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
            final var evaluated = testEval(test.input());
            assertTrue(isValidIntegerObject(evaluated, test.expected()));
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
            assertTrue(isValidIntegerObject(evaluated, test.expected()));
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
            System.err.printf("object has wrong value. got=%b, want=%b\n", result.getValue(), expected);
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
            new test("(1 + 2) * 3 == 3 + 2 * 3", true),
            new test("(10 + 2) * 30 == 300 + 20 * 3", true),
            new test("(5 > 5 == true) != false", false),
            new test("500 / 2 != 250", false),
            new test("3 + 4 * 5 == 3 * 1 + 4 * 5", true),
            new test("5 * 10 > 40 + 5", true)
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            assertTrue(isValidBooleanObject(evaluated, test.expected()));
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
            assertTrue(isValidBooleanObject(evaluated, test.expected()));
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
                assertTrue(isValidIntegerObject(evaluated, (Integer)test.expected()));
            } else {
                assertTrue(isValidNullObject(evaluated));
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
            assertTrue(isValidIntegerObject(evaluated, test.expected()), String.format("Failed with -> [%s]", test));
        }
    }

    @Test
    public void shouldEvaluateFunctionObject() {
        final String INPUT = "fn(x) { x + 2; };";
        final String EXPECTED_BODY = "(x + 2)";
        final String EXPECTED_PARAMETER_NAME = "x";

        final var evaluated = testEval(INPUT);
        assertTrue(evaluated instanceof Function, String.format("object is not Function, got=%s", evaluated));

        final Function fn = (Function) evaluated;
        assertEquals(1
                , fn.getParameters().size()
                , String.format("function has wrong parameters. Parameters=%s", fn.getParameters()));

        assertEquals(EXPECTED_PARAMETER_NAME
                , fn.getParameters().get(0).toString()
                , String.format("parameter is not 'x'. got=%s", fn.getParameters().get(0)));

        assertEquals(EXPECTED_BODY, fn.getBody().toString(), String.format("got=%s", fn.getBody().toString()));
    }

    @Test
    public void shouldEvaluateFunctionApplication() {
        record test(String input, Integer expected) {}

        final test[] tests = {
            new test("let identity = fn(x) { x; }; identity(5);", 5),
            new test("let identity = fn(x) { return x; }; identity(5);", 5),
            new test("let double = fn(x) { x * 2; }; double(5);", 10),
            new test("let add = fn(x, y) { x + y; }; add(5, 5);", 10),
            new test("let add = fn(x, y) { x + y; }; add(5 + 5, add(5, 5));", 20),
            new test("fn(x) { x; }(5)", 5)
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            assertTrue(isValidIntegerObject(evaluated, test.expected()));
        }
    }

    @Test
    public void shouldEvaluateClosures() {
        final String INPUT = """
let newAdder = fn(x) {
    fn(y) { x + y };
};
let addTwo = newAdder(2);
addTwo(2);
                """;

        final var evaluated = testEval(INPUT);
        assertTrue(isValidIntegerObject(evaluated, 4));
    }

    @Test
    public void shouldEvaluateStringLiteral() {
        final String INPUT = """
                "Hello World!"
                """;
        final var evaluated = testEval(INPUT);
        assertTrue(evaluated instanceof Str, "object is not String, got => " + evaluated);
    }

    @Test
    public void shouldEvaluateStringConcatenation() {
        final String INPUT = """
                "Hello" + " " + "World!"
                """;
        final var evaluated = testEval(INPUT);
        assertTrue(evaluated instanceof Str, "object is not String, got => " + evaluated);

        final Str str = (Str) evaluated;
        assertEquals(
                "Hello World!"
                , str.getValue()
                , "String has wrong value, got=" + (str.getValue()));
    }

    @Test
    public void shouldEvaluateBuiltinFunction() {
        record test(String input, Object expected) {}

        final test[] tests = {
                new test("""
                        len("")
                        """, 0),
                new test("""
                        len([1, 5, 2])
                        """, 3),
                new test("""
                        last([1, 5, 2])
                        """, 2),
                new test("first(1)", "argument to `first` must be ARRAY, got INTEGER"),
                new test("""
                        len("four")
                        """, 4),
                new test("""
                        len("hello world")
                        """, 11),
                new test("len(1)", "argument to `len` not supported, got INTEGER"),
                new test("""
                        len("one", "two")
                        """, "wrong number of arguments. got=2, want=1")
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (test.expected() instanceof Integer i) {
                assertTrue(isValidIntegerObject(evaluated, (Integer) test.expected()));
            } else if (test.expected() instanceof String) {
                if (evaluated instanceof Error errObj) {
                    assertEquals(test.expected(), errObj.getMessage());
                } else {
                    fail(String.format("object is not Error, got=%s", evaluated));
                }
            }
        }
    }

    @Test
    public void shouldParseArrayLiterals() {
        final String INPUT = "[1, 2 * 2, 3 + 3]";
        final var evaluated = testEval(INPUT);

        assertTrue(evaluated instanceof Array, String.format("object is not Array. got=%s (%s)", evaluated, evaluated));

        final var result = (Array) evaluated;

        assertTrue(isValidIntegerObject(result.getElements().get(0), 1));
        assertTrue(isValidIntegerObject(result.getElements().get(1), 4));
        assertTrue(isValidIntegerObject(result.getElements().get(2), 6));
    }

    @Test
    public void shouldParseArrayIndexExpressions() {
        record test(String input, Object expected) {}

        final test[] tests = {
            new test("[1, 2, 3][0]", 1),
            new test("[1, 2, 3][1]", 2),
            new test("[1, 2, 3][2]", 3),
            new test("let i = 0; [1][i];", 1),
            new test("[1, 2, 3][1 + 1];", 3),
            new test("let myArray = [1, 2, 3]; myArray[2];", 3),
            new test("let myArray = [1, 2, 3]; myArray[0] + myArray[1] + myArray[2];", 6),
            new test("let myArray = [1, 2, 3]; let i = myArray[0]; myArray[i]", 2),
            new test("[1, 2, 3][3]", null),
            new test("[1, 2, 3][-1]", null)
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            if (test.expected() instanceof Integer) {
                assertTrue(isValidIntegerObject(evaluated, (Integer) test.expected()));
            } else {
                assertTrue(isValidNullObject(evaluated));
            }
        }
    }

    @Test
    public void shouldEvaluateRestBuiltinFunction() {
        record test(String input, String expected) {}

        final test[] tests = {
                new test("""
                    let a = [1, 2, 3, 4]; rest(a)
                """, "[2, 3, 4]"),
                new test("""
                    rest(1)
                """, "ERROR: argument to `rest` must be ARRAY, got INTEGER"),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            assertEquals(test.expected(), evaluated.inspect());
        }
    }

    @Test
    public void shouldEvaluatePushBuiltinFunction() {
        record test(String input, String expected) {}

        final test[] tests = {
                new test("""
                    let a = [1, 2, 3, 4]; let b = push(a, 98); b
                """, "[1, 2, 3, 4, 98]"),
                new test("""
                    push(1, 2)
                """, "ERROR: argument to `push` must be ARRAY, got INTEGER"),
        };

        for (final test test : tests) {
            final var evaluated = testEval(test.input());
            assertEquals(test.expected(), evaluated.inspect());
        }
    }

    @Test
    public void shouldEvaluateHashLiterals() {
        final String INPUT = """
                let two = "two";
                {
                    "one": 10 - 9,
                    two: 1 + 1,
                    "thr" + "ee": 6 / 2,
                    4: 4,
                    true: 5,
                    false: 6
                }
                """;

        final var evaluated = testEval(INPUT);
        assertTrue(evaluated instanceof Hash, String.format("Eval didn't return Hash. got=%s", evaluated));

        final Map<HashKey, Integer> expectedHashes = Map.of(
            Str.of("one").hashKey(), 1,
            Str.of("two").hashKey(), 2,
            Str.of("three").hashKey(), 3,
            Int.of(4).hashKey(), 4,
            Literals.TRUE.hashKey(), 5,
            Literals.FALSE.hashKey(), 6
        );

        final var result = (Hash) evaluated;
        assertEquals(expectedHashes.size(), result.getPairs().size());

        for (final var expected : expectedHashes.entrySet()) {
            final var pair = result.getPairs().get(expected.getKey());

            assertTrue(pair != null);
            assertTrue(isValidIntegerObject(pair.getValue(), expected.getValue()));
        }
    }

    @Test
    public void shouldEvaluateHashIndexExpressions() {
        record test(String input, Object expected) {}

        final test[] tests = {
            new test(
                    """
                            {"foo": 5}["foo"]
                            """, 5
            ),
            new test(
                    """
                            {"foo": 5}["bar"]
                            """,
                    null
            ),
            new test(
                    """
                            let key = "foo"; {"foo": 5}[key]
                            """,
                    5
            ),
            new test(
                    """
                            {}["foo"]
                            """,
                    null
            ),
            new test(
                    """
                            {5: 5}[5]
                            """,
                    5
            ),
            new test(
                    """
                            {true: 5}[true]
                            """,
                    5
            ),
            new test(
                    """
                            {false: 5}[false]
                            """,
                    5
            )
        };

        for (final var test : tests) {
            final var evaluated = testEval(test.input());
//            System.out.println(evaluated);
            if (test.expected() instanceof Integer i) {
                // System.out.println("Value: " + i);
                assertTrue(isValidIntegerObject(evaluated, i));
            } else {
                assertTrue(isValidNullObject(evaluated));
            }
        }

    }

}