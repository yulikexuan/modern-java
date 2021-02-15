//: com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers.Worker.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers;


import lombok.NonNull;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


final class Worker implements Runnable {

    private final IBoard board;
    private final CyclicBarrier barrier;

    private Worker(IBoard board, CyclicBarrier barrier) {
        this.board = board;
        this.barrier = barrier;
    }

    public static Worker of(@NonNull IBoard board,
                            @NonNull CyclicBarrier barrier) {
        return new Worker(board, barrier);
    }

    @Override
    public void run() {

        while (!board.hasConverged()) {

            for (int x = 0; x < board.getMaxX(); x++) {
                for (int y = 0; y < board.getMaxY(); y++) {
                    board.setNewValue(x, y, computeValue(x, y));
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
        }
    }

    private int computeValue(int x, int y) {
        // Compute the new value that goes in (x,y)
        return 0;
    }

}///:~