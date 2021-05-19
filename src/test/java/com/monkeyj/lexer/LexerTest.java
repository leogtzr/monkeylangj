package com.monkeyj.lexer;

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
            final var tok = lex.nextToken();
            assertEquals(tok.type(), test.expectedType);
            assertEquals(tok.literal(), test.expectedLiteral);
        }
    }

    @Test
    public void shouldRetrieveNextToken2() {

        final String INPUT = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
    x + y;
};

let result = add(five, ten);
""";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
            new test(LET, "let"),
            new test(IDENT, "five"),
            new test(ASSIGN, "="),
            new test(INT, "5"),
            new test(SEMICOLON, ";"),
            new test(LET, "let"),
            new test(IDENT, "ten"),
            new test(ASSIGN, "="),
            new test(INT, "10"),
            new test(SEMICOLON, ";"),
            new test(LET, "let"),
            new test(IDENT, "add"),
            new test(ASSIGN, "="),
            new test(FUNCTION, "fn"),
            new test(LPAREN, "("),
            new test(IDENT, "x"),
            new test(COMMA, ","),
            new test(IDENT, "y"),
            new test(RPAREN, ")"),
            new test(LBRACE, "{"),
            new test(IDENT, "x"),
            new test(PLUS, "+"),
            new test(IDENT, "y"),
            new test(SEMICOLON, ";"),
            new test(RBRACE, "}"),
            new test(SEMICOLON, ";"),
            new test(LET, "let"),
            new test(IDENT, "result"),
            new test(ASSIGN, "="),
            new test(IDENT, "add"),
            new test(LPAREN, "("),
            new test(IDENT, "five"),
            new test(COMMA, ","),
            new test(IDENT, "ten"),
            new test(RPAREN, ")"),
            new test(SEMICOLON, ";"),
            new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            final var tok = lex.nextToken();
            assertEquals(test.expectedType, tok.type());
            assertEquals(test.expectedLiteral, tok.literal());
        }
    }

    @Test
    public void shouldRetrieveNextToken3() {
        final String INPUT = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
    x + y;
};

let result = add(five, ten);
!-/*5;
5 < 10 > 5;
""";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
                new test(LET, "let"),
                new test(IDENT, "five"),
                new test(ASSIGN, "="),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "ten"),
                new test(ASSIGN, "="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "add"),
                new test(ASSIGN, "="),
                new test(FUNCTION, "fn"),
                new test(LPAREN, "("),
                new test(IDENT, "x"),
                new test(COMMA, ","),
                new test(IDENT, "y"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(IDENT, "x"),
                new test(PLUS, "+"),
                new test(IDENT, "y"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "result"),
                new test(ASSIGN, "="),
                new test(IDENT, "add"),
                new test(LPAREN, "("),
                new test(IDENT, "five"),
                new test(COMMA, ","),
                new test(IDENT, "ten"),
                new test(RPAREN, ")"),
                new test(SEMICOLON, ";"),
                new test(BANG, "!"),
                new test(MINUS, "-"),
                new test(SLASH, "/"),
                new test(ASTERISK, "*"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(GT, ">"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),

                new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            final var tok = lex.nextToken();
            assertEquals(test.expectedType, tok.type());
            assertEquals(test.expectedLiteral, tok.literal());
        }
    }

    @Test
    public void shouldRetrieveNextToken4() {
        final String INPUT = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
    x + y;
};

let result = add(five, ten);
!-/*5;
5 < 10 > 5;

if (5 < 10) {
    return true;
} else {
    return false;
}

10 == 10;
10 != 9;
""";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
                new test(LET, "let"),
                new test(IDENT, "five"),
                new test(ASSIGN, "="),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "ten"),
                new test(ASSIGN, "="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "add"),
                new test(ASSIGN, "="),
                new test(FUNCTION, "fn"),
                new test(LPAREN, "("),
                new test(IDENT, "x"),
                new test(COMMA, ","),
                new test(IDENT, "y"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(IDENT, "x"),
                new test(PLUS, "+"),
                new test(IDENT, "y"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "result"),
                new test(ASSIGN, "="),
                new test(IDENT, "add"),
                new test(LPAREN, "("),
                new test(IDENT, "five"),
                new test(COMMA, ","),
                new test(IDENT, "ten"),
                new test(RPAREN, ")"),
                new test(SEMICOLON, ";"),
                new test(BANG, "!"),
                new test(MINUS, "-"),
                new test(SLASH, "/"),
                new test(ASTERISK, "*"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(GT, ">"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(IF, "if"),
                new test(LPAREN, "("),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(TRUE, "true"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(ELSE, "else"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(FALSE, "false"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(INT, "10"),
                new test(EQ, "=="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(INT, "10"),
                new test(NOT_EQ, "!="),
                new test(INT, "9"),
                new test(SEMICOLON, ";"),

                new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            final var tok = lex.nextToken();
            assertEquals(test.expectedType, tok.type());
            assertEquals(test.expectedLiteral, tok.literal());
        }
    }

    @Test
    public void shouldRetrieveNextToken5() {
        final String INPUT = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
    x + y;
};

let result = add(five, ten);
!-/*5;
5 < 10 > 5;

if (5 < 10) {
    return true;
} else {
    return false;
}

10 == 10;
10 != 9
"foobar"
"foo bar"
""";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
                new test(LET, "let"),
                new test(IDENT, "five"),
                new test(ASSIGN, "="),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "ten"),
                new test(ASSIGN, "="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "add"),
                new test(ASSIGN, "="),
                new test(FUNCTION, "fn"),
                new test(LPAREN, "("),
                new test(IDENT, "x"),
                new test(COMMA, ","),
                new test(IDENT, "y"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(IDENT, "x"),
                new test(PLUS, "+"),
                new test(IDENT, "y"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "result"),
                new test(ASSIGN, "="),
                new test(IDENT, "add"),
                new test(LPAREN, "("),
                new test(IDENT, "five"),
                new test(COMMA, ","),
                new test(IDENT, "ten"),
                new test(RPAREN, ")"),
                new test(SEMICOLON, ";"),
                new test(BANG, "!"),
                new test(MINUS, "-"),
                new test(SLASH, "/"),
                new test(ASTERISK, "*"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(GT, ">"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(IF, "if"),
                new test(LPAREN, "("),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(TRUE, "true"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(ELSE, "else"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(FALSE, "false"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(INT, "10"),
                new test(EQ, "=="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(INT, "10"),
                new test(NOT_EQ, "!="),
                new test(INT, "9"),
                new test(STRING, "foobar"),
                new test(STRING, "foo bar"),

                new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            final var tok = lex.nextToken();
            assertEquals(test.expectedType, tok.type());
            assertEquals(test.expectedLiteral, tok.literal());
        }
    }

    @Test
    public void shouldRetrieveNextToken6() {
        final String INPUT = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
    x + y;
};

let result = add(five, ten);
!-/*5;
5 < 10 > 5;

if (5 < 10) {
    return true;
} else {
    return false;
}

10 == 10;
10 != 9
"foobar"
"foo bar"
[1, 2];
""";

        record test(String expectedType, String expectedLiteral) {}

        final test []tests = {
                new test(LET, "let"),
                new test(IDENT, "five"),
                new test(ASSIGN, "="),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "ten"),
                new test(ASSIGN, "="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "add"),
                new test(ASSIGN, "="),
                new test(FUNCTION, "fn"),
                new test(LPAREN, "("),
                new test(IDENT, "x"),
                new test(COMMA, ","),
                new test(IDENT, "y"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(IDENT, "x"),
                new test(PLUS, "+"),
                new test(IDENT, "y"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(SEMICOLON, ";"),
                new test(LET, "let"),
                new test(IDENT, "result"),
                new test(ASSIGN, "="),
                new test(IDENT, "add"),
                new test(LPAREN, "("),
                new test(IDENT, "five"),
                new test(COMMA, ","),
                new test(IDENT, "ten"),
                new test(RPAREN, ")"),
                new test(SEMICOLON, ";"),
                new test(BANG, "!"),
                new test(MINUS, "-"),
                new test(SLASH, "/"),
                new test(ASTERISK, "*"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(GT, ">"),
                new test(INT, "5"),
                new test(SEMICOLON, ";"),
                new test(IF, "if"),
                new test(LPAREN, "("),
                new test(INT, "5"),
                new test(LT, "<"),
                new test(INT, "10"),
                new test(RPAREN, ")"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(TRUE, "true"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(ELSE, "else"),
                new test(LBRACE, "{"),
                new test(RETURN, "return"),
                new test(FALSE, "false"),
                new test(SEMICOLON, ";"),
                new test(RBRACE, "}"),
                new test(INT, "10"),
                new test(EQ, "=="),
                new test(INT, "10"),
                new test(SEMICOLON, ";"),
                new test(INT, "10"),
                new test(NOT_EQ, "!="),
                new test(INT, "9"),
                new test(STRING, "foobar"),
                new test(STRING, "foo bar"),
                new test(LBRACKET, "["),
                new test(INT, "1"),
                new test(COMMA, ","),
                new test(INT, "2"),
                new test(RBRACKET, "]"),
                new test(SEMICOLON, ";"),
                new test(EOF, ""),
        };

        final var lex = new Lexer(INPUT);

        for (final test test : tests) {
            final var tok = lex.nextToken();
            assertEquals(test.expectedType, tok.type());
            assertEquals(test.expectedLiteral, tok.literal());
        }
    }

}
