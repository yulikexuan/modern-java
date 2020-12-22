//: com.yulikexuan.effectivejava.strings.Interview.java

package com.yulikexuan.effectivejava.strings;


import com.google.common.collect.Maps;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Interview {

    /*
     * laval: l, l , a, v ,a
     */
    static boolean isPermutation(@NonNull List<Character> input) {

        Map<Character, Integer> data = Maps.newHashMap();

        for (Character ch: input) {
            data.merge(ch, 1, (key, value) -> ++value);
        }

        Collection<Integer> counts = data.values();

        int odd = 0;
        for (int count : counts) {
            if (count % 2 == 1) {
                odd++;
                if (odd > 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (count > 2) {
                return false;
            }
        }
        return true;
    }

}///:~