package net.lazyio.jorth.words;

import java.util.HashMap;
import java.util.Map;

public class WordParser {

    private static final Map<String, IWordParser> WORD_PARSERS = new HashMap<>();

    public static final String EQUAL = "=";
    public static final String NOT_EQUAL = "!=";
    public static final String LESS = "<";
    public static final String GREATER = ">";
    public static final String LESS_EQUAL = "<=";
    public static final String GREATER_EQUAL = ">=";

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String DIV = "/";
    public static final String TIMES = "*";
    public static final String MOD = "mod";

    public static final String PRINT = "print";
    public static final String DUP = "dup";
    public static final String DROP = "drop";

    public static final String IF = "if";
    public static final String ELSE = "else";
    public static final String END = "end";

    static {
        WORD_PARSERS.put(EQUAL, ComparisonWords.EQUAL);
        WORD_PARSERS.put(NOT_EQUAL, ComparisonWords.NOT_EQUAL);
        WORD_PARSERS.put(LESS, ComparisonWords.LESS);
        WORD_PARSERS.put(GREATER, ComparisonWords.GREATER);
        WORD_PARSERS.put(LESS_EQUAL, ComparisonWords.LESS_EQUAL);
        WORD_PARSERS.put(GREATER_EQUAL, ComparisonWords.GREATER_EQUAL);

        WORD_PARSERS.put(PLUS, ArithmeticWords.PLUS);
        WORD_PARSERS.put(MINUS, ArithmeticWords.MINUS);
        WORD_PARSERS.put(DIV, ArithmeticWords.DIV);
        WORD_PARSERS.put(TIMES, ArithmeticWords.TIMES);
        WORD_PARSERS.put(MOD, ArithmeticWords.MOD);

        WORD_PARSERS.put(PRINT, StackWords.PRINT);
        WORD_PARSERS.put(DUP, StackWords.DUP);
        WORD_PARSERS.put(DROP, StackWords.DROP);

        WORD_PARSERS.put(IF, ConditionWords.IF);
        WORD_PARSERS.put(ELSE, ConditionWords.ELSE);
        WORD_PARSERS.put(END, ConditionWords.END);
    }

    public static IWordParser forWord(String word) {
        return WORD_PARSERS.get(word);
    }

    public static boolean containsWord(String word){
        return WORD_PARSERS.containsKey(word);
    }
}
