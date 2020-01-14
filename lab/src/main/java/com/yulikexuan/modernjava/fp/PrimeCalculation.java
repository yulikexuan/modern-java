//: com.yulikexuan.modernjava.fp.PrimeCalculation.java


package com.yulikexuan.modernjava.fp;


import java.util.stream.IntStream;
import java.util.stream.Stream;


public class PrimeCalculation {

    static final int LIMIT = 100;

    public static Stream<Integer> primes(int n) {
        if (n > LIMIT) throw new IllegalArgumentException("The limit is 100.");
        return Stream.iterate(2, i -> i + 1)
                .filter(PrimeCalculation::isPrime)
                .limit(n);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt(candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    // Compute primes recursively: ---------------------------------------------

    // Step 1: Get a stream of numbers
    public static IntStream getNumbers() {
        return IntStream.iterate(2, i -> i + 1);
    }

    // Step 2: Take the head
    private static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    // Step 3: Filter the tail
    private static IntStream fetchTailFromNumbers(IntStream numbers) {
        return numbers.skip(1);
    }

    // Step 4: Recursive create a stream of primes
    public static IntStream recursivelyPrimes(IntStream numbers) {

        int head = head(numbers);

        if (head > 20) return numbers;

        return IntStream.concat(IntStream.of(head),
                recursivelyPrimes(fetchTailFromNumbers(numbers)
                        .filter(i -> i % head != 0)));
    }

}///:~