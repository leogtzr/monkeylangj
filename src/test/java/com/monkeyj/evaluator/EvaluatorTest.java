package com.monkeyj.evaluator;

import com.monkeyj.lexer.Lexer;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;
import com.monkeyj.parser.Parser;
import org.junit.jupiter.api.Test;

import static com.monkeyj.token.TokenConstants.*;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    @Test
    public void shouldEvalIntegerExpression() {
        record test(String input, int expected) {}

        final test[] tests = {
            new test("5", 5),
            new test("10", 10)
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
        final var program = parser.parseProgram();

        return Evaluator.eval(program);
    }

    public static boolean isValidIntegerObject(final Obj obj, final int expected) {
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

}
