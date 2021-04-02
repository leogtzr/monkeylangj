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
            assertTrue("return".equals(stmt.tokenLiteral()), String.format("returnStmt.TokenLiteral not 'return', got %s", stmt.tokenLiteral()));
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

}
