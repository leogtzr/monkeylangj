package com.monkeyj.evaluator;

import com.monkeyj.object.Builtin;
import com.monkeyj.object.Int;
import com.monkeyj.object.Obj;
import com.monkeyj.object.Str;

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

                        return newError("argument to `len` not supported, got %s", firstArg.type());
                    }
            )
    );

}
