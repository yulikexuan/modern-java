//: com.yulikexuan.modernjava.concurrency.threadlocal.BasicThreadLocalTest.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import org.junit.jupiter.api.*;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test basic of ThreadLocal - ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BasicThreadLocalTest {

    private int initialVal;
    private ThreadLocal<Integer> intThreadLocal;
    
    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();
    }

    @Test
    @Order(0)
    void test_Getter_And_Setter_Of_ThreadLocal() {

        // Given
        this.initialVal = this.random.nextInt(100);
        this.intThreadLocal = ThreadLocal.withInitial(() -> initialVal);

        // When
        System.out.printf("The initial value is %d%n", this.initialVal);
        Integer snapshot = intThreadLocal.get();

        // Then
        assertThat(snapshot).isEqualTo(initialVal);
    }

    @Test
    @Order(1)
    void test_The_Value_Of_ThreadLocal_Can_Be_Reinitialized_After_Removing() {

        // Given
        this.initialVal = this.random.nextInt(100);
        this.intThreadLocal = ThreadLocal.withInitial(() -> initialVal);

        // When
        this.intThreadLocal.remove();
        var newValue = this.intThreadLocal.get();

        // Then
        assertThat(newValue).isEqualTo(initialVal);
    }

    @Test
    @Order(1)
    void test_The_Value_Of_No_Initializer_ThreadLocal_After_Calling_Remove() {

        // Given
        this.initialVal = this.random.nextInt(100);
        this.intThreadLocal = new ThreadLocal();
        this.intThreadLocal.set(this.initialVal);

        // When
        this.intThreadLocal.remove();
        var newValue = this.intThreadLocal.get();

        // Then
        assertThat(newValue).isNull();
    }
    
}///:~