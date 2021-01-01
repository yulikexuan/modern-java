//: com.yulikexuan.effectivejava.concurrency.sync.ObservableSetTest.java

package com.yulikexuan.effectivejava.concurrency.sync;


import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.awaitility.Durations;
import org.junit.jupiter.api.*;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;


@DisplayName("Test ObservableSet - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ObservableSetTest {

    private Set<Integer> set;
    private ObservableSet<Integer> observableSet;

    @BeforeEach
    void setUp() {
        this.set = Sets.newHashSet();
        this.observableSet = ObservableSet.of(this.set);
    }

    @Test
    void test_Single_Observer_To_Track_Every_Added_Element() {

        // Given
        final List<Integer> resultContainer = Lists.newArrayList();
        this.observableSet.addObserver((os, e) -> resultContainer.add(e));

        // When
        IntStream.range(0, 100).forEach(i -> this.observableSet.add(i));

        // Then
        assertThat(resultContainer).hasSize(100).contains(0, 99);
    }

    @Test
    void test_Single_Observer_To_Remove_Itself_Then_Fall_In_ConcurrentModificationException() {

        // Given
        final List<Integer> resultContainer = Lists.newArrayList();

        // When
        this.observableSet.addObserver(new SetObserver<>() {
            @Override
            public void added(IObservableSet<Integer> set, Integer element) {
                resultContainer.add(element);
                if (element.intValue() == 23) {
                    set.removeObserver(this);
                }
            }
        });

        // Then
        assertThatThrownBy(() ->IntStream.range(0, 100)
                .forEach(i -> this.observableSet.add(i)))
                .isInstanceOf(ConcurrentModificationException.class);
    }

    @Test
    void test_Single_Observer_To_Remove_Itself_With_Another_Thread_Then_Deadlock() {

        // Given
        final List<Integer> resultContainer = Lists.newArrayList();

        // When
        this.observableSet.addObserver(new SetObserver<>() {
            @Override
            public void added(IObservableSet<Integer> set, Integer element) {
                resultContainer.add(element);
                if (element.intValue() == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> set.removeObserver(this)).get();
                    } catch(InterruptedException | ExecutionException e) {
                        throw new AssertionError(e);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        // Then
        assertThatThrownBy(() -> await().atMost(Durations.TWO_SECONDS)
                .until(resultContainer::size, equalTo(23)))
                .isInstanceOf(org.awaitility.core.ConditionTimeoutException.class);
    }

}///:~