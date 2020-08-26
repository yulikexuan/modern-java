//: com.yulikexuan.effectivejava.generics.PreferListsToArrays.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

class PreferListsToArrays {

    /*
     * Long[] is a sub-type of Object[]
     * String[] is a sub-type of Object[]
     * But String[] has nothing with Long[]
     *
     * ArrayStoreException will be thrown if:
     *   - Assign a String to a Long[]
     *   - Assign a Long to a String[]
     */
    static Object[] getLongTypeArray() {
        return new Long[1];
    }

    static Object[] getObjArr() {
        return new Object[1];
    }

    /*
     * List<Object> has nothing with List<Long> or List<String>
     */
    static List<Object> getObjectTypeList() {
        // return new ArrayList<Long>();
        // return new ArrayList<String>();
        return Lists.newArrayList();
    }

}///:~