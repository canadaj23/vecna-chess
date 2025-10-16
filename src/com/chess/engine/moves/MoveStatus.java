package com.chess.engine.moves;

/**
 * Represents the status of a move.
 * E.g., the move may be DONE.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */

public enum MoveStatus {
    DONE {
        /**
         * Returns whether the move is done.
         *
         * @return Whether the move is done.
         */
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE {

    },
    IN_CHECK {

    };

    /**
     * Returns whether the move is done.
     *
     * @return Whether the move is done.
     */
    public boolean isDone() {
        return false;
    }
}
