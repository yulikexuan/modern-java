//: com.yulikexuan.modernjava.algorithms.NumberBinarySearch.java


package com.yulikexuan.modernjava.algorithms;


import lombok.NonNull;


class NumberBinarySearch {

    static int iterativeBinarySearch(@NonNull int[] numbers,
                                      int low, int high, int key) {

        if (numbers.length == 0) {
            return Integer.MIN_VALUE;
        } else if (numbers.length == 1) {
            return 0;
        }

        if (low > high) {
            return Integer.MAX_VALUE;
        }

        int index = Integer.MAX_VALUE;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (numbers[mid] < key) {
                low = mid + 1;
            } else if (numbers[mid] > key) {
                high = mid - 1;
            } else {
                index = mid;
                break;
            }
        }

        return index;
    }

}///:~