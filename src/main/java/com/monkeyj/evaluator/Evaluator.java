package com.monkeyj.evaluator;

import com.monkeyj.ast.IntegerLiteral;
import com.monkeyj.ast.Node;
import com.monkeyj.ast.Program;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;

public final class Evaluator {

    private Evaluator() {}

    public static Obj eval(final Node node) {
        if (node instanceof IntegerLiteral) {
            final IntegerLiteral literal = (IntegerLiteral) node;
            final var intObj = new Int();
            intObj.setValue(literal.getValue());

            return intObj;
        }

        return null;
    }

}
