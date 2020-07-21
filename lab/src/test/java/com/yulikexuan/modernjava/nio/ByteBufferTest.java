//: com.yulikexuan.modernjava.nio.ByteBufferTest.java


package com.yulikexuan.modernjava.nio;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Java nio Buffer Test - ")
public class ByteBufferTest {

    private static final String TEST_DATA = "Effective Java the Third Edition";

    private String data;
    private byte[] dataBytes;
    private ByteBuffer byteBuffer;

    private int initPos;
    private int initLimit;
    private int initCapacity;

    private int curPos;
    private int curLimit;
    private int curCapacity;

    @BeforeEach
    void setUp() {
        this.data = TEST_DATA;
        this.dataBytes = this.data.getBytes(StandardCharsets.UTF_8);
        this.byteBuffer = ByteBuffer.wrap(this.dataBytes);
        this.initPos = this.byteBuffer.position();
        this.initLimit = this.byteBuffer.limit();
        this.initCapacity = this.byteBuffer.capacity();
    }

    private void update() {
        curPos = this.byteBuffer.position();
        curLimit = this.byteBuffer.limit();
        curCapacity = this.byteBuffer.capacity();
    }

    private void printCurrentState(ByteBuffer byteBuffer) {
        log.info(">>>>>>> The current position is: {}", byteBuffer.position());
        log.info(">>>>>>> The current limit is: {}", byteBuffer.limit());
        log.info(">>>>>>> The current capacity is: {}", byteBuffer.capacity());
    }

    @Test
    void test_Given_ByteBuffer_Then_Get_Two_Integers_Directly_With_Using_Array_Index() {

        // Given
        int firstInt = this.byteBuffer.getChar(0);
        int secondInt = this.byteBuffer.getChar(2);

        // When
        update();

        // Then
        assertThat(curPos).isEqualTo(initPos);
        assertThat(curLimit).isEqualTo(initLimit);
        assertThat(curCapacity).isEqualTo(initCapacity);
    }

    @Test
    void test_Given_ByteBuffer_Then_Get_Two_Integers_Indirectly() {

        // Given
        int firstInt = this.byteBuffer.getChar();
        int secondInt = this.byteBuffer.getChar();

        // When
        update();

        // Then
        // Then
        assertThat(curPos).isEqualTo(initPos + 4);
        assertThat(curLimit).isEqualTo(initLimit);
        assertThat(curCapacity).isEqualTo(initCapacity);
    }

    @Test
    void test_Given_An_Byte_Array_When_Put_It_Into_Byte_Buffer_Without_Flipping_Then_Getting_Empty_ByteBuffer() {

        // Given
        int size = this.dataBytes.length;
        byte[] backBytes = new byte[size];

        // When
        final ByteBuffer byteBuffer = ByteBuffer.wrap(backBytes);

        /*
         * Relative bulk put method  (optional operation)
         * This method transfers the entire content of the given source byte
         * array into this buffer
         * An invocation of this method of the form dst.put(a) behaves in
         * exactly the same way as the invocation
         *     dst.put(a, 0, a.length)
         */
        byteBuffer.put(this.dataBytes);

        // Then
        assertThat(byteBuffer.position()).isEqualTo(size);
        assertThat(byteBuffer.limit()).isEqualTo(byteBuffer.position());
        assertThat(byteBuffer.capacity()).isEqualTo(byteBuffer.position());
    }

    @Test
    void test_Given_An_Byte_Array_When_Put_It_Into_Byte_Buffer_With_Flipping_Then_Getting_ByteBuffer_Ready_For_Read() {

        // Given
        int size = this.dataBytes.length;
        byte[] backBytes = new byte[size];

        // When
        final ByteBuffer byteBuffer = ByteBuffer.wrap(backBytes);

        /*
         * Relative bulk put method  (optional operation)
         * This method transfers the entire content of the given source byte
         * array into this buffer
         * An invocation of this method of the form dst.put(a) behaves in
         * exactly the same way as the invocation
         *     dst.put(a, 0, a.length)
         */
        byteBuffer.put(this.dataBytes);

        /*
         * The limit is set to the current position and then the position is
         * set to zero. If the mark is defined then it is discarded.
         */
        byteBuffer.flip();

        // Then
        assertThat(byteBuffer.position()).isEqualTo(0);
        assertThat(byteBuffer.limit()).isEqualTo(size);
        assertThat(byteBuffer.capacity()).isEqualTo(size);
    }

    @Test
    void test_Given_Duplilcated_ByteBuffer_When_Creating_String_From_Byte_Array_Then_NO_Side_Effect() {

        // Given

        /*
         * ByteBuffer::remaining:
         *   Returns the number of elements between the current position and
         *   the limit
         */
        int size = this.byteBuffer.remaining();

        final byte[] bytes = new byte[size];

        /*
         * Creates a new byte buffer that shares this buffer's content
         * The content of the new buffer will be that of this buffer
         * Changes to this buffer's content will be visible in the new buffer
         * and vice versa
         * the two buffers' position, limit, and mark values will be independent
         *
         * The new buffer's capacity, limit, position, and mark values will be
         * identical to those of this buffer, and its byte order will be
         * BIG_ENDIAN
         *
         * The new buffer will be direct if, and only if, this buffer is direct,
         * and it will be read-only if, and only if, this buffer is read-only.
         */
        ByteBuffer duplicatedBuffer = this.byteBuffer.duplicate();

        /*
         * Relative bulk get method
         *
         * This method transfers bytes from this buffer into the given
         * destination array
         *
         * An invocation of this method of the form src.get(a) behaves in
         * exactly the same way as the invocation
         *
         *    src.get(a, 0, a.length)
         */
        duplicatedBuffer.get(bytes);

        // When
        String message = new String(bytes);

        // Then
        assertThat(message).isEqualTo(TEST_DATA);
        assertThat(this.byteBuffer.position()).isEqualTo(0);
        assertThat(this.byteBuffer.remaining()).isEqualTo(size);
        assertThat(duplicatedBuffer.position()).isEqualTo(size);
        assertThat(duplicatedBuffer.remaining()).isEqualTo(0);

//        log.info(">>>>>> The String in the duplicated ByteBuffer is: '{}'", message);
//
//        log.info(">>>>>>> Print the state of the original ByteBuffer: ");
//        printCurrentState(this.byteBuffer);
//
//        log.info(">>>>>>> Print the state of the duplicated ByteBuffer: ");
//        printCurrentState(duplicatedBuffer);
    }

}///:~