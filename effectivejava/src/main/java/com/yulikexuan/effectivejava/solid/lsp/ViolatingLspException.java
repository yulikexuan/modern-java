//: com.yulikexuan.effectivejava.solid.lsp.ViolatingLspException.java


package com.yulikexuan.effectivejava.solid.lsp;


/*
 * Violate Liskov Substitution Principle
 */
public class ViolatingLspException extends RuntimeException {

    public ViolatingLspException() {
        super();
    }

    public ViolatingLspException(String message) {
        super(message);
    }

}///:~