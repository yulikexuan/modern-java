//: com.yulikexuan.modernjava.classloader.BuiltInClassLoadersDemo.java


package com.yulikexuan.modernjava.classloader;


import java.util.Optional;


/*
 * Class loaders are responsible for loading Java classes during runtime
 * dynamically to the JVM
 *
 * The JVM doesn’t need to know about the underlying files or file systems in
 * order to run Java programs thanks to class loaders
 *
 * These Java classes aren’t loaded into memory all at once, but when required
 * by an application
 *   - This is where class loaders come into the picture
 *   - They are responsible for loading classes into memory
 *
 * In this class, we’re going to present different types of built-in class
 * loaders, how they work and an introduction to our own custom implementation
 *
 * Let’s say we have a request to load an application class into the JVM
 *   - The application class loader first delegates the loading of that class
 *     to its parent class loader, platform class loader, which in turn
 *     delegates it to the bootstrap class loader
 *   - Only if the bootstrap and then the platform class loader is unsuccessful
 *     in loading the class, the application class loader tries to load the
 *     class itself
 *
 * Children class loaders are visible to classes loaded by its parent class
 * loaders
 */
public class BuiltInClassLoadersDemo {

    /*
     * The application class loader is the default loader for classes in modules
     * that are neither Java SE nor JDK modules.
     */
    public Optional<ClassLoader> getApplicationClassLoader() {
        return Optional.ofNullable(this.getClass().getClassLoader());
    }

    /*
     * The platform class loader (previous extension class loader)
     * All classes in the Java SE Platform are guaranteed to be visible through
     * the platform class loader
     *
     * In addition, the classes in modules that are standardized under the Java
     * Community Process but not part of the Java SE Platform are guaranteed to
     * be visible through the platform class loader
     *
     * Just because a class is visible through the platform class loader does
     * not mean the class is actually defined by the platform class loader
     *
     * Some classes in the Java SE Platform are defined by the platform class
     * loader while others are defined by the bootstrap class loader
     *
     * Applications should not depend on which class loader defines which
     * platform class
     *
     * The changes in JDK 9 may impact code that creates class loaders with null
     * (that is, the bootstrap class loader) as the parent class loader and
     * assumes that all platform classes are visible to the parent
     *
     * Such code may need to be changed to use the platform class loader as the
     * parent (see ClassLoader.getPlatformClassLoader).
     *
     */
    public Optional<ClassLoader> getPlatformClassLoader() {
        return Optional.ofNullable(java.sql.Date.class.getClassLoader());
    }

    /*
     * The bootstrap class loader is still built-in to the Java Virtual Machine
     * and represented by null in the ClassLoader API
     *
     * It defines the classes in a handful of critical modules, such as java.base
     *
     * Loads JDK internal classes, typically loads rt.jar and other core classes
     * for example java.lang.* package classes
     */
    public Optional<ClassLoader> getBootstrapClassLoader() {
        return Optional.ofNullable(String.class.getClassLoader());
    }

    public Optional<ClassLoader> getClassLoader(Class clazz) {
        return Optional.ofNullable(clazz.getClassLoader());
    }

}///:~