package com.monkeyj.parser;

import com.monkeyj.ast.*;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.token.Token;
import com.monkeyj.token.TokenConstants;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Parser {
    private final Lexer lexer;
    private final List<String> errors;
    private Token curToken;
    private Token peekToken;
    private Map<String, Supplier<Expression>> prefixParseFns;
    private Map<String, Function<Expression, Expression>> infixParseFns;

    private static final Map<String, Integer> PRECEDENCES = Map.of(
        TokenConstants.EQ, Precedence.EQUALS,
        TokenConstants.NOT_EQ, Precedence.EQUALS,
        TokenConstants.LT, Precedence.LESS_GREATER,
        TokenConstants.GT, Precedence.LESS_GREATER,
        TokenConstants.PLUS, Precedence.SUM,
        TokenConstants.MINUS, Precedence.SUM,
        TokenConstants.SLASH, Precedence.PRODUCT,
        TokenConstants.ASTERISK, Precedence.PRODUCT
    );

    public Parser(final Lexer lexer) {
        this.lexer = lexer;
        this.errors = new ArrayList<>();
        this.prefixParseFns = new HashMap<>();
        this.infixParseFns = new HashMap<>();

        // this.prefixParseFns.put(TokenConstants.IDENT, () -> this.parseIdentifier());
        this.registerPrefix(TokenConstants.IDENT, () -> this.parseIdentifier());
        this.registerPrefix(TokenConstants.INT, () -> this.parseIntegerLiteral());
        this.registerPrefix(TokenConstants.BANG, () -> this.parsePrefixExpression());
        this.registerPrefix(TokenConstants.MINUS, () -> this.parsePrefixExpression());
        this.registerPrefix(TokenConstants.TRUE, () -> this.parseBoolean());
        this.registerPrefix(TokenConstants.FALSE, () -> this.parseBoolean());

        this.registerInfix(TokenConstants.PLUS, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.MINUS, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.SLASH, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.ASTERISK, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.EQ, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.NOT_EQ, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.LT, ex -> this.parseInfixExpression(ex));
        this.registerInfix(TokenConstants.GT, ex -> this.parseInfixExpression(ex));

        this.nextToken();
        this.nextToken();
    }

    private Expression parseBoolean() {
        final var bool = new Bool();
        bool.setToken(this.curToken);
        bool.setValue(this.curTokenIs(TokenConstants.TRUE));
        return bool;
    }

    private Expression parseInfixExpression(final Expression left) {
        final var expression = new InfixExpression();
        expression.setToken(this.curToken);
        expression.setOperator(this.curToken.literal());
        expression.setLeft(left);

        final int precedence = this.curPrecedence();

        this.nextToken();
        expression.setRight(this.parseExpression(precedence));

        return expression;
    }

    private void registerPrefix(final String tokenType, final Supplier<Expression> fn) {
        this.prefixParseFns.put(tokenType, fn);
    }

    private void registerInfix(final String tokenType, final Function<Expression, Expression> fn) {
        this.infixParseFns.put(tokenType, fn);
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lexer.nextToken();
    }

    public Program parseProgram() {
        final var program = new Program();

        while (!this.curToken.type().equals(TokenConstants.EOF)) {
            final var stmt = this.parseStatement();
            if (stmt != null) {
                program.addStatement(stmt);
            }
            this.nextToken();
        }

        return program;
    }

    private Statement parseStatement() {
        switch (this.curToken.type()) {
            case TokenConstants.LET:
                return this.parseLetStatement();
            case TokenConstants.RETURN:
                return this.parseReturnStatement();
            default:
                return this.parseExpressionStatement();
        }
    }

    private ExpressionStatement parseExpressionStatement() {
        final var stmt = new ExpressionStatement();
        stmt.setToken(this.curToken);

        final Expression expression = this.parseExpression(Precedence.LOWEST);
        stmt.setExpression(expression);

        if (this.peekTokenIs(TokenConstants.SEMICOLON)) {
            this.nextToken();
        }

        return stmt;
    }

    private Expression parseIdentifier() {
        final var identifier = new Identifier();

        identifier.setToken(this.curToken);
        identifier.setValue(this.curToken.literal());

        return identifier;
    }

    private Expression parseIntegerLiteral() {
        final var literal = new IntegerLiteral();
        literal.setToken(this.curToken);

        try {
            literal.setValue(Integer.parseInt(this.curToken.literal()));
        } catch (final NumberFormatException ignored) {
            return null;
        }

        return literal;
    }

    private Expression parsePrefixExpression() {
        final var expression = new PrefixExpression();
        expression.setToken(this.curToken);
        expression.setOperator(this.curToken.literal());

        this.nextToken();
        expression.setRight(this.parseExpression(Precedence.PREFIX));

        return expression;
    }

    private Expression parseExpression(final int precedence) {
        final var currentToken = this.curToken;
        final var prefix = this.prefixParseFns.get(currentToken.type());
        if (prefix == null) {
            this.noPrefixParseFnError(currentToken.type());
            return null;
        }

        Expression leftExpression = prefix.get();

        // the loop:
        while (!this.peekTokenIs(TokenConstants.SEMICOLON) && (precedence < this.peekPrecedence())) {
            final var infix = this.infixParseFns.get(this.peekToken.type());
            if (infix == null) {
                return leftExpression;
            }

            this.nextToken();

            leftExpression = infix.apply(leftExpression);
        }

        return leftExpression;
    }

    private ReturnStatement parseReturnStatement() {
        final var returnStmt = new ReturnStatement();
        returnStmt.setToken(this.curToken);

        this.nextToken();

        // TODO: We're skipping the expression until we encounter a semicolon.
        while (!this.curTokenIs(TokenConstants.SEMICOLON)) {
            this.nextToken();
        }

        return returnStmt;
    }

    private LetStatement parseLetStatement() {
        final var letStmt = new LetStatement();
        letStmt.setToken(this.curToken);

        if (!this.expectPeek(TokenConstants.IDENT)) {
            return null;
        }

        final Identifier identifier = new Identifier(this.curToken, this.curToken.literal());
        letStmt.setName(identifier);

        if (!this.expectPeek(TokenConstants.ASSIGN)) {
            return null;
        }

        // TODO: We're skipping the expression until we encounter a semicolon.
        while (!this.curTokenIs(TokenConstants.SEMICOLON)) {
            this.nextToken();
        }

        return letStmt;
    }

    private boolean expectPeek(final String tokenType) {
        if (this.peekTokenIs(tokenType)) {
            this.nextToken();
            return true;
        }
        this.peekError(tokenType);
        return false;
    }

    private boolean peekTokenIs(final String tokenType) {
        return this.peekToken.type().equals(tokenType);
    }

    private boolean curTokenIs(final String tokenType) {
        return this.curToken.type().equals(tokenType);
    }

    public List<String> errors() {
        return Collections.unmodifiableList(this.errors);
    }

    private void peekError(final String tokenType) {
        final var msg = String.format("expected next token to be %s, got %s instead",
                tokenType, this.peekToken.type());
        this.errors.add(msg);
    }

    private void noPrefixParseFnError(final String tokenType) {
        final var msg = String.format("no prefix parse function for %s found",tokenType);
        this.errors.add(msg);
    }

    private int peekPrecedence() {
        return PRECEDENCES.getOrDefault(this.peekToken.type(), Precedence.LOWEST);
    }

    private int curPrecedence() {
        return PRECEDENCES.getOrDefault(this.curToken.type(), Precedence.LOWEST);
    }

}
