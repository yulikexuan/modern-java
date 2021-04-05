//: com.yulikexuan.concurrency.threadpool.recursive.Puzzle.java

package com.yulikexuan.concurrency.threadpool.recursive;


import java.util.Set;


/**
 * Puzzle
 * <p/>
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 *
 * Puzzle in shows our puzzle abstraction;
 * the type parameters P and M represent the classes for a position and a move
 *
 * @author Brian Goetz and Tim Peierls
 */
public interface Puzzle<P, M> {

    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position, M move);

}///:~