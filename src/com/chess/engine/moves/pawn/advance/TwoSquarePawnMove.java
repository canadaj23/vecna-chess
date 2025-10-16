package com.chess.engine.moves.pawn.advance;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.Builder;
import static com.chess.engine.utils.BoardUtils.getPositionAtIndex;
import static com.chess.engine.utils.BuilderUtils.setNonMovingPieces;

/**
 * Represents the move when the pawn advances by two squares. This class extends the Move class, and may implement some
 * of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class TwoSquarePawnMove extends Move {
    /**
     * Creates a TwoSquarePawnMove object in which the pawn advances by two squares.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     */
    public TwoSquarePawnMove(final Board board, final Piece movedPiece, final Position destinationPosition) {
        super(board, movedPiece, destinationPosition);
    }

    /**
     * Returns a new board based off the two-square advance.
     *
     * @return A new board based off the two-square advance.
     */
    @Override
    public Board execute() {
        final Builder builder = new Builder();

        setNonMovingPieces(builder, this.board, this.movedPiece);

        final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
        builder.setPiece(movedPawn);
        // Once the pawn advances by two squares, it is marked as an en passant pawn only for the next move
        builder.setEnPassantPawn(movedPawn);
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
        return this == other || other instanceof TwoSquarePawnMove && super.equals(other);
    }

    /**
     * Returns the String representation of the tow-square pawn advance.
     *
     * @return The String representation of the two-square pawn advance.
     */
    @Override
    public String toString() {
        return getPositionAtIndex(this.getDestinationIndex());
    }
}
