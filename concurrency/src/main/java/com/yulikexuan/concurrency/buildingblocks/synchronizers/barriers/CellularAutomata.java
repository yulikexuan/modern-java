//: com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers.CellularAutomata.java

package com.yulikexuan.concurrency.buildingblocks.synchronizers.barriers;


import lombok.NonNull;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CellularAutomata
 *
 * Coordinating computation in a cellular automaton with CyclicBarrier
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CellularAutomata {

    private final IBoard mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    private CellularAutomata(@NonNull IBoard board,
                             @NonNull CyclicBarrier barrier,
                             @NonNull Worker[] workers) {

        this.mainBoard = board;
        this.barrier = barrier;
        this.workers = workers;
    }

    public static CellularAutomata of(IBoard board) {

        int count = Runtime.getRuntime().availableProcessors();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count,
                () -> board.commitNewValues());

        Worker[] workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = Worker.of(board.getSubBoard(count, i), cyclicBarrier);
        }

        return new CellularAutomata(board, cyclicBarrier, workers);
    }

    public void start() {

        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i]).start();
        }

        mainBoard.waitForConvergence();
    }

}///:~