package com.monkeyj.parser;

import com.monkeyj.ast.*;
import com.monkeyj.lexer.Lexer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void shouldParseLetStatements() {
        final String INPUT = """
let x = 5;
let y = 10;
let foobar = 838383;
""";
        final int EXPECTED_NUMBER_LET_STATEMENTS = 3;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        if (program == null) {
            fail("Program.parseProgram() returned null.");
        }

        assertEquals(
                EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

        record test(String expectedIdentifier) {}
        final test []tests = { new test("x"), new test("y"), new test("foobar") };

        for (int i = 0; i < tests.length; i++) {
            final var stmt = program.getStatements().get(i);
            if (!testLetStatement(stmt, tests[i].expectedIdentifier)) {
                fail("error parsing let stmt");
            }
        }

    }

    private boolean testLetStatement(final Statement stmt, final String name) {
        if (!stmt.tokenLiteral().equals("let")) {
            return false;
        }

        if (!(stmt instanceof LetStatement)) {
            return false;
        }

        final LetStatement letStmt = (LetStatement) stmt;
        if (!letStmt.getName().getValue().equals(name)) {
            return false;
        }

        return letStmt.getName().tokenLiteral().equals(name);
    }

    @Test
    void shouldParseLetStatements2() {
        final String INPUT = """
let x = 5;
let y = 10;
let foobar = 838383;
""";
        final int EXPECTED_NUMBER_LET_STATEMENTS = 3;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        checkParserErrors(parser);
        if (program == null) {
            fail("Program.parseProgram() returned null.");
        }

        assertEquals(
                EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

        record test(String expectedIdentifier) {}
        final test []tests = { new test("x"), new test("y"), new test("foobar") };

        for (int i = 0; i < tests.length; i++) {
            final var stmt = program.getStatements().get(i);
            if (!testLetStatement(stmt, tests[i].expectedIdentifier)) {
                fail("error parsing let stmt");
            }
        }

    }

    @Test
    void shouldParseLetStatements3() {
        // From:
        final String INPUT = """
let x = 5;
let y = 10;
let foobar = 838383;
""";

//        final String INPUT = """
//let x 5;
//let = 10;
//let 838383;
//""";

        final int EXPECTED_NUMBER_LET_STATEMENTS = 3;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        checkParserErrors(parser);
        if (program == null) {
            fail("Program.parseProgram() returned null.");
        }

        assertEquals(
                EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

        record test(String expectedIdentifier) {}
        final test []tests = { new test("x"), new test("y"), new test("foobar") };

        for (int i = 0; i < tests.length; i++) {
            final var stmt = program.getStatements().get(i);
            if (!testLetStatement(stmt, tests[i].expectedIdentifier)) {
                fail("error parsing let stmt");
            }
        }

    }

    private void checkParserErrors(final Parser parser) {
        final List<String> errors = parser.errors();

        if (errors.isEmpty()) {
            return;
        }

        System.out.printf("parser has %d errors", errors.size());

        for (final String error : errors) {
            System.out.printf("parser error: %s", error);
        }

        fail();
    }

    @Test
    public void shouldParseReturnStatement() {
        final String INPUT = """
return 5;
return 10;
return 993322;
""";
        final int EXPECTED_NUMBER_RETURN_STATEMENTS = 3;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        if (program == null) {
            fail("Program.parseProgram() returned null.");
        }

        assertEquals(
                EXPECTED_NUMBER_RETURN_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_RETURN_STATEMENTS, program.getStatements().size()));


        checkParserErrors(parser);

        for (final Statement stmt : program.getStatements()) {
            assertTrue(stmt instanceof ReturnStatement, String.format("stmt not *ast.ReturnStatement. got=%s", stmt.toString()));
            assertEquals("return", stmt.tokenLiteral(), String.format("returnStmt.TokenLiteral not 'return', got %s", stmt.tokenLiteral()));
        }

    }

    @Test
    public void shouldParseIdentifierExpression() {
        final String INPUT = "foobar;";

        final int EXPECTED_NUMBER_LET_STATEMENTS = 1;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
                EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof Identifier, "exp not ast.Identifier");

        final Identifier ident = (Identifier) stmt.getExpression();
        assertEquals("foobar", ident.getValue());
        assertEquals("foobar", ident.tokenLiteral());
    }

    @Test
    public void shouldValidateIntegerLiteralExpression() {
        final String INPUT = "5;";

        final int EXPECTED_NUMBER_LET_STATEMENTS = 1;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
                EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]"
                        , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof IntegerLiteral, "exp not ast.IntegerLiteral");

        final IntegerLiteral literal = (IntegerLiteral) stmt.getExpression();
        assertEquals(5, literal.getValue());
        assertEquals("5", literal.tokenLiteral());
    }

    @Test
    public void shouldParsePrefixExpressions() {
        record prefixTest(String input, String operator, Integer integerValue) {}
        final prefixTest[] prefixTests = {
                new prefixTest("!5;", "!", 5),
                new prefixTest("-15;", "-", 15)
        };

        for (final prefixTest test : prefixTests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);

            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    1, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

            assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                    , "program.statement[0] is not ast.ExpressionStatement");
            final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

            assertTrue(stmt.getExpression() instanceof PrefixExpression, "exp not ast.PrefixExpression");

            final PrefixExpression exp = (PrefixExpression) stmt.getExpression();
            assertEquals(test.operator(), exp.getOperator());

            if (!testIntegerLiteral(exp.getRight(), test.integerValue())) {
                fail();
            }
        }
    }

    @Test
    public void shouldParsePrefixExpressions2() {
        record prefixTest(String input, String operator, Object value) {}
        final prefixTest[] prefixTests = {
            new prefixTest("!5;", "!", 5),
            new prefixTest("-15;", "-", 15),
            new prefixTest("!true;", "!", true),
            new prefixTest("!false", "!", false)
        };

        for (final prefixTest test : prefixTests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);

            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    1, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

            assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                    , "program.statement[0] is not ast.ExpressionStatement");
            final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

            assertTrue(stmt.getExpression() instanceof PrefixExpression, "exp not ast.PrefixExpression");

            final PrefixExpression exp = (PrefixExpression) stmt.getExpression();
            assertEquals(test.operator(), exp.getOperator());

            if (!testLiteralExpression(exp.getRight(), test.value())) {
                fail();
            }
        }
    }

    @Test
    public void shouldParseInfixExpressions() {
        record infixTest(String input, int leftValue, String operator, int rightValue) {}

        final infixTest[] tests = {
            new infixTest("5 + 5;", 5, "+", 5),
            new infixTest("5 - 5;", 5, "-", 5),
            new infixTest("5 * 5;", 5, "*", 5),
            new infixTest("5 / 5;", 5, "/", 5),
            new infixTest("5 > 5;", 5, ">", 5),
            new infixTest("5 < 5;", 5, "<", 5),
            new infixTest("5 == 5;", 5, "==", 5),
            new infixTest("5 != 5;", 5, "!=", 5)
        };

        for (final infixTest test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    1, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

            assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                    , "program.statement[0] is not ast.ExpressionStatement");
            final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
            assertTrue(stmt.getExpression() instanceof InfixExpression, "exp not ast.PrefixExpression");

            final InfixExpression exp = (InfixExpression) stmt.getExpression();

            if (!testIntegerLiteral(exp.getLeft(), test.leftValue())) {
                fail();
            }

            assertEquals(test.operator(), exp.getOperator());

            if (!testIntegerLiteral(exp.getRight(), test.rightValue)) {
                fail();
            }

            if (!testInfixExpression(stmt, test.leftValue(), test.operator(), test.rightValue())) {
                fail();
            }
        }
    }

    @Test
    public void shouldParseInfixExpressions2() {
        record infixTest(String input, Object leftValue, String operator, Object rightValue) {}

        final infixTest[] tests = {
                new infixTest("5 + 5;", 5, "+", 5),
                new infixTest("5 - 5;", 5, "-", 5),
                new infixTest("5 * 5;", 5, "*", 5),
                new infixTest("5 / 5;", 5, "/", 5),
                new infixTest("5 > 5;", 5, ">", 5),
                new infixTest("5 < 5;", 5, "<", 5),
                new infixTest("5 == 5;", 5, "==", 5),
                new infixTest("5 != 5;", 5, "!=", 5),
                new infixTest("true == true", true, "==", true),
                new infixTest("true != false", true, "!=", false),
                new infixTest("false == false", Boolean.FALSE, "==", Boolean.FALSE)
        };

        for (final infixTest test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    1, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

            assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                    , "program.statement[0] is not ast.ExpressionStatement");
            final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
            assertTrue(stmt.getExpression() instanceof InfixExpression, "exp not ast.PrefixExpression");

            if (!testInfixExpression(stmt, test.leftValue(), test.operator(), test.rightValue())) {
                fail();
            }
        }
    }

    @Test
    public void shouldValidatePrecedenceString() {
        record test(String input, String expected) {}

        final test[] tests = {
            new test("!-a", "(!(-a))"),
            new test("a + b + c", "((a + b) + c)"),
            new test("!-a", "(!(-a))"),
            new test("a + b + c", "((a + b) + c)"),
            new test("a + b - c", "((a + b) - c)"),
            new test("a * b * c", "((a * b) * c)"),
            new test("a * b / c", "((a * b) / c)"),
            new test("a + b / c", "(a + (b / c))"),
            new test("a + b * c + d / e - f", "(((a + (b * c)) + (d / e)) - f)"),
            new test("3 + 4; -5 * 5", "(3 + 4)((-5) * 5)"),
            new test("5 > 4 == 3 < 4", "((5 > 4) == (3 < 4))"),
            new test("5 < 4 != 3 > 4", "((5 < 4) != (3 > 4))"),
            new test("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"),
            new test("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"),
            new test("true","true"),
            new test("false", "false"),
            new test("3 > 5 == false", "((3 > 5) == false)"),
            new test("3 < 5 == true", "((3 < 5) == true)")
        };

        for (final test test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(test.expected(), program.toString());
        }
    }

    private static boolean testInfixExpression(
            final ExpressionStatement exp
            , final Object leftValue
            , final String operator
            , final Object rightValue) {

        if (!(exp.getExpression() instanceof InfixExpression)) {
            System.err.println("exp is not InfixExpression");
            return false;
        }

        final InfixExpression infix = (InfixExpression) exp.getExpression();
        if (!testLiteralExpression(infix.getLeft(), leftValue)) {
            return false;
        }

        if (!infix.getOperator().equals(operator)) {
            System.err.printf("exp.Operator is not '%s', got=%s\n", operator, infix.getOperator());
            return false;
        }

        if (!testLiteralExpression(infix.getRight(), rightValue)) {
            return false;
        }

        return true;
    }

    private static boolean testIntegerLiteral(final Expression il, final Integer value) {
        if (!(il instanceof IntegerLiteral)) {
            System.err.println("il not ast.IntegerLiteral");
            return false;
        }

        final IntegerLiteral integ = (IntegerLiteral) il;

        if (integ.getValue() != value) {
            System.err.printf("integ.Value not %d. got=%d", value, integ.getValue());
            return false;
        }
        if (!integ.tokenLiteral().equals(String.format("%d", value))) {
            System.err.printf("integ.TokenLiteral not %d. got=%s", value, integ.tokenLiteral());
            return false;
        }
        return true;
    }

    private static boolean testIdentifier(final Expression exp, final String value) {
        if (!(exp instanceof Identifier)) {
            System.err.println("il not ast.Identifier");
            return false;
        }

        final Identifier ident = (Identifier) exp;
        if (!ident.getValue().equals(value)) {
            System.err.printf("integ.Value not %s. got=%s", value, ident.getValue());
            return false;
        }

        if (!ident.tokenLiteral().equals(String.format("%d", value))) {
            System.err.printf("integ.TokenLiteral not %s. got=%s", value, ident.tokenLiteral());
            return false;
        }

        return true;
    }

    private static boolean testLiteralExpression(final Expression exp, final Object value) {
        if (value instanceof Integer) {
            return testIntegerLiteral(exp, (Integer) value);
        }
        if (value instanceof String) {
            return testIdentifier(exp, (String) value);
        }
        if (value instanceof Boolean) {
            return testBooleanLiteral(exp, (Boolean) value);
        }
        System.err.printf("type of exp not handled -> [%s]", exp.toString());
        return false;
    }

    private static boolean testBooleanLiteral(final Expression exp, final Boolean value) {
        if (!(exp instanceof Bool)) {
            System.err.println("il not ast.Boolean");
            return false;
        }
        final Bool b = (Bool) exp;
        if (b.getValue() != value) {
            System.err.printf("b.value not %b, got=%b", value, b.getValue());
            return false;
        }

        if (!b.tokenLiteral().equals(String.format("%b", value))) {
            System.err.printf("bool.TokenLiteral not %s. got=%s", value, b.tokenLiteral());
            return false;
        }

        return true;
    }

    @Test
    public void shouldParseBooleanExpressions() {
        record test(String input, boolean expectedBoolean) {}
        final test[] tests = {
            new test("true;", true),
            new test("false;", false)
        };

        for (final test test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    1, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

            assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                    , "program.statement[0] is not ast.ExpressionStatement");
            final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

            assertTrue(stmt.getExpression() instanceof Bool, "program.statement[0] is not ast.Bool");

            final Bool b = (Bool) stmt.getExpression();
            assertEquals(test.expectedBoolean(), b.getValue());
        }
    }


}
