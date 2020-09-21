//: com.yulikexuan.modernjava.algorithms.SequenceGenerator.java


package com.yulikexuan.modernjava.algorithms;


import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
import java.util.stream.Stream;


class SequenceGenerator {

    /*
     * In general, you should use iterate when you need to produce a sequence
     * of successive values (for example, a date followed by its next date:
     * January 31, February 1, and so on)
     */
    static List<Integer> generateEvenNumbers(int size) {
        return Stream.iterate(0, n -> n + 2)
                .limit(size)
                .collect(ImmutableList.toImmutableList());
    }

    static List<Integer> generateEvenNumbersLessThan(int maximum) {
        return Stream.iterate(0, n -> n < maximum, n -> n + 2)
                .collect(ImmutableList.toImmutableList());
    }

    static void printFibonacciNumbers(int size) {
        Stream.iterate(new int[] {0, 1},
                tuple -> new int[] {tuple[1], tuple[0] + tuple[1]})
                .limit(size)
                .forEach(tuple -> System.out.printf("(%d, %d)%n", tuple[0],
                        tuple[1]));
    }

    static List<Integer> generateRandomNumbers(int size) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return Stream.generate(random::nextInt)
                .limit(size)
                .collect(ImmutableList.toImmutableList());
    }

    static final IntSupplier FIBONACCI_NUMBER_SUPPLIER = new IntSupplier() {

        private int previous = 0;
        private int current = 1;

        @Override
        public int getAsInt() {

            int oldPrevious = this.previous;
            int next = this.previous + this.current;

            this.previous = this.current;
            this.current = next;

            return oldPrevious;
        }
    };

}///:~