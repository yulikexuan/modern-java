//: com.yulikexuan.effectivejava.generics.EmptyStackException.java


package com.yulikexuan.effectivejava.generics;


public class EmptyStackException extends RuntimeException {

    public EmptyStackException() {
    }

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }

}///:~