//: com.yulikexuan.effectivejava.exceptions.IndexOutOfBoundsException.java

package com.yulikexuan.effectivejava.exceptions;


import lombok.Value;


/**
 * An exception which have a constructor with detail info
 */
@Value
public class IndexOutOfBoundsException extends RuntimeException {

    private final int lowerBound;
    private final int upperBound;
    private final int index;

    /**
     * Constructs an IndexOutOfBoundsException.
     *
     * @param lowerBound the lowest legal index value
     * @param upperBound the highest legal index value plus one
     * @param index the actualindex value
     */
    public IndexOutOfBoundsException(int lowerBound, int upperBound, int index) {

        super(String.format("Lower bound: %d, Upper bound: %d, Index: %d",
                lowerBound, upperBound, index));

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.index = index;
    }

}///:~