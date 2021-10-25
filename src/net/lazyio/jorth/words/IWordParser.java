package net.lazyio.jorth.words;

import net.lazyio.jorth.Porth;
import net.lazyio.jorth.util.IntPair;

import java.util.List;
import java.util.Map;

public interface IWordParser {

    IntPair parse(List<Porth.Token> tokens, int currentIndex, Map<Integer, String> stack, int stackIndex, boolean verbose);
}
