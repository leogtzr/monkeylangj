package com.monkeyj.ast;

import com.monkeyj.token.Token;
import com.monkeyj.token.TokenConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstTest {

    @Test
    public void testString() {
        final LetStatement letStmt = new LetStatement();
        letStmt.setToken(new Token(TokenConstants.LET, "let"));

        final Identifier identifier = new Identifier();
        identifier.setToken(new Token(TokenConstants.IDENT, "myVar"));
        identifier.setValue("myVar");

        letStmt.setName(identifier);

        final Identifier identifierExpression = new Identifier();
        identifierExpression.setToken(new Token(TokenConstants.IDENT, "anotherVar"));
        identifierExpression.setValue("anotherVar");

        letStmt.setValue(identifierExpression);

        final var program = new Program(List.of(letStmt));

        assertEquals("let myVar = anotherVar;", program.toString());
    }

}
