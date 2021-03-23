package com.monkeyj.token;

import java.util.Map;

public class Keywords {

    private Keywords() {}

    public static final Map<String, String> KEYWORDS = Map.of(
        "fn", TokenConstants.FUNCTION,
        "let", TokenConstants.LET
    );

    public static String lookupIdentifier(final String ident) {
        return KEYWORDS.getOrDefault(ident, TokenConstants.IDENT);
    }

}
