package com.monkeyj.parser;

import com.monkeyj.ast.LetStatement;
import com.monkeyj.ast.Statement;
import com.monkeyj.lexer.Lexer;
import org.junit.jupiter.api.Test;

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
}
