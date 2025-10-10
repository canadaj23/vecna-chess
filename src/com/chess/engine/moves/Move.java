package com.chess.engine.moves;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.moves.misc.NullMove;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.*;

/**
 * Represents a move made in chess. Castling, pawn promotion, and attacking are all examples of a chess move.
 * This class extends for various chess moves.
 *
 * @author Jamie Canada
 * @since 10/08/25
 */
public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final Position destinationPosition;
    public static final Move NULL_MOVE = new NullMove();

    /**
     * Creates a Move object affiliated with the chessboard that contains a piece going to a destination position.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          What the move performs on.
     * @param destinationPosition Where the piece wants to get placed at.
     */
    protected Move(final Board board, final Piece movedPiece, final Position destinationPosition) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationPosition = destinationPosition;
    }

    /**
     * Returns the index of the piece's destination position during a move.
     *
     * @return The index of the piece's destination position during a move.
     */
    public int getDestinationIndex() {
        return getPositionIndex(this.destinationPosition.rank(), this.destinationPosition.file());
    }

    /**
     * Returns the destination position of a piece.
     *
     * @return The destination position of a piece.
     */
    public Position getDestinationPosition() {
        return this.destinationPosition;
    }

    /**
     * Returns the piece to be moved.
     *
     * @return The piece to be moved.
     */
    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    /**
     * Returns a new board based off the move performed.
     *
     * @return A new board based off the move performed.
     */
    public Board execute() {
        final Builder builder = new Builder();

        setNonMovingPieces(builder, this.board, this.movedPiece);

        // Place the moved piece and switch the move maker
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    /**
     * Returns the piece's current position.
     *
     * @return The piece's current position.
     */
    public Position getCurrentPosition() {
        return this.movedPiece.getPiecePosition();
    }

    /**
     * Returns the current index of the piece to be moved.
     *
     * @return The current index of the piece to be moved.
     */
    public int getCurrentIndex() {
        return getPositionIndex(this.movedPiece.getPiecePosition().rank(), this.movedPiece.getPiecePosition().file());
    }

    /**
     * Returns whether the move is an attack.
     *
     * @return Whether the move is an attack.
     */
    public boolean isAttack() {
        return false;
    }

    /**
     * Returns whether the move is a castle.
     *
     * @return Whether the move is a castle.
     */
    public boolean isCastle() {
        return false;
    }

    /**
     * Returns the piece currently under attack.
     *
     * @return The piece currently under attack.
     */
    public Piece getAttackedPiece() {
        return null;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move otherMove)) {
            return false;
        }

        return destinationPosition == otherMove.getDestinationPosition() &&
               movedPiece.equals(otherMove.getMovedPiece());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationPosition.rank();
        result = 31 * result + this.destinationPosition.file();
        result = 31 * result + this.movedPiece.hashCode();

        return result;
    }
}
