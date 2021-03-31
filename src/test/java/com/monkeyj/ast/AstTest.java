package com.monkeyj.ast;

import com.monkeyj.lexer.Lexer;
import com.monkeyj.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class AstTest {

    @Test
    public void testString() {
        final String INPUT = "let myVar = anotherVar;";
        final var lex = new Lexer(INPUT);
        final Parser parser = new Parser(lex);

        final var program = parser.parseProgram();
        if (program == null) {
            fail("Program.parseProgram() returned null.");
        }

        System.out.println(program.toString());
    }

}
