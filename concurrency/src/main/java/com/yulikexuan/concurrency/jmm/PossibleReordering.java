//: com.yulikexuan.concurrency.jmm.PossibleReordering.java

package com.yulikexuan.concurrency.jmm;


public class PossibleReordering {

    private static final String OUT_PUT_TEMPLATE = "(%s, %s) - (%d, %d)";

    private static int x = 0;
    private static int y = 0;

    private static int a = 0;
    private static int b = 0;

    /*
     * It is fairly easy to imagine how PossibleReordering could print
     *   - (1, 0) : Thread B could run to completion before A starts
     *   - (0, 1) : Thread A could run to completion before B starts
     *   - (1, 1) : Actions in each thread could be interleaved
     *   - (0, 0) : The actions in each thread have no dataflow dependence on
     *              each other, and accordingly can be executed out of order
     */
    public static void main(String[] args) throws InterruptedException {

        Thread one = new Thread(() -> { a = 1; x = b; }); // Thread A
        Thread other = new Thread(() -> { b = 1; y = a; }); // Thread B

        one.start();
        other.start();

        one.join();
        other.join();

        System.out.println(String.format(OUT_PUT_TEMPLATE, 'x', 'y', x, y));
    }

}///:~