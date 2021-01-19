//: com.yulikexuan.concurrency.thread.publication.SafeThisListener.java

package com.yulikexuan.concurrency.thread.publication;


import lombok.NonNull;


public class SafeThisListener {

    private final EventListener eventListener;

    private SafeThisListener() {
        this.eventListener = new EventListener() {
            @Override
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    public static SafeThisListener newInstance(@NonNull EventSource eventSource) {
        SafeThisListener stl = new SafeThisListener();
        eventSource.registerListener(stl.eventListener);
        return stl;
    }

    void doSomething(Event e) {
    }

    interface EventSource {
        void registerListener(EventListener e);
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {
    }

}///:~