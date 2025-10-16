package com.chess.engine.player.ai.boardevaluator;

import com.chess.engine.board.Board;

/**
 * Represents the board evaluator for a given chessboard. This class will have different types of board evaluators
 * that will implement this class's method(s).
 *
 * @author Jamie Canada
 * @since 10/16/25
 */
public interface BoardEvaluator {
    /**
     * Returns the score of a move after it is evaluated.
     *
     * @param board What the move takes place on.
     * @param depth How many plies of moves to look at.
     * @return The score of a move after it is evaluated.
     */
    int evaluateMove(final Board board, final int depth);

}
