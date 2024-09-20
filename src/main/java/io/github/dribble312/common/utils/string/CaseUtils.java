package io.github.dribble312.common.utils.string;



import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for handling and converting different case formats of strings.
 * Provides methods to determine the case of a string, convert strings to different cases, and split strings based on their case.
 * Supports case formats such as camel case, snake case, and kebab case.
 *
 * @author czx
 */
public class CaseUtils {

    // Constants for delimiters used in case formats
    private static final char DELIMITER_HYPHEN = '-';
    private static final char DELIMITER_UNDERSCORE = '_';

    // in-class strategy Maps for storing case-specific judge logic
    private static final Map<Case, Judge> allJudges;

    private static final Map<Case, Character> caseDelimiterMap;

    // A Judge to validate if a string is a legal Java identifier or follows specific conventions
    private static final Judge legalJudge = s -> s.chars()
            .noneMatch((i) -> !Character.isJavaIdentifierPart(i) && i != DELIMITER_HYPHEN);

    static {
        allJudges = new HashMap<>();
        caseDelimiterMap = new HashMap<>();

        caseDelimiterMap.put(Case.SNAKE, DELIMITER_UNDERSCORE);
        caseDelimiterMap.put(Case.KEBAB, DELIMITER_HYPHEN);

        allJudges.put(Case.SNAKE, s -> s.chars().anyMatch(i -> i == caseDelimiterMap.get(Case.SNAKE)));
        allJudges.put(Case.KEBAB, s -> s.chars().anyMatch(i -> i == caseDelimiterMap.get(Case.KEBAB)));


    }

    @FunctionalInterface
    private interface Judge {
        boolean judge(String s);
    }

    /**
     * Enum representing different case formats for strings.
     */
    public enum Case {
        NORM_CAMEL,
        CAPITAL_CAMEL,
        SNAKE,
        KEBAB,
        UNKNOWN
    }

    /**
     * Determines the case format of the given string.
     *
     * @param targetStr the string to determine the case format of
     * @return the case format of the string
     */
    public static Case determineCase(String targetStr) {
        if (ObjectUtils.isEmpty(targetStr)) {
            return Case.UNKNOWN;
        }

        if (!legalJudge.judge(targetStr)) {
            return Case.UNKNOWN;
        }

        boolean firstCharUpperCase = Character.isUpperCase(targetStr.charAt(0));
        boolean upperCasePresents = firstCharUpperCase || targetStr.chars().anyMatch(Character::isUpperCase);

        for (Map.Entry<Case, Judge> entry : allJudges.entrySet()) {
            if (entry.getValue().judge(targetStr)) {
                if (upperCasePresents) {
                    return Case.UNKNOWN;
                }
                Case key = entry.getKey();
                if (splitAndLowerCase(key, targetStr).stream().noneMatch(String::isEmpty)) {
                    // maybe has double delimiter
                    return key;
                } else {
                    return Case.UNKNOWN;
                }
            }
        }

        if (upperCasePresents && firstCharUpperCase) return Case.CAPITAL_CAMEL;

        // else
        return Case.NORM_CAMEL;
    }

    /**
     * Capitalizes the first letter of the given word if it is not already uppercase.
     *
     * @param word the word to capitalize
     * @return the word with the first letter capitalized
     */
    public static String upperCaseWord(String word) {
        if (ObjectUtils.isEmpty(word) || Character.isUpperCase(word.charAt(0))) {
            return word;
        }

        if (word.length() == 1) {
            return word.toUpperCase();
        }

        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * Converts the first letter of the given word to lowercase if it is not already lowercase.
     *
     * @param word the word to convert to lowercase
     * @return the word with the first letter in lowercase
     */
    public static String lowercaseWord(String word) {
        if (ObjectUtils.isEmpty(word) || Character.isLowerCase(word.charAt(0))) {
            return word;
        }

        if (word.length() == 1) {
            return word.toLowerCase();
        }

        return Character.toLowerCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * Splits the input string based on the specified case format and converts
     * each part to lowercase.
     *
     * @param caz the case format to use for splitting and lowercasing
     * @param s   the string to split and lowercase
     * @return a list of split and lowercased strings
     */
    private static List<String> splitAndLowerCase(Case caz, String s) {
        if (caz == Case.NORM_CAMEL || caz == Case.CAPITAL_CAMEL) {
            return Arrays.stream(s.split("(?=[A-Z])"))
                    // lowerize
                    .map(str -> Character.isUpperCase(str.charAt(0)) ? str.toLowerCase() : str)
                    .collect(Collectors.toList());
        }

        return Arrays.stream(s.split(caseDelimiterMap.get(caz).toString())).toList();
    }

    /**
     * Converts a string to the specified case format.
     *
     * @param targetCase the target case format
     * @param s          the string to convert
     * @return the converted string
     */
    public static String toCase(Case targetCase, String s) {
        if (s == null) {
            throw new IllegalArgumentException("String is null");
        }

        if (s.isEmpty()) {
            return s;
        }

        Case originCase = determineCase(s);

        if (originCase == Case.UNKNOWN) {
            throw new IllegalArgumentException("Unknown case of string, can not convert.");
        }

        List<String> parts = splitAndLowerCase(originCase, s);

        if (targetCase == Case.NORM_CAMEL || targetCase == Case.CAPITAL_CAMEL) {
            String result = parts.stream().map(CaseUtils::upperCaseWord).collect(Collectors.joining());

            if (targetCase == Case.NORM_CAMEL) {
                return lowercaseWord(result);
            }

            return result;
        }

        return parts.stream().collect(Collectors.joining(String.valueOf(caseDelimiterMap.get(targetCase))));
    }

}
