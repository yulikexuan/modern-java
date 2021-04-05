//: com.yulikexuan.concurrency.threadpool.recursive.SequentialPuzzleSolver.java

package com.yulikexuan.concurrency.threadpool.recursive;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@AllArgsConstructor(staticName = "of")
public class SequentialPuzzleSolver<P, M>  {

    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public List<M> solve() {
        P pos = puzzle.initialPosition();
        return search(PuzzleNode.of(pos, null, null));
    }

    private List<M> search(PuzzleNode<P, M> node) {

        final P pos = node.getPos();

        if (!this.seen.contains(pos)) {

            this.seen.add(pos);

            if (this.puzzle.isGoal(pos)) {
                return node.asMoveList();
            }

            for (M move : puzzle.legalMoves(pos)) {
                P currentPos = puzzle.move(pos, move);
                PuzzleNode<P, M> child = PuzzleNode.of(currentPos, move, node);
                List<M> result = search(child);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

}///:~