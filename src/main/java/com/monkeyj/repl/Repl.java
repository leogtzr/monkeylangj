package com.monkeyj.repl;

import com.monkeyj.evaluator.Evaluator;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.parser.Parser;

import java.util.List;
import java.util.Scanner;

public class Repl {

    private static void printParserErrors(final List<String> errors) {
        for (final String error : errors) {
            System.out.printf("\t%s\n", error);
        }
    }

    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            System.out.printf("\tLine -> [%s]\n", line);

            final var lex = new Lexer(line);
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();

            if (!parser.errors().isEmpty()) {
                printParserErrors(parser.errors());
                continue;
            }

            final var evaluated = Evaluator.eval(program);
            if (evaluated != null) {
                System.out.println(program.toString());
                System.out.println();
            }
        }
    }

}
