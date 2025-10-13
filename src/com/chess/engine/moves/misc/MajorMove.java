package com.chess.engine.moves.misc;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.getPositionAtIndex;

/**
 * Represents a typical move performed in chess: moving to another square. This class extends the Move class, and may
 * implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/08/25
 */
public class MajorMove extends Move {
    /**
     * Creates a MajorMove object in which piece performs a move.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The piece performing the move.
     * @param destinationPosition Where the piece wants to get placed at.
     */
    public MajorMove(final Board board, final Piece movedPiece, final Position destinationPosition) {
        super(board, movedPiece, destinationPosition);
    }

    /**
     * Returns whether the major move and the other major move are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the major move and the other major move are referentially and objectively equal.
     */
    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof MajorMove && super.equals(other);
    }

    /**
     * Returns the String representation of a major piece move.
     *
     * @return The String representation of a major piece move.
     */
    @Override
    public String toString() {
        return movedPiece.getPieceType().toString() + getPositionAtIndex(this.getDestinationIndex());
    }
}
