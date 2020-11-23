//: com.yulikexuan.effectivejava.lambda.MethodReference.java

package com.yulikexuan.effectivejava.lambda;


import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Map;


@AllArgsConstructor(staticName = "of")
public class MethodReference {

    private final Map<String, Integer> inventory;

    Integer merge(@NonNull String key, @NonNull Integer value) {
        return this.inventory.merge(key, value, Integer::sum);
    }

}///:~