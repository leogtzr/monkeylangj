package com.monkeyj.token;

import java.util.Map;

public class Keywords {

    private Keywords() {}

    public static final Map<String, String> KEYWORDS = Map.of(
        "fn", TokenConstants.FUNCTION,
        "let", TokenConstants.LET,
        "true", TokenConstants.TRUE,
        "false", TokenConstants.FALSE,
        "if", TokenConstants.IF,
        "else", TokenConstants.ELSE,
        "return", TokenConstants.RETURN
    );

    public static String lookupIdentifier(final String identifier) {
        return KEYWORDS.getOrDefault(identifier, TokenConstants.IDENT);
    }

}
