//: com.yulikexuan.effectivejava.stream.sublist.SubListsTest.java

package com.yulikexuan.effectivejava.stream.sublist;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test SubListsTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SubListsTest {

    private List<String> source;
    private int size;

    @BeforeEach
    void setUp() {
        this.source = Lists.newArrayList("a", "b", "c");
        this.size = this.source.size();
    }

    @Test
    void able_To_Generate_Prefixes_Of_A_List() {

        // Given

        // When
        List<List<String>> prefixes = SubLists.prefixes(this.source)
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(prefixes).containsExactly(
                List.of("a"), List.of("a", "b"), this.source);
    }

    @Test
    void able_To_Generate_Suffixes_Of_A_List() {

        // Given

        // When
        List<List<String>> prefixes = SubLists.suffixes(this.source)
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(prefixes).containsExactly(
                this.source, List.of("b", "c"), List.of("c"));
    }

    @Test
    void able_To_Retrieve_All_Sub_Lists_Of_A_List() {

        // Given

        // When
        List<List<String>> subLists = SubLists.of(this.source)
                .collect(ImmutableList.toImmutableList());

        // Then
        assertThat(subLists).hasSize(7);
    }

}///:~