//: com.yulikexuan.jdk.ocp2.arrays.GeoCache.java

package com.yulikexuan.jdk.ocp2.arrays;


import java.util.*;


class GeoCache {

    public static void main(String[] args) {

        String[] s = {"map", "pen", "marble", "key"};
        Othello o = new Othello();

        Arrays.sort(s, o);

        for(String s2: s) {
            System.out.print(s2 + " ");
        }

        int map_index = Arrays.binarySearch(s, "map");
        System.out.println(map_index);

        String[] anotherS = {"map", "pen", "marble", "key"};
        Arrays.sort(anotherS);
        for(String s2: anotherS) {
            System.out.print(s2 + " ");
        }
        int another_map_index = Arrays.binarySearch(anotherS, "map");
        System.out.println(another_map_index);
    }

    static class Othello implements Comparator<String> {
        public int compare(String a, String b) {
            return b.compareTo(a);
        }
    }

}///:~