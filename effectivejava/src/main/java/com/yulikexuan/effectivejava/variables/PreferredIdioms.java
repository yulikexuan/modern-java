//: com.yulikexuan.effectivejava.variables.PreferredIdioms.java

package com.yulikexuan.effectivejava.variables;


import com.google.common.collect.Lists;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class PreferredIdioms {

    // Preferred idiom for iterating over a collection or array
    static <T> void iteratingCollection(@NonNull Collection<T> collection) {

        for (T t : collection) {
            // Do something with element in the collection
        }
    }

    // Idiom for iterating when needing the iterator
    static <T> void iteratingWithIterator(@NonNull Collection<T> collection) {

        for (Iterator<T> iterator = collection.iterator(); iterator.hasNext(); ) {
            T t = iterator.next();
            // Do something with element t and iterator
        }
    }

    static <T> void whileLoopIsErrorProne(@NonNull Collection<T> collection) {

        Iterator<T> i = collection.iterator();
        while (i.hasNext()) {
            // Do something with the element in collection
        }

        // Some other code ... ...
        Collection collection2 = Collections.unmodifiableCollection(collection);

        Iterator<T> i2 = collection2.iterator();

        // BUG!
        while (i.hasNext()) {
            // Do something with the element in collection2
        }
    }

    static <T> void preferForLoopToWhileLoop(@NonNull Collection<T> collection) {

        for (Iterator<T> i = collection.iterator(); i.hasNext(); ) {
            T t = i.next();
            // Do something with element t and iterator
        }

        List<T> list = Lists.newArrayList(collection);
        for (Iterator<T> i = list.iterator(); i.hasNext(); ) {
            T t = i.next();
            // Do something with element t and iterator
        }
    }

    // Another loop idiom that minimizes the scope of local variables
    static <T> void moreLocalVar(@NonNull Collection<T> collection) {

        for (int i = 0, size = collection.size(); i < size; i++) {
            // Do something with i
        }
    }

}///:~