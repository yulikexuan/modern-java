//: com.yulikexuan.modernjava.cache.caffeine.DataObject.java

package com.yulikexuan.modernjava.cache.caffeine;


import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor(staticName = "of")
final class DataObject {

    private final String data;

    private static int objectCounter = 0;

}///:~