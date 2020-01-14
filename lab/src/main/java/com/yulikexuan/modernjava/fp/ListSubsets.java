//: com.yulikexuan.modernjava.fp.ListSubsets.java


package com.yulikexuan.modernjava.fp;


import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ListSubsets {

    public static List<List<Integer>> subsets(final List<Integer> list) {

        if ((list == null) || (list.isEmpty())) {
            return List.of(List.of());
        }

        Integer first = list.get(0);
        List<Integer> rest = list.subList(1, list.size());

        List<List<Integer>> restSubsets = subsets(rest);
        List<List<Integer>> restSubsetsWithFirst =
                installFirst(first, restSubsets);

        return combineAll(restSubsets, restSubsetsWithFirst);
    }

    private static List<List<Integer>> installFirst(
            final Integer first, final List<List<Integer>> restSubsets) {

        List<List<Integer>> result = restSubsets.stream()
                .map(list -> {
                    List<Integer> installedList = new ArrayList<>();
                    installedList.add(first);
                    installedList.addAll(list);
                    return installedList;
                })
                .collect(Collectors.toList());

        return ImmutableList.copyOf(result);
    }

    private static List<List<Integer>> combineAll(
            List<List<Integer>> restSubsets,
            List<List<Integer>> restSubsetsWithFirst) {

        List<List<Integer>> combinedList = new ArrayList<>();
        combinedList.addAll(restSubsets);
        combinedList.addAll(restSubsetsWithFirst);

        return ImmutableList.copyOf(combinedList);
    }

}///:~