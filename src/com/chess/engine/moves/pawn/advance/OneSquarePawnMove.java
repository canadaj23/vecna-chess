package com.chess.engine.moves.pawn.advance;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.utils.BoardUtils.getPositionAtIndex;

/**
 * Represents the move when the pawn advances by one square. This class extends the Move class, and may implement some
 * of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class OneSquarePawnMove extends Move {
    /**
     * Creates a OneSquarePawnMove object in which the pawn advances by one square.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     */
    public OneSquarePawnMove(final Board board, final Piece movedPiece, final Position destinationPosition) {
        super(board, movedPiece, destinationPosition);
    }

    /**
     * Returns whether the move and the other move are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the move and the other move are referentially and objectively equal.
     */
    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof OneSquarePawnMove && super.equals(other);
    }

    /**
     * Returns the String representation of the one-square pawn advance.
     *
     * @return The String representation of the one-square pawn advance.
     */
    @Override
    public String toString() {
        return getPositionAtIndex(this.getDestinationIndex());
    }
}
