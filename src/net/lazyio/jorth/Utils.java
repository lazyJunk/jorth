package net.lazyio.jorth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static net.lazyio.jorth.Porth.log;
import static net.lazyio.jorth.StringUtils.*;

public class Utils {

    public static Map<String, IJorthParser> parsers = new HashMap<>();

    static {
        parsers.put("print", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var s = stack.get(stackIndex - 1);
            if (verbose) log("    => Print value on stack with index %d and content %s\n", stackIndex - 1, s);
            System.out.println(isStr(s) ? removeQuotes(s) : s);
            return new IntPair(currentIndex, stackIndex);
        });
        parsers.put("dup", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var pop = stack.get(stackIndex - 1);
            if (pop == null) log("==> ERROR: [%s] can't be duped.\n", stack.get(stackIndex - 1));
            stack.put(stackIndex - 1, pop);
            stack.put(stackIndex, pop);
            if (verbose) {
                log("    => Dup %s from stack index %d to index %d.\n", pop, stackIndex - 1, stackIndex);
            }
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("drop", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var pop = stack.get(stackIndex - 1);
            if (verbose) log("    => Drop %s from stack with index %d.\n", pop, stackIndex - 1);
            stack.remove(stackIndex - 1);
            return new IntPair(currentIndex, stackIndex - 1);
        });
        //loop
        parsers.put("while", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var whileIndex = tokens.indexOf(tokens.stream().filter(t -> t.text().equals("while")).findFirst().get());
            var doIndex = tokens.indexOf(tokens.stream().filter(t -> t.text().equals("do")).findFirst().get());
            var endIndex = tokens.indexOf(tokens.stream().filter(t -> t.text().equals("end")).findFirst().get());

            var tmpStackIndex = 0;
            var tmpStack = new HashMap<Integer, String>();

            for (int i = whileIndex + 1; i < doIndex; i++) {
                tmpStack.put(tmpStackIndex, tokens.get(i).text());
                tmpStackIndex++;
            }

            int ii = 0;
            while (ii < tmpStackIndex) {
                parsers.get(tmpStack.get(ii - 1)).parse(Collections.emptyList(), currentIndex, blocks, tmpStack, tmpStackIndex, false);
                ii++;
            }

            System.out.println(tmpStack.get(tmpStackIndex));


            for (int i = endIndex; i >= whileIndex; i--) {
                tokens.remove(i);
            }

            return new IntPair(currentIndex, stackIndex - 1);
        });
        //MathOperations
        parsers.put("+", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => Adding %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a + popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a + popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("-", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => Subtracting %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a - popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a - popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("/", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => Dividing %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a / popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a / popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("*", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => Multiplying %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a * popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a * popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("mod", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => Mod %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a % popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a % popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        //Comparison
        parsers.put("=", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d equal %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a == popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a == popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("!=", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d != %d with index[%d && %d] returned %s. Set to index %d.\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, popPair.a != popPair.b, stackIndex);
            }
            stack.put(stackIndex, String.valueOf(popPair.a != popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("<", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d is less than %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a < popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a < popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put(">", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d is greater than %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a > popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a > popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put("<=", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d is less or equal to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a <= popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a <= popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        parsers.put(">=", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var popPair = popPairFromStack(stack, stackIndex);
            if (verbose) {
                log("      => %d is greater or equal to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a >= popPair.b);
            }
            stack.put(stackIndex, String.valueOf(popPair.a >= popPair.b));
            return new IntPair(currentIndex, stackIndex + 1);
        });
        //conditions
        parsers.put("if", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            var pop = stack.get(stackIndex - 1);
            int tmp = currentIndex;
            var end = blocks.stream().filter(intPair -> intPair.a == tmp).findFirst().get().b;
            if (pop == "false") {
                if (verbose)
                    log("    => [if] value with location %s returned false. Jumping to [end] with location %s.\n", currentIndex, end);
                currentIndex = end;
                return new IntPair(currentIndex, stackIndex);
            }
            return new IntPair(currentIndex, stackIndex);
        });
        parsers.put("else", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            return new IntPair(currentIndex, stackIndex);
        });
        parsers.put("end", (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
            return new IntPair(currentIndex, stackIndex);
        });
    }

    public record IntPair(int a, int b) {
    }

    private static IntPair popPairFromStack(Map<Integer, String> stack, int index) {
        return new IntPair(_int(stack.get(index - 2)), _int(stack.get(index - 1)));
    }
}
