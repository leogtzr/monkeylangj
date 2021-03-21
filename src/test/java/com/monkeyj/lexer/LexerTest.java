package com.monkeyj.lexer;

import com.monkeyj.token.TokenConstants;
import org.junit.jupiter.api.Test;

import static com.monkeyj.token.TokenConstants.*;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void shouldRetrieveNextToken() {
        final String INPUT = "=+(){},;";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
            new test(ASSIGN, "="),
            new test(PLUS, "+"),
            new test(LPAREN, "("),
            new test(RPAREN, ")"),
            new test(LBRACE, "{"),
            new test(RBRACE, "}"),
            new test(COMMA, ","),
            new test(SEMICOLON, ";"),
            new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            var tok = lex.nextToken();
            assertEquals(tok.type(), test.expectedType);
            assertEquals(tok.literal(), test.expectedLiteral);
        }

    }

}
