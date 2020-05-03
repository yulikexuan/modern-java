//: com.yulikexuan.modernjava.strings.StringTransformation.java


package com.yulikexuan.modernjava.strings;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StringTransformation {

    public static String sanitizeBlanks(String value) {

        if (Objects.isNull(value)) {
            return "";
        }

        return value.lines()
                .filter(Predicate.not(String::isBlank))
                .map(String::strip)
                .collect(Collectors.joining("\n"));
    }

    public static String addLineNumber(String value) {
        AtomicInteger lineNumber = new AtomicInteger();
        return value.lines()
                .map(line -> String.format("%d : %s%n",
                        lineNumber.incrementAndGet(), line))
                .collect(Collectors.joining());
    }

}///:~