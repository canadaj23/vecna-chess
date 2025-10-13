package com.chess.engine.moves.misc;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.utils.BoardUtils.getPositionAtIndex;

/**
 * Represents a major piece's attack move.
 *
 * @author Jamie Canada
 * @since 10/13/25
 */
public class MajorAttackMove extends AttackMove {
    /**
     * Creates an AttackMove object in which the piece performs an attack.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The piece performing the move.
     * @param destinationPosition Where the piece wants to get placed at.
     * @param attackedPiece       What the piece wants to capture.
     */
    public MajorAttackMove(Board board, Piece movedPiece, Position destinationPosition, Piece attackedPiece) {
        super(board, movedPiece, destinationPosition, attackedPiece);
    }

    /**
     * Returns whether the move and the other move are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the move and the other move are referentially and objectively equal.
     */
    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof MajorAttackMove && super.equals(other);
    }

    /**
     * Returns the String representation of a major piece capturing another piece.
     *
     * @return The String representation of a major piece capturing another piece.
     */
    @Override
    public String toString() {
        return movedPiece.getPieceType() + getPositionAtIndex(this.getDestinationIndex());
    }
}
