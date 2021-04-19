package com.monkeyj.evaluator;

import com.monkeyj.object.Bool;
import com.monkeyj.object.Null;

public final class Literals {

    private Literals() {}

    public static final Bool TRUE = new Bool(true);
    public static final Bool FALSE = new Bool(false);
    public static final Null NULL = new Null();

}
