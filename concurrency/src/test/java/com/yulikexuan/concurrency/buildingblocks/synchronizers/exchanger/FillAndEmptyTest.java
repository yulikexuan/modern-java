//: com.yulikexuan.concurrency.buildingblocks.synchronizers.exchanger.FillAndEmptyTest.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.exchanger;


import org.junit.jupiter.api.*;

import java.io.IOException;


@DisplayName("Test Exchanger with ByteBuffers - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FillAndEmptyTest {

    private FillAndEmpty fillAndEmpty;

    @BeforeEach
    void setUp() throws IOException {
        this.fillAndEmpty = new FillAndEmpty();
    }

    @Test
    void start_To_Exchanger() {
        this.fillAndEmpty.start();
    }

}///:~