//: com.yulikexuan.modernjava.algorithms.powerset.PowerSetAlgorithms.java

package com.yulikexuan.modernjava.algorithms.powerset;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

import static java.util.stream.Collectors.toSet;


/*
 * https://www.baeldung.com/java-power-set-of-a-set
 */
public class PowerSetAlgorithms {

    /*
     * Guava powerSet internally operates over the Iterator interface
     * in the way when the next subset is requested, the subset is calculated
     * and returned
     * So, the space complexity is reduced to O(n) instead of O(2^n).
     */
    static <T> Set<Set<T>> getGuavaPowerSet(@NonNull Set<T> set) {
        return Sets.powerSet(set);
    }

    /*
     * a --> [a, []]
     * b --> [[ba], [b], [a], []]
     * c --> [[cba], [cb], [ca], [c], [ba], [b], [a], []]
     */
    static <T> Set<Set<T>> getPowerSetRecursively(@NonNull Set<T> set) {

        if (set.isEmpty()) {
            Set<Set<T>> powerSet = Sets.newHashSet();
            powerSet.add(set);
            return powerSet;
        }

        ImmutablePair<T, Set<T>> splitPair = split(set);

        final T selectedElement = splitPair.left;
        Set<T> subSet_Without_SelectedElement = splitPair.right;

        Set<Set<T>> powerSet_Without_SelectedElement =
                getPowerSetRecursively(subSet_Without_SelectedElement);

        Set<Set<T>> powerSet_With_SelectedElement =
                getPowerSetWithTheSelectedElement(
                        powerSet_Without_SelectedElement, selectedElement);

        Set<Set<T>> powerSet = Sets.newHashSet();
        powerSet.addAll(powerSet_With_SelectedElement);
        powerSet.addAll(powerSet_Without_SelectedElement);

        return powerSet;
    }

    private static <T> ImmutablePair<T, Set<T>> split(@NonNull Set<T> set) {
        T selectedElement = set.iterator().next();
        Set<T> subSetWithoutSelectedEmement = set.stream()
                .filter(e -> !e.equals(selectedElement))
                .collect(toSet());
        return ImmutablePair.of(selectedElement, subSetWithoutSelectedEmement);
    }

    private static <T> Set<Set<T>> getPowerSetWithTheSelectedElement(
            @NonNull Set<Set<T>> powerSetWithoutTheSelectedElement,
            @NonNull T theSelectedElement) {

        Set<Set<T>> powerSetWithTheSelectedElement = Sets.newHashSet();

        for (Set<T> subSet : powerSetWithoutTheSelectedElement) {
            Set<T> newSet = Sets.newHashSet();
            newSet.add(theSelectedElement);
            newSet.addAll(subSet);
            powerSetWithTheSelectedElement.add(newSet);
        }

        return powerSetWithTheSelectedElement;
    }

    static <T> Map<T, Integer> getElementFrequencyMap(
            @NonNull Set<Set<T>> namePowerSet) {

        Map<T, Integer> nameFrequency = Maps.newHashMap();

        for (Set<T> subSet : namePowerSet) {
            for (T name : subSet) {
                nameFrequency.merge(name, 1, Integer::sum);
            }
        }

        return nameFrequency;
    }

    static <T> Map<T, Integer> getElementIndexMap(
            @NonNull Collection<T> elements) {

        Map<T, Integer> map = Maps.newHashMap();

        int i = 0;
        for (T element : elements) {
            map.put(element, i++);
        }

        return map;
    }

    static <T> List<T> getOrderedElements(
            @NonNull Collection<T> elements) {

        List<T> elementList = Lists.newArrayList();

        int i = 0;
        for (T element : elements) {
            elementList.add(element);
        }

        return elementList;
    }

    /* Generates power set by using element index */

    static <T> Set<Set<T>> indexToElement(
            @NonNull Set<Set<Integer>> indexPowerSet,
            @NonNull List<T> elementIndex) {

        Set<Set<T>> powerSet = Sets.newHashSet();

        for (Set<Integer> indexes : indexPowerSet) {
            Set<T> set = Sets.newHashSet();
            for (Integer i : indexes) {
                set.add(elementIndex.get(i));
            }
            powerSet.add(set);
        }

        return powerSet;
    }

    static <T> Set<Set<T>> getIndexPowerSetRecursively(@NonNull Set<T> set) {

        List<T> elementList = getOrderedElements(set);

        int size = set.size();
        Set<Set<Integer>> indexPowerSet = buildIndexPowerSetRecursively(
                0, size);

        return  indexToElement(indexPowerSet, elementList);
    }

    private static Set<Set<Integer>> buildIndexPowerSetRecursively(
            int index, int size) {

        if (index == size) {
            Set<Set<Integer>> powerSet = Sets.newHashSet();
            powerSet.add(Sets.newHashSet());
            return powerSet;
        }

        Set<Set<Integer>> powerSetWithoutSelectedIndex =
                buildIndexPowerSetRecursively(index + 1, size);

        Set<Set<Integer>> powerSetWithSelectedIndex = Sets.newHashSet();
        for (Set<Integer> set : powerSetWithoutSelectedIndex) {
            Set<Integer> newSet = Sets.newHashSet();
            newSet.addAll(set);
            newSet.add(index);
            powerSetWithSelectedIndex.add(newSet);
        }

        Set<Set<Integer>> powerSet = Sets.newHashSet();
        powerSet.addAll(powerSetWithSelectedIndex);
        powerSet.addAll(powerSetWithoutSelectedIndex);

        return powerSet;
    }

    static <T> Set<Set<T>> getBinaryPowerSetRecursively(@NonNull Set<T> set) {

        List<T> elementList = getOrderedElements(set);

        int size = set.size();
        Set<List<Boolean>> binPowerSet = buildBinaryPowerSetRecursively(
                0, size);

        return  binaryElementMap(binPowerSet, elementList);
    }

    static <T> Set<Set<T>> getBinaryGraycodePowerSetRecursively(
            @NonNull Set<T> set) {

        List<T> elementList = getOrderedElements(set);

        int size = set.size();
        List<List<Boolean>> binPowerSet =
                OptimizedPowerSetFactory.newBinaryPowerSet(size);

        return  binaryElementMap(binPowerSet, elementList);
    }

    private static Set<List<Boolean>> buildBinaryPowerSetRecursively(
            int index, int size) {

        if (index == size) {
            Set<List<Boolean>> emptyPowerSet = Sets.newHashSet();
            emptyPowerSet.add(Arrays.asList(new Boolean[size]));
            return emptyPowerSet;
        }

        Set<List<Boolean>> powerSetWithoutSelectedIndex =
                buildBinaryPowerSetRecursively(index + 1, size);

        Set<List<Boolean>> powerSet = Sets.newHashSet();

        for (List<Boolean> binList : powerSetWithoutSelectedIndex) {
            List<Boolean> exclusiveList = Lists.newArrayList(binList);
            exclusiveList.set(index, false);
            powerSet.add(exclusiveList);
            List<Boolean> inclusiveList = Lists.newArrayList(binList);
            inclusiveList.set(index, true);
            powerSet.add(inclusiveList);
        }

        return powerSet;
    }

    private static <T> Set<Set<T>> binaryElementMap(
            @NonNull Collection<List<Boolean>> sets,
            @NonNull List<T> orderedElementList) {

        Set<Set<T>> ret = new HashSet<>();

        for (List<Boolean> binList : sets) {
            HashSet<T> subset = new HashSet<>();
            for (int i = 0; i < binList.size(); i++) {
                if (Objects.requireNonNull(binList.get(i))) {
                    subset.add(orderedElementList.get(i));
                }
            }
            ret.add(subset);
        }

        return ret;
    }

}///:~