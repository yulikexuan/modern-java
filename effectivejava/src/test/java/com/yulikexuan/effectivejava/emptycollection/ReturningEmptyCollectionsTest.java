//: com.yulikexuan.effectivejava.emptycollection.ReturningEmptyCollectionsTest.java

package com.yulikexuan.effectivejava.emptycollection;


import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Prefer Returning Empty Collections instead of Null - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReturningEmptyCollectionsTest {

    @Test
    void test_Getting_Empty_List_From_Null_Collection() {

        // Given

        // When
        List<String> names_1 = ReturningEmptyCollections.getListOptimized(
                null);

        List<String> names_2 = ReturningEmptyCollections.getListOptimized(
                Sets.newHashSet());

        // Then
        assertThat(names_1).isSameAs(Collections.EMPTY_LIST);
        assertThat(names_2).isSameAs(Collections.EMPTY_LIST);
    }

}///:~