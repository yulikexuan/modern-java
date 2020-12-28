//: com.yulikexuan.effectivejava.reflection.ReflectiveInstantiation.java

package com.yulikexuan.effectivejava.reflection;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;


/*
 * Reflection is a powerful facility that is required for certain sophisticated
 * system programming tasks, but it has many disadvantages
 * If you are writing a program that has to work with classes unknown at
 * compile time, you should, if at all possible, use reflection only to
 * instantiate objects, and access the objects using some interface or
 * superclass that is known at compile time
 */
@Slf4j
public class ReflectiveInstantiation {

    // Reflective instantiation with interface access
    public static Set<String> initializeStringSet(String[] args)
            throws Exception {

        // Translate the class name into a Class object
        Class<? extends Set<String>> cl = null;

        try {
            // Unchecked cast!
            cl = (Class<? extends Set<String>>) Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            fatalError("Class not found.");
        }

        // Get the constructor
        Constructor<? extends Set<String>> cons = null;

        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fatalError("No parameterless constructor");
        }

        // Instantiate the set
        Set<String> s = null;
        try {
            s = cons.newInstance();
        } catch (IllegalAccessException e) {
            fatalError("Constructor not accessible");
        } catch (InstantiationException e) {
            fatalError("Class not instantiable.");
        } catch (InvocationTargetException e) {
            fatalError("Constructor threw " + e.getCause());
        } catch (ClassCastException e) {
            fatalError("Class doesn't implement Set");
        }

        // Exercise the set
        s.addAll(Arrays.asList(args).subList(1, args.length));

        return s;
    }

    private static void fatalError(String msg) {
        log.error(">>>>>>> There is an error that {}", msg);
        System.exit(1);
    }

}///:~