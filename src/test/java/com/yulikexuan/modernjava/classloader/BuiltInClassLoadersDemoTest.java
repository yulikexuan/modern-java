//: com.yulikexuan.modernjava.classloader.BuiltInClassLoadersDemoTest.java


package com.yulikexuan.modernjava.classloader;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


@Slf4j
class BuiltInClassLoadersDemoTest {

    private BuiltInClassLoadersDemo demo;

    @BeforeEach
    void setUp() {
        this.demo = new BuiltInClassLoadersDemo();
    }

    @Test
    void test_App_Class_Loader() {
        this.demo.getApplicationClassLoader()
                .ifPresentOrElse(this::printClassLoaderDetails,
                        this::printBootstrapClassLoader);
    }

    @Test
    void test_Platform_Class_Loader() {
        this.demo.getPlatformClassLoader()
                .ifPresentOrElse(this::printClassLoaderDetails,
                        this::printBootstrapClassLoader);
    }

    @Test
    void test_Bootstrap_Class_Loader() {
        this.demo.getBootstrapClassLoader()
                .ifPresentOrElse(this::printClassLoaderDetails,
                        this::printBootstrapClassLoader);
    }

    @Test
    void test_Any_Class_Loader() {
        this.demo.getClassLoader(Integer.class)
                .ifPresentOrElse(this::printClassLoaderDetails,
                        this::printBootstrapClassLoader);
    }

    private void printBootstrapClassLoader() {
        log.info("\n\t>>>>>>> Bootstrap classloader");
    }

    private void printClassLoaderDetails(ClassLoader classLoader) {
        log.info("\n\t>>>>>>> ClassLoader name is {}", classLoader.getName());
        log.info("\n\t>>>>>>> ClassLoader class is {}", classLoader.getClass().getName());
        Optional.ofNullable(classLoader.getParent())
                .ifPresentOrElse(this::printClassLoaderDetails, this::printBootstrapClassLoader);
    }

}///:~