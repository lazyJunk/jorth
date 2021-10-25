package net.lazyio.jorth.words;

import net.lazyio.jorth.util.IntPair;

import java.util.Map;

import static net.lazyio.jorth.Porth.log;
import static net.lazyio.jorth.util.StringUtils._int;

public class ComparisonWords {

    public static final IWordParser EQUAL = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d equal %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a == popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a == popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser NOT_EQUAL = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d != %d with index[%d && %d] returned %s. Set to index %d.\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, popPair.a != popPair.b, stackIndex);
        }
        stack.put(stackIndex, String.valueOf(popPair.a != popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser LESS = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d is less than %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a < popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a < popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser GREATER = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d is greater than %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a > popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a > popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser LESS_EQUAL = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d is less or equal to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a <= popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a <= popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser GREATER_EQUAL = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => %d is greater or equal to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a >= popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a >= popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    private static IntPair popPairFromStack(Map<Integer, String> stack, int index) {
        return new IntPair(_int(stack.get(index - 2)), _int(stack.get(index - 1)));
    }
}
