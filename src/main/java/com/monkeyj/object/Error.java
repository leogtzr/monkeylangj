package com.monkeyj.object;

import java.util.Objects;

public class Error implements Obj {

    private String message;

    public Error(final String message) {
        this.message = message;
    }

    @Override
    public String type() {
        return ObjConstants.ERROR_OBJ;
    }

    @Override
    public String inspect() {
        return "ERROR: " + this.message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {

        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Error error = (Error) o;
        return Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                '}';
    }
}