//: com.yulikexuan.effectivejava.enums.VersatileOperation.java


package com.yulikexuan.effectivejava.enums;


import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


public enum VersatileOperation {

    PLUS("+") {
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        public double apply(double x, double y) {
            return x / y;
        }
    };

    /*
     * To collect into a Map that contains multiple values by key
     * (Map<MyKey, Set<MyObject>>), use Collectors.groupingBy()
     *
     * To collect into a Map that contains a single value by key
     * (Map<MyKey, MyObject>), use Collectors.toMap()
     */
    private static final Map<String, VersatileOperation> STRING_ENUM_MAP =
            Stream.of(VersatileOperation.values())
                    .collect(ImmutableMap.toImmutableMap(
                            Object::toString, e -> e));

    private final String optSymbol;

    private VersatileOperation(String optSymbol) {
        this.optSymbol = optSymbol;

        // Illegal
        // STRING_ENUM_MAP.put(optSymbol, this);
    }

    public abstract double apply(double x, double y);

    @Override
    public String toString() {
        return this.optSymbol;
    }

    public static Optional<VersatileOperation> fromString(String optSymbol) {
        return Optional.ofNullable(STRING_ENUM_MAP.get(optSymbol));
    }

}///:~