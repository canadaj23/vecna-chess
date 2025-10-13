package com.chess.engine.moves;

import com.chess.engine.board.Board;

/**
 * Represents the transition from one board to a subsequent board based on a move performed.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    /**
     * Creates a MoveTransition object based on the board to transition to, the move performed, and the move's status.
     *
     * @param transitionBoard The board to use after a move is performed.
     * @param move            What the piece is trying to do.
     * @param moveStatus      The move's status.
     */
    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    /**
     * Returns the board to transition to after a move is performed.
     *
     * @return The board to transition to after a move is performed.
     */
    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    /**
     * Returns the status of the move.
     *
     * @return The status of the move.
     */

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
