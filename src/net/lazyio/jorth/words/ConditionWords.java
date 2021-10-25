package net.lazyio.jorth.words;

import net.lazyio.jorth.util.IntPair;

import static net.lazyio.jorth.Porth.log;

public class ConditionWords {

    public static final IWordParser IF = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
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
    };

    public static final IWordParser ELSE = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        return new IntPair(currentIndex, stackIndex);
    };

    public static final IWordParser END = (tokens, currentIndex, blocks, stack, stackIndex, verbose) -> {
        return new IntPair(currentIndex, stackIndex);
    };
}
