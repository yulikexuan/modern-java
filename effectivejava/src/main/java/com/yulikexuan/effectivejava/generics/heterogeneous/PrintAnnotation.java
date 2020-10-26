//: com.yulikexuan.effectivejava.generics.heterogeneous.PrintAnnotation.java


package com.yulikexuan.effectivejava.generics.heterogeneous;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;


public class PrintAnnotation {

    static Annotation getAnnotation(
            AnnotatedElement element, String annotationTypeName) {

        // Unbounded type token
        Class<?> annotationType = null;

        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }

        return element.getAnnotation(
                annotationType.asSubclass(Annotation.class));
    }

}///:~