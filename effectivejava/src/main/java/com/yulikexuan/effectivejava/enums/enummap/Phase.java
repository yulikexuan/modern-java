//: com.yulikexuan.effectivejava.enums.enummap.Phase.java

package com.yulikexuan.effectivejava.enums.enummap;


import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;


/*
 * In summary, it is rarely appropriate to use ordinals to index into arrays:
 * use EnumMap instead
 *
 * If the relationship you are representing is multi-dimensional,
 * use EnumMap<..., EnumMap<...>>
 *
 */
enum Phase {

    SOLID, LIQUID, GAS;

    enum FragileTransition {

        MELT,       // solid to liquid
        FREEZE,     // liquid to solid
        BOIL,       // liquid to gas
        CONDENSE,   // gas to liquid
        SUBLIME,    // solid to gas
        DEPOSIT;    // gas to solid

        /*
         * The compiler has no way of knowing the relationship between ordinals
         * and array indices
         *
         * If you make a mistake in the transition table or forget to update it
         * when you modify the Phase or Phase.Transition enum type, your program
         * will fail at runtime
         *
         * The failure may be an ArrayIndexOutOfBoundsException,
         * a NullPointerException, or (worse) silent erroneous behavior
         *
         * And the size of the table is quadratic in the number of phases, even
         * if the number of non-null entries is smaller
         */
        private static final FragileTransition[][] FRAGILE_TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        // Returns the phase transition from one phase to another
        public static FragileTransition from(Phase from, Phase to) {
            return FRAGILE_TRANSITIONS[from.ordinal()][to.ordinal()];
        }

    }

    @Getter
    enum VersatileTransition {

        MELT_TRANS(SOLID, LIQUID),
        FREEZE_TRANS(LIQUID, SOLID),
        BOIL_TRANS(LIQUID, GAS),
        CONDENSE_TRANS(GAS, LIQUID),
        SUBLIME_TRANS(SOLID, GAS),
        DEPOSIT_TRANS(GAS, SOLID);

        private final Phase from;
        private final Phase to;

        private VersatileTransition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        /*
         * Initialize the phase transition map
         *
         * The type of the map is Map<Phase, Map<Phase, Transition>>,
         * which means “map from(source) phase to map from (destination) phase
         * to transition.”
         *
         * This map-of-maps is initialized using a cascaded sequence of two
         * collectors
         *
         * The first collector groups the transitions by source phase,
         * and the second creates an EnumMap with mappings from destination
         * phase to transition
         *
         * The merge function in the second collector ((x, y) -> y)) is unused;
         * it is required only because we need to specify a map factory in order
         * to get an EnumMap, and Collectors provides telescoping (可伸縮的)
         * factories
         *
         *
         */
        private static final Map<Phase, Map<Phase, VersatileTransition>>
                transMap = Stream.of(values()).collect(groupingBy(
                        VersatileTransition::getFrom,
                        () -> new EnumMap<>(Phase.class),
                        toMap(VersatileTransition::getTo,
                                trans -> trans,
                                (x, y) -> y, // x is the old value, y is the new one
                                () -> new EnumMap<>(Phase.class))));
    }

}///:~