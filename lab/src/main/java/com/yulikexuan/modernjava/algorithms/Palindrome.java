//: com.yulikexuan.modernjava.algorithms.Palindrome.java

package com.yulikexuan.modernjava.algorithms;


import com.google.common.collect.Maps;
import lombok.NonNull;

import java.util.Map;


public class Palindrome {

    static boolean isPalindrome(@NonNull String word) {

        Map<Character, Integer> data = Maps.newHashMap();

        char[] letters = word.toLowerCase().toCharArray();

        for (Character ch : letters) {
            data.merge(ch, 1, (key, value) -> ++value);
        }

        return data.values().stream()
                .filter(count -> (count & 1) == 1)
                .count() <= 1;
    }

}///:~