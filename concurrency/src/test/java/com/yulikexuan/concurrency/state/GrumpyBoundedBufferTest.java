//: com.yulikexuan.concurrency.state.GrumpyBoundedBufferTest.java

package com.yulikexuan.concurrency.state;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Test GrumpyBoundedBufferTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GrumpyBoundedBufferTest {

    private static final int SLEEP_GRANULARITY_MILLIS = 50;
    private static final int BUFFER_CAPACITY = 10;

    private GrumpyBoundedBuffer<String> stringBuffer;

    @BeforeEach
    void setUp() {
        this.stringBuffer = GrumpyBoundedBuffer.of(BUFFER_CAPACITY);
    }

    @Test
    void single_Thread_Test_BufferEmptyException() {

        assertThatThrownBy(() -> this.stringBuffer.take())
                .isInstanceOf(BufferEmptyException.class);
    }

    @Test
    void single_Thread_Test_BufferFullException() {

        assertThatThrownBy(() -> {
            while (true) {
                this.stringBuffer.put(RandomStringUtils.randomAlphanumeric(7));
            }
        });
    }

}///:~