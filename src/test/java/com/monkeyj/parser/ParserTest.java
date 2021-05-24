package com.monkeyj.parser;

import com.monkeyj.ast.*;
import com.monkeyj.lexer.Lexer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void shouldParseLetStatements() {
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
            assertTrue(testLetStatement(stmt, tests[i].expectedIdentifier), "error parsing let stmt");
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
    public void shouldParseLetStatements2() {
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
            assertTrue(testLetStatement(stmt, tests[i].expectedIdentifier), "error parsing let stmt");
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
            assertTrue(testLetStatement(stmt, tests[i].expectedIdentifier), "error parsing let stmt");
        }
    }

    @Test
    public void shouldParseLetStatements4() {
        record test(String input, String expectedIdentifier, Object expectedValue) {}

        final test[] tests = {
            new test("let x = 5;", "x", 5),
            new test("let y = true;", "y", true),
            new test("let foobar = y;", "foobar", "y")
        };

        final int EXPECTED_NUMBER_LET_STATEMENTS = 1;

        for (final test test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]"
                            , EXPECTED_NUMBER_LET_STATEMENTS, program.getStatements().size()));

            final var stmt = program.getStatements().get(0);
            assertTrue(testLetStatement(stmt, test.expectedIdentifier));

            assertTrue(stmt instanceof LetStatement, "program.statement[0] is not ast.LetStatement");
            final var val = ((LetStatement) stmt).getValue();

            assertTrue(testLiteralExpression(val, test.expectedValue));
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
    public void shouldParseReturnStatement2() {
        record test(String input, Object expectedValue) {}

        final test[] tests = {
            new test("return 5;", 5),
            new test("return true;", true),
            new test("return foobar;", "foobar")
        };

        final int EXPECTED_NUMBER_RETURN_STATEMENTS = 1;

        for (final test test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            assertEquals(
                    EXPECTED_NUMBER_RETURN_STATEMENTS, program.getStatements().size()
                    , String.format("program.Stmts does not contain %d statements, got=[%d]"
                            , EXPECTED_NUMBER_RETURN_STATEMENTS, program.getStatements().size()));

            final var stmt = program.getStatements().get(0);
            assertTrue(stmt instanceof ReturnStatement, "program.statement[0] is not ast.ReturnStatement");

            final var returnStmt = (ReturnStatement) stmt;
            assertEquals("return", returnStmt.tokenLiteral());
            assertTrue(testLiteralExpression(returnStmt.getReturnValue(), test.expectedValue));
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

            assertTrue(testIntegerLiteral(exp.getRight(), test.integerValue()));
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

            assertTrue(testLiteralExpression(exp.getRight(), test.value()));
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

            assertTrue(testIntegerLiteral(exp.getLeft(), test.leftValue()));
            assertEquals(test.operator(), exp.getOperator());
            assertTrue(testIntegerLiteral(exp.getRight(), test.rightValue));
            assertTrue(testInfixExpression(stmt.getExpression(), test.leftValue(), test.operator(), test.rightValue()));
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

            assertTrue(testInfixExpression(stmt.getExpression(), test.leftValue(), test.operator(), test.rightValue()));
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
            new test("3 < 5 == true", "((3 < 5) == true)"),
            new test("1 + (2 + 3) + 4", "((1 + (2 + 3)) + 4)"),
            new test("(5 + 5) * 2", "((5 + 5) * 2)"),
            new test("2 / (5 + 5)", "(2 / (5 + 5))"),
            new test("-(5 + 5)", "(-(5 + 5))"),
            new test("!(true == true)", "(!(true == true))"),
            new test("a + add(b * c) + d", "((a + add((b * c))) + d)"),
            new test("add(a, b, 1, 2 * 3, 4 + 5, add(6, 7 * 8))", "add(a, b, 1, (2 * 3), (4 + 5), add(6, (7 * 8)))"),
            new test("add(a + b + c * d / f + g)", "add((((a + b) + ((c * d) / f)) + g))"),
            new test("a * [1, 2, 3, 4][b * c] * d", "((a * ([1, 2, 3, 4][(b * c)])) * d)"),
            new test("add(a * b[2], b[1], 2 * [1, 2][1])", "add((a * (b[2])), (b[1]), (2 * ([1, 2][1])))"),
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
            final Expression exp
            , final Object leftValue
            , final String operator
            , final Object rightValue) {

        if (!(exp instanceof InfixExpression)) {
            System.err.println("exp is not InfixExpression");
            return false;
        }

        final InfixExpression infix = (InfixExpression) exp;
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

        if (!ident.tokenLiteral().equals(value)) {
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

    @Test
    public void shouldParseIfExpressions() {
        final String INPUT = "if (x < y) { x }";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
            1, program.getStatements().size()
            , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof IfExpression, "program.statement[0] is not ast.IfExpression");

        final var exp = (IfExpression) stmt.getExpression();

        if (!testInfixExpression(exp.getCondition(), "x", "<", "y")) {
            return;
        }

        assertEquals(
            1, exp.getConsequence().getStatements().size()
            , String.format("consequence is not 1 statements. got=%d\n", 1, exp.getConsequence().getStatements().size()));

        assertTrue(exp.getConsequence().getStatements().get(0) instanceof ExpressionStatement
                , "exp.conseq.stmt[0] is not ExpressionStatement");
        final ExpressionStatement consequence = (ExpressionStatement) exp.getConsequence().getStatements().get(0);

        if (!testIdentifier(consequence.getExpression(), "x")) {
            return;
        }
        assertTrue(exp.getAlternative() == null);
    }

    @Test
    public void shouldParseIfElseExpressions() {
        final String INPUT = "if (x < y) { x } else { y }";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
                1, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof IfExpression, "program.statement[0] is not ast.IfExpression");

        final var exp = (IfExpression) stmt.getExpression();

        if (!testInfixExpression(exp.getCondition(), "x", "<", "y")) {
            return;
        }

        assertEquals(
            1, exp.getConsequence().getStatements().size()
            , String.format("consequence is not 1 statements. got=%d\n", 1, exp.getConsequence().getStatements().size()));

        assertTrue(exp.getConsequence().getStatements().get(0) instanceof ExpressionStatement
                , "exp.conseq.stmt[0] is not ExpressionStatement");
        final ExpressionStatement consequence = (ExpressionStatement) exp.getConsequence().getStatements().get(0);

        if (!testIdentifier(consequence.getExpression(), "x")) {
            return;
        }

        assertEquals(
            1, exp.getAlternative().getStatements().size()
            , String.format("alternative.stmt is not 1 statements. got=%d\n", 1, exp.getAlternative().getStatements().size()));

        assertTrue(exp.getAlternative().getStatements().get(0) instanceof ExpressionStatement
                , "exp.conseq.stmt[0] is not ExpressionStatement");
        final ExpressionStatement alternative = (ExpressionStatement) exp.getAlternative().getStatements().get(0);

        assertTrue(testIdentifier(alternative.getExpression(), "y"));
    }

    @Test
    public void shouldParseFunctionLiterals() {
        final String INPUT = "fn(x, y) { x + y; }";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
            1, program.getStatements().size()
            , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof FunctionLiteral
                , "program.statement[0] is not ast.FunctionLiteral");

        final FunctionLiteral function = (FunctionLiteral) stmt.getExpression();
        assertEquals(
            2
            , function.getParameters().size()
            , String.format("function literal parameters wrong. want 2, got=%d", function.getParameters().size()));

        assertTrue(testLiteralExpression(function.getParameters().get(0), "x"));
        assertTrue(testLiteralExpression(function.getParameters().get(0), "x"));

        assertEquals(
            1, function.getBody().getStatements().size()
            , String.format("function.Body.Statements has not 1 statements. got=%d", 1, function.getBody().getStatements().size()));

        assertTrue(function.getBody().getStatements().get(0) instanceof ExpressionStatement);

        final ExpressionStatement bodyStmt = (ExpressionStatement) function.getBody().getStatements().get(0);
        assertTrue(testInfixExpression(bodyStmt.getExpression(), "x", "+", "y"));
    }

    @Test
    public void shouldParseFunctionParameters() {
        record test(String input, List<String> expectedParams) {}

        final test[] tests = {
            new test("fn() {};", List.of()),
            new test("fn(x) {};", List.of("x")),
            new test("fn(x, y, z) {};", List.of("x", "y", "z"))
        };

        for (final test test : tests) {
            final var lex = new Lexer(test.input());
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();
            checkParserErrors(parser);

            final var stmt = (ExpressionStatement) program.getStatements().get(0);
            final var function = (FunctionLiteral) stmt.getExpression();

            assertEquals(
                    test.expectedParams.size()
                    , function.getParameters().size()
                    , String.format("length parameters wrong. want %d, got=%d", test.expectedParams.size(), function.getParameters().size()));

            for (int i = 0; i < test.expectedParams.size(); i++) {
                final String ident = test.expectedParams().get(i);
                assertTrue(testLiteralExpression(function.getParameters().get(i), ident));
            }
        }
    }

    @Test
    public void shouldParseCallExpression() {
        final String INPUT = "add(1, 2 * 3, 4 + 5);";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
            1, program.getStatements().size()
            , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof CallExpression
                , "program.statement[0] is not ast.CallExpression");

        final CallExpression exp = (CallExpression) stmt.getExpression();
        assertTrue(testIdentifier(exp.getFunction(), "add"));

        final int numberOfFunctionArgs = exp.getArguments().size();
        assertEquals(3, numberOfFunctionArgs, String.format("wrong length of arguments. got=%d", numberOfFunctionArgs));

        assertTrue(testLiteralExpression(exp.getArguments().get(0), 1));
        assertTrue(testInfixExpression(exp.getArguments().get(1), 2, "*", 3));
        assertTrue(testInfixExpression(exp.getArguments().get(2), 4, "+", 5));
    }

    @Test
    public void shouldEvaluateLiteralExpression() {
        final String INPUT = "\"hello world\";";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertEquals(
                1, program.getStatements().size()
                , String.format("program.Stmts does not contain %d statements, got=[%d]", 1, program.getStatements().size()));

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement
                , "program.statement[0] is not ast.ExpressionStatement");
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof StringLiteral, "program.statement[0] is not ast.Bool");

        final StringLiteral strLiteral = (StringLiteral) stmt.getExpression();
        assertEquals(strLiteral.getValue(), "hello world");
    }

    @Test
    public void shouldParseArrayLiterals() {
        final String INPUT = "[1, 2 * 2, 3 + 3]";
        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement, "expecting a ExpressionStatement");

        final var stmt = (ExpressionStatement) program.getStatements().get(0);
        assertTrue(stmt.getExpression() instanceof ArrayLiteral, String.format("exp not ast.ArrayLiteral, got=%s", stmt.getExpression()));

        final var array = (ArrayLiteral) stmt.getExpression();
        assertEquals(
                3
                , array.getElements().size()
                , String.format("len(array.Elements) not 3. got=%d", array.getElements().size()));

        assertTrue(testIntegerLiteral(array.getElements().get(0), 1));
        assertTrue(testInfixExpression(array.getElements().get(1), 2, "*", 2));
        assertTrue(testInfixExpression(array.getElements().get(2), 3, "+", 3));
    }

    @Test
    public void shouldParseIndexExpressions() {
        final String INPUT = "myArray[1 + 1]";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
        assertTrue(
                stmt.getExpression() instanceof IndexExpression
                , String.format("exp not *ast.IndexExpression. got=%s", stmt.getExpression()));

        final var indexExp = (IndexExpression) stmt.getExpression();

        assertTrue(testIdentifier(indexExp.getLeft(), "myArray"));
        assertTrue(testInfixExpression(indexExp.getIndex(), 1, "+", 1));
    }

    @Test
    public void shouldParseHashLiteralsStringKeys() {
        final String INPUT = """
                {"one": 1, "two": 2, "three": 3}
                """;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
        assertTrue(
                stmt.getExpression() instanceof HashLiteral
                , String.format("exp not *ast.HashLiteral. got=%s", stmt.getExpression()));

        final HashLiteral hash = (HashLiteral) stmt.getExpression();
        assertEquals(3, hash.getPairs().size());

        final var expected = Map.of(
            "one", 1,
            "two", 2,
            "three", 3
        );

        for (final Map.Entry<Expression, Expression> entry : hash.getPairs().entrySet()) {
            assertTrue(entry.getKey() instanceof StringLiteral, String.format("key is not a String literal, got=%s", entry.getKey()));
            final StringLiteral literal = (StringLiteral) entry.getKey();
            final var expectedValue = expected.get(literal.toString());

            assertTrue(testIntegerLiteral(entry.getValue(), expectedValue));
        }
    }

    @Test
    public void shouldParseEmptyHashLiteral() {
        final String INPUT = "{}";

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
        assertTrue(
                stmt.getExpression() instanceof HashLiteral
                , String.format("exp not *ast.HashLiteral. got=%s", stmt.getExpression()));

        final HashLiteral hash = (HashLiteral) stmt.getExpression();
        assertEquals(0, hash.getPairs().size());
    }

    @Test
    public void shouldParseHashLiteralsWithExpressions() {
        final String INPUT = """
                {"one": 0 + 1, "two": 10 - 8, "three": 15 / 5}
                """;

        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        final var program = parser.parseProgram();
        checkParserErrors(parser);

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        final ExpressionStatement stmt = (ExpressionStatement) program.getStatements().get(0);
        assertTrue(
                stmt.getExpression() instanceof HashLiteral
                , String.format("exp not *ast.HashLiteral. got=%s", stmt.getExpression()));

        final HashLiteral hash = (HashLiteral) stmt.getExpression();
        assertEquals(3, hash.getPairs().size());

        record test(String hashKey, Integer left, String op, Integer right) {}

        final Map<String, test> expected = Map.of(
            "one", new test("one", 0, "+", 1),
            "two", new test("two", 10, "-", 8),
            "three", new test("three", 15, "/", 5)
        );

        for (final Map.Entry<Expression, Expression> entry : hash.getPairs().entrySet()) {
            assertTrue(entry.getKey() instanceof StringLiteral);
            assertTrue(entry.getValue() instanceof InfixExpression);

            final var literal = (StringLiteral) entry.getKey();
            final var test = expected.get(literal.toString());

            assertTrue(testInfixExpression(entry.getValue(), test.left(), test.op(), test.right()));
        }
    }

}
