//: com.yulikexuan.effectivejava.enums.enummap.Plant.java

package com.yulikexuan.effectivejava.enums.enummap;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;


@Value
@AllArgsConstructor(staticName = "of")
class Plant {

    static enum LifeCycle {
        ANNUAL,
        BIENNIAL,
        PERENNIAL;
    }

    static final int LIFE_CYCLE_SIZE = LifeCycle.values().length;

    private final String name;
    private final LifeCycle lifeCycle;

    static Set<Plant>[] classifyPlantsOfTheGarden(List<Plant> garden) {

        // Using ordinal() to index into an array - DON'T DO THIS!
        Set<Plant>[] plantsByLifeCycle = (Set<Plant>[]) new Set[LIFE_CYCLE_SIZE];

        // Initialize each Set of Plant
        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            plantsByLifeCycle[i] = new HashSet<>();
        }

        // Classify
        for (Plant p : garden) {
            plantsByLifeCycle[p.lifeCycle.ordinal()].add(p);
        }

        return plantsByLifeCycle;
    }

    static Map<LifeCycle, Set<Plant>> classifyPlantsInEnumMapWithStream(
            @NonNull List<Plant> gargen) {

        Map<LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<>(LifeCycle.class);

        /*
         * To collect into a Map that contains multiple values by key
         * (Map<MyKey, Set<MyObject>>), use Collectors.groupingBy()
         *
         * To collect into a Map that contains a single value by key
         * (Map<MyKey, MyObject>), use Collectors.toMap()
         */
        return gargen.stream().collect(groupingBy(
                Plant::getLifeCycle,
                () -> new EnumMap<>(LifeCycle.class),
                toSet()));
    }

}///:~