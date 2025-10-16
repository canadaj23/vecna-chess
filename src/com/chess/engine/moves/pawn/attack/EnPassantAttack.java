package com.chess.engine.moves.pawn.attack;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.getPositionAtIndex;
import static com.chess.engine.utils.BuilderUtils.setNonMovingEnPassantPieces;

/**
 * Represents the pawn's en passant attack. When the opponent's pawn advances by two squares (to the bottom left or
 * right square of the player's pawn's diagonal), the player's pawn can capture this pawn by advancing to the square
 * behind the opponent's pawn. This class extends the PawnAttackMove class, and may implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class EnPassantAttack extends PawnAttackMove {
    /**
     * Creates an EnPassantAttack object.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     * @param attackedPiece       The piece the pawn wants to capture.
     */
    public EnPassantAttack(final Board board,
                           final Piece movedPiece,
                           final Position destinationPosition,
                           final Piece attackedPiece) {
        super(board, movedPiece, destinationPosition, attackedPiece);
    }

    /**
     * Returns a new board based off an en passant attack.
     *
     * @return A new board based off an en passant attack.
     */
    @Override
    public Board execute() {
        final Builder builder= new Builder();

        setNonMovingEnPassantPieces(builder, this.board, this.movedPiece, this.getAttackedPiece());

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    /**
     * Returns whether the move and the other move are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the move and the other move are referentially and objectively equal.
     */
    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof EnPassantAttack && super.equals(other);
    }

    /**
     * Returns the String representation of an en passant attack.
     *
     * @return The String representation of an en passant attack.
     */
    @Override
    public String toString() {
        final int capturedPieceIndex = getPositionIndex(this.getAttackedPiece().getPiecePosition().rank(),
                this.getAttackedPiece().getPiecePosition().file());
        return getPositionAtIndex(this.getCurrentIndex()).charAt(0) +
               "x" +
               getPositionAtIndex(capturedPieceIndex) +
               " e.p.";
    }
}
