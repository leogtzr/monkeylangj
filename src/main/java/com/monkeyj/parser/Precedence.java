package com.monkeyj.parser;

public final class Precedence {

    private Precedence() {}

    public static final int LOWEST = 1;
    public static final int EQUALS = 2;
    public static final int LESS_GREATER = 3;
    public static final int SUM = 4;
    public static final int PRODUCT = 5;
    public static final int PREFIX = 6;
    public static final int CALL = 7;

}
