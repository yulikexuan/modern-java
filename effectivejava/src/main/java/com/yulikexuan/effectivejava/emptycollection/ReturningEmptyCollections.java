//: com.yulikexuan.effectivejava.emptycollection.ReturningEmptyCollections.java

package com.yulikexuan.effectivejava.emptycollection;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;


class ReturningEmptyCollections {

    //The right way to return a possibly empty collectionx
    static <T> List<T> getList(Collection<T> things) {
        return Objects.isNull(things) ?
                Collections.EMPTY_LIST : Lists.newArrayList(things);
    }

    /*
     * Optimization - avoids allocating empty collections
     *
     * Remember, this is an optimization, and it’s seldom called for
     * If you think you need it, measure performance before and after,
     * to ensure that it’s actually helping
     */
    static <T> List<T> getListOptimized(Collection<T> things) {
        return CollectionUtils.isEmpty(things) ?
                Collections.EMPTY_LIST : Lists.newArrayList(things);
    }

    static <T> Set<T> getSetOptimized(Collection<T> things) {
        return CollectionUtils.isEmpty(things) ?
                Collections.EMPTY_SET : Sets.newHashSet(things);
    }

    static <K, V> Map<K, V> getMapOptimized(Map<K, V> things) {
        return MapUtils.isEmpty(things) ?
                Collections.EMPTY_MAP : Maps.newHashMap(things);
    }

    //The right way to return a possibly empty array
    static String[] getArray(Collection<String> things) {
        return Objects.isNull(things) ? new String[0] :
                things.toArray(new String[0]);
    }

    // Optimization - avoids allocating empty arrays
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    static String[] getArrayOptimized(Collection<String> things) {
        return Objects.isNull(things) ? EMPTY_STRING_ARRAY :
                things.toArray(EMPTY_STRING_ARRAY);
    }

}///:~