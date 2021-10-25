package net.lazyio.jorth;

import net.lazyio.jorth.words.WordParser;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.lazyio.jorth.util.StringUtils.*;

public class Porth {

    public enum DataType {WORD, STR, INT, CHAR, BOOL;}

    public static class Token {
        public DataType type;
        public String text;
        public HashMap<String, Integer> tokenLocs = new HashMap<>();

        public Token(DataType type, String text) {
            this.type = type;
            this.text = text;
        }
    }

    public static void sim(List<String> script, boolean verbose, boolean print) {
        Instant start = Instant.now();

        // FIXME: 23/10/2021 ImmutableList pls
        var tokens = scriptToOps(script);

        setupBlocks(tokens);

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
                var result = WordParser.forWord(token.text).parse(tokens, index, stack, stackIndex, verbose);
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
        log("==> Took: %sm:%ss:%sms\n", duration.toMinutes(), duration.toSeconds(), duration.toMillis());
    }

    private static void setupBlocks(List<Token> tokens) {

        Token lastIf = null;
        Token lastElse = null;

        for (int i = 0; i < tokens.size(); i++) {
            var currToken = tokens.get(i).text;
            if (currToken.equals(WordParser.IF)) {
                if (lastIf == null) {
                    lastIf = tokens.get(i);
                } else if (lastIf.tokenLocs.containsKey(WordParser.END)) {
                    lastIf = tokens.get(i);
                } else fatal("==> ERROR: unclosed [if] blocks. [0]");
            } else if (currToken.equals(WordParser.ELSE)) {
                if (lastIf != null) {
                    lastIf.tokenLocs.put(WordParser.ELSE, i);
                    lastElse = tokens.get(i);
                } else fatal("==> ERROR: [else] keyword can't be used without an [if]");
            } else if (currToken.equals(WordParser.END)) {
                if (lastIf != null) {
                    lastIf.tokenLocs.put(WordParser.END, i);
                    if (lastElse != null) lastElse.tokenLocs.put(WordParser.END, i);
                } else fatal("==> ERROR: [end] keyword can't be used without an [if]");
            }
        }

        if (lastIf != null && !lastIf.tokenLocs.containsKey(WordParser.END))
            fatal("==> ERROR: unclosed [if] blocks. [1]");
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

    public static void fatal(String msg, Object... args) {
        System.err.printf(msg, args);
        System.exit(-1);
    }
}
