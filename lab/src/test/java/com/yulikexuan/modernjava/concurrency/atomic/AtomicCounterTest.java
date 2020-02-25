//: com.yulikexuan.modernjava.concurrency.atomic.AtomicCounterTest.java


package com.yulikexuan.modernjava.concurrency.atomic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.LongBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;


class AtomicCounterTest {

    private AtomicCounter counter;

    @BeforeEach
    void setUp() {
        this.counter = AtomicCounter.of(0L);
    }

    @Test
    void test_Get_And_Update() {

        // Given & When
        long previousValue = this.counter.getAndUpdate(val -> val + 2);

        // Then
        assertThat(previousValue).isEqualTo(0);
        assertThat(this.counter.getCounter()).isEqualTo(2);
    }

    @Test
    void test_Update_And_Get() {

        // Given & When
        long updatedValue = this.counter.updateAndGet(val -> val + 2);

        // Then
        assertThat(updatedValue).isEqualTo(2);
        assertThat(this.counter.getCounter()).isEqualTo(updatedValue);
    }

    @Test
    void test_Get_And_Accumulate() {

        // Given & When
        long param = 100;
        LongBinaryOperator accumulatorFunction =
                (currentVal, x) -> currentVal + x;

        long previousValue = this.counter.getAndAccumulate(
            param, accumulatorFunction);

        previousValue = this.counter.getAndAccumulate(param, accumulatorFunction);

        // Then
        assertThat(previousValue).isEqualTo(param);
        assertThat(this.counter.getCounter()).isEqualTo(param * 2);
    }

    @Test
    void test_Accumulate_And_Get() {

        // Given & When
        long param = 100;
        LongBinaryOperator accumulatorFunction =
                (currentVal, x) -> currentVal + x;

        long newValue = this.counter.getAndAccumulate(
                param, accumulatorFunction);

        newValue = this.counter.accumulateAndGet(param, accumulatorFunction);

        // Then
        assertThat(newValue).isEqualTo(param * 2);
        assertThat(this.counter.getCounter()).isEqualTo(newValue);
    }

}///:~