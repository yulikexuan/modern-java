//: com.yulikexuan.effectivejava.object.construction.AvoidCreatingUnnecessaryObjectsTest.java


package com.yulikexuan.effectivejava.object.construction;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test reusing objects - ")
// @ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AvoidCreatingUnnecessaryObjectsTest {

    @BeforeEach
    void setUp() {
    }

    void test_Reusing_Strings() {

        // Given
        String info_1 = "Bikini";
        String info_2 = "Bikini";

        String info_3 = new String("Bikini");
        String info_4 = new String("Bikini");

        // When
        boolean reused_1_2 = (info_1 == info_2);
        boolean reused_3_4 = (info_4 == info_3);
        boolean reused_2_3 = (info_2 == info_3);
        boolean reused_1_4 = (info_1 == info_4);

        // Then
        assertThat(reused_1_2).isTrue();
        assertThat(reused_3_4).isFalse();
        assertThat(reused_2_3).isFalse();
        assertThat(reused_1_4).isFalse();
    }

    @Test
    void test_Reusing_Booleans() {

        // Given
        Boolean boolean_1 = Boolean.valueOf("true");
        Boolean boolean_2 = Boolean.valueOf("true");
        Boolean boolean_3 = true;

        // When
        boolean reused = (boolean_1 == boolean_2);
        boolean reused_1_3 = (boolean_1 == boolean_3);

        // Then
        assertThat(reused).isTrue();
        assertThat(reused_1_3).isTrue();
    }

}///:~