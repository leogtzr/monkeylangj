package com.monkeyj.parser;

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
        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);
        // p := New(l)

    }
}