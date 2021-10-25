package net.lazyio.jorth;

import java.util.List;
import java.util.Map;

public interface IJorthParser {

    Utils.IntPair parse(List<Porth.Token> tokens, int currentIndex, List<Utils.IntPair> blocks, Map<Integer, String> stack, int stackIndex, boolean verbose);
}
