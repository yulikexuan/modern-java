//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.InvalidTemperatureException.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;

public class InvalidTemperatureException extends RuntimeException {

    public InvalidTemperatureException() {
        super();
    }

    public InvalidTemperatureException(String message) {
        super(message);
    }

    public InvalidTemperatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTemperatureException(
            String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }

}///:~