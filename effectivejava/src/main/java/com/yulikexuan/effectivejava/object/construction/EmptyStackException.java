//: com.yulikexuan.effectivejava.object.construction.EmptyStackException.java


package com.yulikexuan.effectivejava.object.construction;


public class EmptyStackException extends IllegalStateException {

    public EmptyStackException() {
        super();
    }

    public EmptyStackException(String s) {
        super(s);
    }

    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyStackException(Throwable cause) {
        super(cause);
    }

}///:~