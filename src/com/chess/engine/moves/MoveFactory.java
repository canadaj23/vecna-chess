package com.chess.engine.moves;

import com.chess.engine.Position;
import com.chess.engine.board.Board;

import static com.chess.engine.moves.Move.NULL_MOVE;

/**
 * Retrieves a move to be used during the player's turn.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class MoveFactory {
    /**
     * Returns either a legal or null move based on the current and destination indices.
     *
     * @param board               What the move takes place on.
     * @param currentPosition     Where the piece currently is.
     * @param destinationPosition Where the piece wants to move to.
     * @return A legal move based on the current and destination indices.
     */
    public static Move findMove(final Board board,
                                final Position currentPosition,
                                final Position destinationPosition) {
        for (final Move move : board.getAllLegalMoves()) {
            if (move.getCurrentPosition() == currentPosition &&
                move.getDestinationPosition() == destinationPosition) {
                return move;
            }
        }

        return NULL_MOVE;
    }
}
