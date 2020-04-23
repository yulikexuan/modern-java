//: com.yulikexuan.modernjava.lambdas.MethodReferences.java


package com.yulikexuan.modernjava.lambdas;


import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class MethodReferences {

    static Random random = new Random(System.currentTimeMillis());

    private int[] dashCaloryArr = IntStream
            .generate(() -> random.nextInt(1000))
            .limit(10)
            .toArray();

    public void printCaloricLevel() {
        Arrays.stream(dashCaloryArr)
                .mapToObj(calory -> {
                    if (calory <= 400) {
                        return "DIET";
                    } else if (calory <= 700) {
                        return "NORMAL";
                    } else {
                        return "FAT";
                    }})
                .forEach(System.out::println);
    }

    public void printCaloricLevelWithMethodReference() {
        Arrays.stream(dashCaloryArr)
                .mapToObj(this::getCaloricLevelFromCalory)
                .forEach(System.out::println);
    }

    String getCaloricLevelFromCalory(int calory) {
        if (calory <= 400) {
            return "DIET";
        } else if (calory <= 700) {
            return "NORMAL";
        } else {
            return "FAT";
        }
    }

}///:~