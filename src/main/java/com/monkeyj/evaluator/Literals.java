package com.monkeyj.evaluator;

import com.monkeyj.object.Bool;
import com.monkeyj.object.Null;

public final class Literals {

    private Literals() {}

    public static final Bool TRUE = Bool.of(true);
    public static final Bool FALSE = Bool.of(false);
    public static final Null NULL = new Null();

}
