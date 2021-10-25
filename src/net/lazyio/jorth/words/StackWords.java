package net.lazyio.jorth.words;

import net.lazyio.jorth.util.IntPair;

import static net.lazyio.jorth.Porth.log;
import static net.lazyio.jorth.StringUtils.isStr;
import static net.lazyio.jorth.StringUtils.removeQuotes;

public class StackWords {

    public static final IWordParser PRINT = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var s = stack.get(stackIndex - 1);
        if (verbose) log("    => Print value on stack with index %d and content %s\n", stackIndex - 1, s);
        System.out.println(isStr(s) ? removeQuotes(s) : s);
        return new IntPair(currentIndex, stackIndex);
    };

    public static final IWordParser DUP = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var pop = stack.get(stackIndex - 1);
        if (pop == null) log("==> ERROR: [%s] can't be duped.\n", stack.get(stackIndex - 1));
        stack.put(stackIndex - 1, pop);
        stack.put(stackIndex, pop);
        if (verbose) {
            log("    => Dup %s from stack index %d to index %d.\n", pop, stackIndex - 1, stackIndex);
        }
        return new IntPair(currentIndex, stackIndex + 1);
    };

    public static final IWordParser DROP = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        var pop = stack.get(stackIndex - 1);
        if (verbose) log("    => Drop %s from stack with index %d.\n", pop, stackIndex - 1);
        stack.remove(stackIndex - 1);
        return new IntPair(currentIndex, stackIndex - 1);
    };
}
