//: com.yulikexuan.effectivejava.stream.powerset.PowerSetFactoryTest.java

package com.yulikexuan.effectivejava.stream.powerset;


import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Lazily Loading PowerSetFactory - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PowerSetFactoryTest {

    private Set<String> charSet;
    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        this.charSet = Sets.newHashSet("a", "b", "c");
        this.random = ThreadLocalRandom.current();
    }

    @Test
    void test_Given_Index_Then_Get_Sub_Set_Of_Power_Set() {

        // Given
        int index = this.random.nextInt(8);
        List<Set<String>> powerSet = PowerSetFactory.of(this.charSet);

        // When
        Set<String> subSet = powerSet.get(index);

        // Then
        assertThat(subSet).isNotNull();
    }

}///:~