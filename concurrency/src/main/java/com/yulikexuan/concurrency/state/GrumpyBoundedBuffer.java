//: com.yulikexuan.concurrency.state.GrumpyBoundedBuffer.java

package com.yulikexuan.concurrency.state;


import javax.annotation.concurrent.ThreadSafe;


/**
 * GrumpyBoundedBuffer
 * <p/>
 * Bounded buffer that balks when preconditions are not met
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    private GrumpyBoundedBuffer() {
        this(100);
    }

    protected GrumpyBoundedBuffer(int size) {
        super(size);
    }

    public static <V> GrumpyBoundedBuffer of(int size) {
        return new GrumpyBoundedBuffer(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }

}

class BufferFullException extends RuntimeException {
}

class BufferEmptyException extends RuntimeException {
}

///:~