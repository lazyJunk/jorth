package net.lazyio.jorth.words;

import net.lazyio.jorth.util.IntPair;

import java.util.Map;

import static net.lazyio.jorth.Porth.log;
import static net.lazyio.jorth.util.StringUtils._int;

public class ArithmeticWords {

    public static final IWordParser PLUS = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => Adding %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a + popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a + popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser MINUS = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => Subtracting %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a - popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a - popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser DIV = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => Dividing %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a / popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a / popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser TIMES = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => Multiplying %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a * popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a * popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser MOD = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var popPair = popPairFromStack(stack, stackIndex);
        if (verbose) {
            log("      => Mod %d to %d with index[%d && %d] to index %d. Result %s\n", popPair.a, popPair.b, stackIndex - 2, stackIndex - 1, stackIndex, popPair.a % popPair.b);
        }
        stack.put(stackIndex, String.valueOf(popPair.a % popPair.b));
        return new IntPair(currentIndex, stackIndex + 1);
    };

    private static IntPair popPairFromStack(Map<Integer, String> stack, int index) {
        return new IntPair(_int(stack.get(index - 2)), _int(stack.get(index - 1)));
    }
}
