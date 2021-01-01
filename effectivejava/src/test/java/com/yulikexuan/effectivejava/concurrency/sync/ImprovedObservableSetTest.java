//: com.yulikexuan.effectivejava.concurrency.sync.ImprovedObservableSetTest.java

package com.yulikexuan.effectivejava.concurrency.sync;


import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test ImprovedObservableSet - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ImprovedObservableSetTest {

    private Set<Integer> set;
    private ImprovedObservableSet<Integer> observableSet;

    @BeforeEach
    void setUp() {
        this.set = Sets.newHashSet();
        this.observableSet = ImprovedObservableSet.of(this.set);
    }

    @Test
    void test_Single_Observer_To_Remove_Itself_Successfully() {

        // Given
        final List<Integer> container = Lists.newArrayList();
        final int threshold = 23;
        this.observableSet.addObserver(new SetObserver<>() {
            @Override
            public void added(IObservableSet<Integer> set, Integer element) {
                container.add(element);
                if (element.intValue() == threshold) {
                    set.removeObserver(this);
                }
            }
        });

        // When
        IntStream.range(0, 100).forEach(
                i -> this.observableSet.add(i));

        // Then
        assertThat(container).hasSize(threshold + 1);
    }

}///:~