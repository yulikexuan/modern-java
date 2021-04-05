//: com.yulikexuan.concurrency.threadpool.recursive.PuzzleNode.java

package com.yulikexuan.concurrency.threadpool.recursive;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * PuzzleNode
 * <p/>
 * Link node for the puzzle solving framework
 *
 * PuzzleNode represents a position that has been reached through some series
 * of moves, holding a reference to the move that created the position and the
 * previous PuzzleNode
 *
 * @author Brian Goetz and Tim Peierls
 */
@Slf4j
@Getter
class PuzzleNode<P, M> {

    final P pos;
    final M move;
    final PuzzleNode<P, M> prev;

    PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    public static <P, M> PuzzleNode<P, M> of(P pos, M move, PuzzleNode<P, M> prev) {
        return new PuzzleNode<>(pos, move, prev);
    }

    List<M> asMoveList() {

        List<M> solution = new LinkedList<M>();

        for (PuzzleNode<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }

        return solution;
    }

}///:~