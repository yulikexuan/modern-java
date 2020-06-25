//: com.yulikexuan.effectivejava.object.construction.RoomTest.java


package com.yulikexuan.effectivejava.object.construction;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("JDK Cleaner Test - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RoomTest {

    private int numJunkPiles;
    private Room room;

    @BeforeEach
    void setUp() {
        this.numJunkPiles = RandomUtils.nextInt(10, 50);
    }

    @Test
    void test_Cleaning_Room_By_An_Adult() throws Exception {

        // Given
        Room.State dirtyState = Room.State.of(this.numJunkPiles);

        // When
        try (Room room = Room.of(dirtyState)) {
            assertThat(room.isDirty()).isTrue();
        }

        // Then
        assertThat(dirtyState.isDirty()).isFalse();
    }

    @Test
    void test_Cleaning_Room_By_An_Kid() throws Exception {

        // Given
        Room.State dirtyState = Room.State.of(this.numJunkPiles);
        Room room = Room.of(dirtyState);

        // When & Then
        assertThat(room.isDirty()).isTrue();
    }

}///:~