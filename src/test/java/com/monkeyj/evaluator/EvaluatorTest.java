package com.monkeyj.evaluator;

import com.monkeyj.ast.Program;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.object.Bool;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;
import com.monkeyj.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

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

        return Evaluator.eval(program);
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

}
