//: com.yulikexuan.jdk.ocp2.nio.PathRelativizationTest.java

package com.yulikexuan.jdk.ocp2.nio;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test Path Relativization - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PathRelativizationTest {

    private PathRelativization relativization;

    @BeforeEach
    void setUp() {
        this.relativization = new PathRelativization();
    }

    @Test
    void given_Two_Paths_Containing_Root_When_Relativizing_Then_Get_Relative_Path() {

        // Given
        Path path1 = Paths.get("c:/x");
        Path path2 = Paths.get("c:/x/y/a");

        // When
        String relativePath = this.relativization.relativize(path1, path2);

        // Then
        assertThat(relativePath).isEqualTo("y\\a");
    }

    @Test
    void given_Two_Paths_Not_Containing_Root_When_Relativizing_Then_Get_Relative_Path() {

        // Given
        Path path1 = Paths.get("x/y");
        Path path2 = Paths.get("x/y/a");

        // When
        String relativePath = this.relativization.relativize(path1, path2);

        // Then
        assertThat(relativePath).isEqualTo("a");
    }

    @Test
    void given_Two_Paths_Only_One_Contains_Root_When_Relativizing_Then_Throw_IllegalArgumentException() {

        // Given
        Path path1 = Paths.get("/x/y");
        Path path2 = Paths.get("x/y/a");

        // When & Then
        assertThatThrownBy(() -> this.relativization.relativize(path1, path2))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("different type of Path");
    }

}///:~