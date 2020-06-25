//: com.yulikexuan.effectivejava.object.construction.RomanNumerals.java


package com.yulikexuan.effectivejava.object.construction;


import java.util.regex.Pattern;


public class RomanNumerals {

    private static final String REGEX_ROMAN_PATTERN =
            "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$";

    private static final Pattern ROMAN = Pattern.compile(REGEX_ROMAN_PATTERN);

    private RomanNumerals() {
        throw new AssertionError(
                "The class RomanNumerals is not instantiable.");
    }

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }

    static boolean isRomanNumeralSlowly(String s) {
        return s.matches(REGEX_ROMAN_PATTERN);
    }

}///:~