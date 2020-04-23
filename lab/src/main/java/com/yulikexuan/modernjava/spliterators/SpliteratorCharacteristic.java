//: com.yulikexuan.modernjava.spliterators.SpliteratorCharacteristic.java


package com.yulikexuan.modernjava.spliterators;


import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;


public enum SpliteratorCharacteristic {

    CONCURRENT(Spliterator.CONCURRENT),
    DISTINCT(Spliterator.DISTINCT),
    IMMUTABLE(Spliterator.IMMUTABLE),
    NONNULL(Spliterator.NONNULL),
    ORDERED(Spliterator.ORDERED),
    SIZED(Spliterator.SIZED),
    SORTED(Spliterator.SORTED),
    SUBSIZED(Spliterator.SUBSIZED);

    private final int value;

    SpliteratorCharacteristic(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Set<SpliteratorCharacteristic> getCharacteristics(
            final Spliterator spliterator) {

        Set<SpliteratorCharacteristic> characteristics = Sets.newHashSet();

        if (!Objects.isNull(spliterator)) {
            Arrays.stream(SpliteratorCharacteristic.values())
                    .forEach(c -> {
                        if (spliterator.hasCharacteristics(c.getValue())) {
                            characteristics.add(c);
                        }
                    });
        }

        return characteristics;
    }

}///:~
