package com.monkeyj.repl;

import com.monkeyj.evaluator.Evaluator;
import com.monkeyj.lexer.Lexer;
import com.monkeyj.object.Environment;
import com.monkeyj.parser.Parser;

import java.util.List;
import java.util.Scanner;

public class RunEvaluatePrintLoop {

    private static final String PROMPT = ">> ";
    private static final String BANNER = """
             ____________________________
            < Welcome to monkeylang v1.0 >
             ----------------------------
                    \\   ^__^
                     \\  (oo)\\_______
                        (__)\\       )\\/\\
                            ||----w |
                            ||     ||
                        
            """;

    private static void printParserErrors(final List<String> errors) {
        errors.forEach(error -> System.out.printf("\t%s\n", error));
    }

    public static void main(final String[] args) {
        System.out.println(BANNER);
        System.out.print(PROMPT);

        final Scanner scanner = new Scanner(System.in);
        final var env = new Environment();

        while (scanner.hasNext()) {
            System.out.print(PROMPT);
            final String line = scanner.nextLine();
            System.out.printf("\tLine -> [%s]\n", line);

            final var lex = new Lexer(line);
            final Parser parser = new Parser(lex);
            final var program = parser.parseProgram();

            if (!parser.errors().isEmpty()) {
                printParserErrors(parser.errors());
                continue;
            }

            final var evaluated = Evaluator.eval(program, env);
            if (evaluated != null) {
                System.out.println(evaluated.inspect());
                System.out.println();
            }
        }
    }

}
