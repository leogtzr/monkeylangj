package com.monkeyj.evaluator;

import com.monkeyj.object.*;

import java.util.Map;

import static com.monkeyj.evaluator.Evaluator.newError;

public final class Builtins {

    private Builtins() {}

    public static final Map<String, Builtin> BUILTINS = Map.of(
            "len", new Builtin(
                    args -> {
                        if (args.size() != 1) {
                            return newError("wrong number of arguments. got=%d, want=1", args.size());
                        }

                        final Obj firstArg = args.get(0);

                        if (firstArg instanceof Str str) {
                            return new Int(str.getValue().length());
                        }

                        if (firstArg instanceof Array arr) {
                            return new Int(arr.getElements().size());
                        }

                        return newError("argument to `len` not supported, got %s", firstArg.type());
                    }
            )
    );

}
