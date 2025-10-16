package com.chess.engine.player.ai.movestrategy;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

/**
 * Represents the strategy the AI will use to find the best move. There are different searching algorithms for the AI
 * to use, so this class serves as the backbone for those algorithms to extend from.
 *
 * @author Jamie Canada
 * @since 10/16/25
 */
public interface MoveStrategy {
    /**
     * Returns a move based on the move strategy used.
     *
     * @param board What the move will take place on.
     * @return A move based on the move strategy used.
     */
    Move execute(final Board board);
}
