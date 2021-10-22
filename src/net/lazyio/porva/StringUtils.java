package net.lazyio.porva;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    //https://stackoverflow.com/a/1102916
    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    //https://stackoverflow.com/a/366532
    public static List<String> splitOnSpaceKeepingQuotes(String str) {
        var words = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(str);
        while (regexMatcher.find()) {
            words.add(regexMatcher.group());
        }
        return words;
    }

    public static boolean isStr(String str) {
        return str.startsWith("\"") && str.endsWith("\"");
    }

    public static boolean isChar(String str) {
        return str.length() == 3 && (str.startsWith("'") && str.endsWith("'"));
    }

    public static String removeQuotes(String str){
        return str.substring(1, str.length() - 1);
    }

    public static int _int(String str){
        return Integer.parseInt(str);
    }
}
