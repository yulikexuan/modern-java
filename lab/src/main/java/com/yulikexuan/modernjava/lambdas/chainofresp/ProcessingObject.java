//: com.yulikexuan.modernjava.lambdas.chainofresp.ProcessingObject.java


package com.yulikexuan.modernjava.lambdas.chainofresp;


import java.util.Objects;


public abstract class ProcessingObject<T> {

    protected ProcessingObject<T> successor;

    public void setSuccessor(ProcessingObject<T> successor) {
        this.successor = successor;
    }

    public T handle(T input) {
        T thisOutput = this.handleWork(input);
        if (!Objects.isNull(this.successor)) {
            return this.successor.handleWork(thisOutput);
        }
        return thisOutput;
    }

    abstract T handleWork(T input);

}///:~