package net.lazyio.porva;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static net.lazyio.porva.StringUtils.*;

public class Porth {

    public enum Keyword {
        IF("if"),
        ORELSE("orelse"),
        ELSE("else"),
        END("end"),
        WHILE("while"),
        DO("do"),
        MACRO("macro"),
        INCLUDE("include"),
        MEMORY("memory"),
        PROC("proc");

        public String name;

        Keyword(String name) {
            this.name = name;
        }
    }

    public enum Intrinsics {
        PRINT("print"),
        PLUS("+"),
        MINUS("-"),
        DIV("/"),
        MUL("*"),
        MOD("mod"),
        EQUAL("="),
        NOT_EQUAL("!="),
        LESS("<"),
        GREATER(">"),
        LESS_EQUAL("<="),
        GREATER_EQUAL(">="),
        DUP("dup"),
        DROP("drop"),
        ;

        public String name;

        Intrinsics(String name) {
            this.name = name;
        }

        public static boolean contains(String str) {
            return Arrays.stream(Intrinsics.values()).anyMatch(intrinsics -> intrinsics.name.equals(str));
        }

        public static Intrinsics from(String str) {
            return Arrays.stream(Intrinsics.values()).filter(i -> i.name.equals(str)).findFirst().get();
        }

    }

    public enum TokenType {
        WORD, STR, INT, KEYWORD, INTRINSIC, CHAR;

    }

    public record Token(TokenType type, String text) {
        public boolean isIntrinsic() {
            return Intrinsics.contains(text);
        }

    }

    public record IntPair(int a, int b) {
    }

    public static void sim(List<String> script, boolean verbose, boolean print) {
        Instant start = Instant.now();
        var stack = new HashMap<Integer, String>();
        var tokens = scriptToOps(script);
        int index = 0;
        while (index < tokens.size()) {
            var token = tokens.get(index);
            if (token.type == TokenType.STR) {
                if (verbose) log("  => Put STR on stack with index %d and content %s\n", index, token.text);
                stack.put(index, token.text);
                index++;
            } else if (token.type == TokenType.INT) {
                if (verbose) log("  => Put INT on stack with index %d and content %s\n", index, token.text);
                stack.put(index, token.text);
                index++;
            } else if (token.isIntrinsic()) {
                if (Intrinsics.from(token.text) == Intrinsics.PRINT) {
                    var s = stack.get(index - 1);
                    if (verbose) log("    => Print value on stack with index %d and content %s\n", index - 1, s);
                    if (print) System.out.println(isStr(s) ? removeQuotes(s) : s);
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.PLUS) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => Adding %d to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a + popPair.b);
                    stack.put(index, String.valueOf(popPair.a + popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.MINUS) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => Subtracting %d to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a - popPair.b);
                    stack.put(index, String.valueOf(popPair.a - popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.DIV) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => Dividing %d to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a / popPair.b);
                    stack.put(index, String.valueOf(popPair.a / popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.MUL) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => Multiplying %d to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a * popPair.b);
                    stack.put(index, String.valueOf(popPair.a * popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.MOD) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => Mod %d to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a % popPair.b);
                    stack.put(index, String.valueOf(popPair.a % popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.EQUAL) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d equal %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a == popPair.b);
                    stack.put(index, String.valueOf(popPair.a == popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.NOT_EQUAL) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d doesn't equal %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a != popPair.b);
                    stack.put(index, String.valueOf(popPair.a != popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.LESS) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d is less than %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a < popPair.b);
                    stack.put(index, String.valueOf(popPair.a < popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.GREATER) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d is greater than %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a > popPair.b);
                    stack.put(index, String.valueOf(popPair.a > popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.LESS_EQUAL) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d is less or equal to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a <= popPair.b);
                    stack.put(index, String.valueOf(popPair.a <= popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.GREATER_EQUAL) {
                    var popPair = popPairFromStack(stack, index);
                    if (verbose)
                        log("      => %d is greater or equal to %d with index[%d && %d] to index %d. Result %d\n", popPair.a, popPair.b, index - 2, index - 1, index, popPair.a >= popPair.b);
                    stack.put(index, String.valueOf(popPair.a >= popPair.b));
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.DUP) {
                    var pop = pop(stack, index);
                    if (pop == null) fatal("==> ERROR: [%s] can't be duped.\n", tokens.get(index - 1).text);
                    if (verbose) log("    => Removed %s from stack with index %d.\n", pop, index - 1);
                    stack.put(index - 1, pop);
                    stack.put(index, pop);
                    if (verbose) log("    => Dup %s from stack to index %d && %d.\n", pop, index - 1, index);
                    index++;
                } else if (Intrinsics.from(token.text) == Intrinsics.DROP) {
                    var pop = pop(stack, index);
                    tokens.remove(index);
                    tokens.remove(index - 1);
                    if (verbose) log("    => Drop %s from stack with index %d.\n", pop, index - 1);
                    index--;
                }
            } else {
                index++;
            }
        }
        if (verbose) log("Stack: %s\n", stack);
        Instant end = Instant.now();
        var duration = Duration.between(start, end);
        log("Took: %sm:%ss:%sms\n", duration.toMinutes(), duration.toSeconds(), duration.toMillis());
    }

    private static IntPair popPairFromStack(Map<Integer, String> stack, int index) {
        return new IntPair(_int(stack.get(index - 2)), _int(stack.get(index - 1)));
    }

    private static String pop(Map<Integer, String> stack, int index) {
        var pop = stack.get(index - 1);
        stack.remove(index - 1);
        return pop;
    }

    public static List<Token> scriptToOps(List<String> script) {
        List<Token> tokens = new ArrayList<>();
        for (String line : script) {
            for (String word : splitOnSpaceKeepingQuotes(line)) {
                TokenType tokenType;
                if (isStr(word)) tokenType = TokenType.STR;
                else if (isChar(word)) tokenType = TokenType.CHAR;
                else if (isNumber(word)) tokenType = TokenType.INT;
                else if (Intrinsics.contains(word)) tokenType = TokenType.INTRINSIC;
                else tokenType = TokenType.WORD;
                tokens.add(new Token(tokenType, word));
            }
        }
        return tokens;
    }

    private static void log(String msg, Object... args) {
        System.out.printf(msg, args);
    }

    private static void fatal(String msg, Object... args) {
        System.err.printf(msg, args);
        System.exit(-1);
    }
}
