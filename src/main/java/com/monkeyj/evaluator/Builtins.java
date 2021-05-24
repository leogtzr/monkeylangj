package com.monkeyj.evaluator;

import com.monkeyj.object.*;

import java.util.ArrayList;
import java.util.List;
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
                        return Int.of(str.getValue().length());
                    }

                    if (firstArg instanceof Array arr) {
                        return Int.of(arr.getElements().size());
                    }

                    return newError("argument to `len` not supported, got %s", firstArg.type());
                }
        )
        , "first", new Builtin(
                args -> {
                    if (args.size() != 1) {
                        return newError("wrong number of arguments. got=%d, want=1", args.size());
                    }

                    final Obj firstArg = args.get(0);

                    if (!firstArg.type().equals(ObjConstants.ARRAY_OBJ)) {
                        return newError("argument to `first` must be ARRAY, got %s", firstArg.type());
                    }

                    final Array arr = (Array) firstArg;
                    if (arr.getElements().size() > 0) {
                        return arr.getElements().get(0);
                    }

                    return Literals.NULL;
                }
        )
            , "last", new Builtin(
                    args -> {
                        if (args.size() != 1) {
                            return newError("wrong number of arguments. got=%d, want=1", args.size());
                        }

                        final Obj firstArg = args.get(0);

                        if (!firstArg.type().equals(ObjConstants.ARRAY_OBJ)) {
                            return newError("argument to `last` must be ARRAY, got %s", firstArg.type());
                        }

                        final Array arr = (Array) firstArg;
                        final var length = arr.getElements().size();
                        if (length > 0) {
                            return arr.getElements().get(length - 1);
                        }

                        return Literals.NULL;
                    }
            )
            , "rest", new Builtin(
                args -> {
                    if (args.size() != 1) {
                        return newError("wrong number of arguments. got=%d, want=1", args.size());
                    }

                    if (!args.get(0).type().equals(ObjConstants.ARRAY_OBJ)) {
                        return newError("argument to `rest` must be ARRAY, got %s", args.get(0).type());
                    }

                    final var arr = (Array) args.get(0);
                    final int length = arr.getElements().size();

                    if (length > 0) {
                        final var newElements = arr.getElements().subList(1, length);
                        return new Array(newElements);
                    }

                    return Literals.NULL;
                }
            )
            , "push", new Builtin(
                    args -> {
                        if (args.size() != 2) {
                            return newError("wrong number of arguments. got=%d, want=2", args.size());
                        }

                        if (!args.get(0).type().equals(ObjConstants.ARRAY_OBJ)) {
                            return newError("argument to `push` must be ARRAY, got %s", args.get(0).type());
                        }

                        final var arr = (Array) args.get(0);

                        final List<Obj> newElements = new ArrayList<>(arr.getElements());
                        newElements.add(args.get(1));

                        return new Array(newElements);
                    }
            )
    );

}
