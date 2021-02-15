//: com.yulikexuan.concurrency.buildingblocks.UnsafeVectorHelpers.java

package com.yulikexuan.concurrency.buildingblocks;


import lombok.NonNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Vector;


/**
 * UnsafeVectorHelpers
 * <p/>
 * Compound actions on a Vector that may produce confusing results
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class UnsafeVectorHelpers {

    public static Object getLast(@NonNull Vector list) {
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }

    public static void deleteLast(@NonNull Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }

}///:~