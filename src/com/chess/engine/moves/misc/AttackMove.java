package com.chess.engine.moves.misc;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

/**
 * Represents an attack move (i.e., piece capture) performed in chess. This class extends the Move class, and may
 * implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/08/25
 */
public class AttackMove extends Move {
    private final Piece attackedPiece;

    /**
     * Creates a AttackMove object in which the piece performs an attack.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The piece performing the move.
     * @param destinationPosition Where the piece wants to get placed at.
     * @param attackedPiece       What the piece wants to capture.
     */
    public AttackMove(final Board board,
               final Piece movedPiece,
               final Position destinationPosition,
               final Piece attackedPiece) {
        super(board, movedPiece, destinationPosition);
        this.attackedPiece = attackedPiece;
    }

    /**
     * Returns a new board based off the attack move performed.
     *
     * @return A new board based off the attack move performed.
     */
    @Override
    public Board execute() {
        return null;
    }

    /**
     * Returns whether the move is an attack.
     *
     * @return Whether the move is an attack.
     */
    @Override
    public boolean isAttack() {
        return true;
    }

    /**
     * Returns the piece currently under attack.
     *
     * @return The piece currently under attack.
     */
    @Override
    public Piece getAttackedPiece() {
        return this.attackedPiece;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AttackMove otherAttackMove)) {
            return false;
        }

        return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
    }

    @Override
    public int hashCode() {
        return this.attackedPiece.hashCode() + super.hashCode();
    }
}
