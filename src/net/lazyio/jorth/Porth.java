package net.lazyio.jorth;

import net.lazyio.jorth.util.IntPair;
import net.lazyio.jorth.words.WordParser;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.lazyio.jorth.StringUtils.*;

public class Porth {

    public enum DataType {WORD, STR, INT, CHAR, BOOL;}

    public record Token(DataType type, String text) {
    }

    public static void sim(List<String> script, boolean verbose, boolean print) {
        Instant start = Instant.now();

        // FIXME: 23/10/2021 ImmutableList pls
        var tokens = scriptToOps(script);

        var blocks = setupBlocks(tokens);

        int stackIndex = 0;
        var stack = new HashMap<Integer, String>();

        int index = 0;
        while (index < tokens.size()) {
            var token = tokens.get(index);
            if (token.type == DataType.STR) {
                if (verbose) log("  => Put STR on stack with index %d and content %s\n", stackIndex, token.text);
                stack.put(stackIndex, token.text);
                stackIndex++;
            } else if (token.type == DataType.INT) {
                if (verbose) log("  => Put INT on stack with index %d and content %s\n", stackIndex, token.text);
                stack.put(stackIndex, token.text);
                stackIndex++;
            } else if (WordParser.containsWord(token.text)) {
                var result = WordParser.forWord(token.text).parse(tokens, index, blocks, stack, stackIndex, verbose);
                index = result.a;
                stackIndex = result.b;
            } else {
                fatal("==> ERROR: Unknown word [%s].", token.text);
            }
            index++;
        }
        if (verbose) log("Stack: %s\n", stack);
        Instant end = Instant.now();
        var duration = Duration.between(start, end);
        log("Took: %sm:%ss:%sms\n", duration.toMinutes(), duration.toSeconds(), duration.toMillis());
    }

    private static List<IntPair> setupBlocks(List<Token> tokens) {
        var blocks = new ArrayList<IntPair>();

        int lastEnd = -1;
        int lastIf = -1;

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).text.equals("if")) {
                if (lastEnd == -1) lastIf = i;
            } else if (tokens.get(i).text.equals("end")) {
                if (lastIf != -1) {
                    blocks.add(new IntPair(lastIf, i));
                    lastIf = -1;
                    lastEnd = -1;
                } else fatal("==> ERROR: [end] can only be used to close blocks.\n");
            }
        }

        //if (lastEnd != 1 && lastIf == -1) fatal("==> ERROR: [end] can only be used to close blocks.\n");

        return blocks;
    }


    public static List<Token> scriptToOps(List<String> script) {
        List<Token> tokens = new ArrayList<>();
        for (String line : script) {
            for (String word : splitOnSpaceKeepingQuotes(line)) {
                DataType dataType;
                if (isStr(word)) dataType = DataType.STR;
                else if (isChar(word)) dataType = DataType.CHAR;
                else if (isNumber(word)) dataType = DataType.INT;
                else dataType = DataType.WORD;
                tokens.add(new Token(dataType, word));
            }
        }
        return tokens;
    }

    public static void log(String msg, Object... args) {
        System.out.printf(msg, args);
    }

    private static void fatal(String msg, Object... args) {
        System.err.printf(msg, args);
        System.exit(-1);
    }
}
