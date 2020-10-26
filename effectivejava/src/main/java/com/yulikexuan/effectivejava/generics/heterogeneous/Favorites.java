//: com.yulikexuan.effectivejava.generics.heterogeneous.Favorites.java


package com.yulikexuan.effectivejava.generics.heterogeneous;


import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Objects;


@AllArgsConstructor(staticName = "of")
class Favorites {

    private final Map<Class<?>, Object> favorites;

    <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }

//    <T> void putFavorite(Class<T> type, T instance) {
//        favorites.put(Objects.requireNonNull(type), instance);
//    }

    // Achieving runtime type safety with a dynamic cast
    <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }

}///:~