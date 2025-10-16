package com.chess.engine.moves.pawn.attack;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.misc.AttackMove;
import com.chess.engine.pieces.Piece;
import com.chess.engine.utils.BoardUtils;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.*;

/**
 * Represents the pawn's attacks. This class extends the AttackMove class, and may implement some of
 * its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PawnAttackMove extends AttackMove {
    /**
     * Creates a PawnAttackMove object based on a normal attack or an en passant.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     * @param attackedPiece       The piece the pawn wants to capture.
     */
    public PawnAttackMove(final Board board,
                          final Piece movedPiece,
                          final Position destinationPosition,
                          final Piece attackedPiece) {
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
        return other instanceof PawnAttackMove && super.equals(other);
    }

    /**
     * Returns the String representation of a pawn capturing another piece.
     *
     * @return The String representation of a pawn capturing another piece.
     */
    @Override
    public String toString() {
        final int capturedPieceIndex = getPositionIndex(this.getAttackedPiece().getPiecePosition().rank(),
                                                        this.getAttackedPiece().getPiecePosition().file());
        return getPositionAtIndex(this.getCurrentIndex()).charAt(0) + "x" + getPositionAtIndex(capturedPieceIndex);
    }
}
