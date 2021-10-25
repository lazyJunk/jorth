package net.lazyio.jorth.words;

import net.lazyio.jorth.util.IntPair;

import static net.lazyio.jorth.Porth.log;

public class ConditionWords {

    public static final IWordParser IF = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        var pop = stack.get(stackIndex - 1);

        if (pop == "false") {
            if (tokens.get(currentIndex).tokenLocs.containsKey(WordParser.ELSE)) {
                if (verbose)
                    log("    => [if] value with location %s returned false. Jumping to [else] with location %s.\n", currentIndex, tokens.get(currentIndex).tokenLocs.get(WordParser.ELSE));
                return new IntPair(tokens.get(currentIndex).tokenLocs.get(WordParser.ELSE), stackIndex);
            } else {
                if (verbose)
                    log("    => [if] value with location %s returned false. Jumping to [end] with location %s.\n", currentIndex, tokens.get(currentIndex).tokenLocs.get(WordParser.END));
                return new IntPair(tokens.get(currentIndex).tokenLocs.get(WordParser.END), stackIndex);
            }
        }
        return new IntPair(currentIndex, stackIndex);
    };

    public static final IWordParser ELSE = (tokens, currentIndex, stack, stackIndex, verbose) -> {

        if (tokens.get(currentIndex).tokenLocs.containsKey(WordParser.END)) {
            return new IntPair(tokens.get(currentIndex).tokenLocs.get(WordParser.END), stackIndex);
        }

        return new IntPair(currentIndex, stackIndex);
    };

    public static final IWordParser END = (tokens, currentIndex, stack, stackIndex, verbose) -> {
        return new IntPair(currentIndex, stackIndex);
    };
}
