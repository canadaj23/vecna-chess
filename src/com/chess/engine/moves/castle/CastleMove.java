package com.chess.engine.moves.castle;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BuilderUtils.setNonMovingCastlePieces;
import static com.chess.engine.utils.PiecePositions.NFM_BR_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WR_POSITIONS_CACHE;

/**
 * Represents a castle move. Castling involves the king and one of the rooks and has some conditions in order to be
 * performed. This class extends the Move class, and may implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public abstract class CastleMove extends Move {
    protected final Rook castleRook;
    protected final Position rookDestinationPosition;

    /**
     * Creates a CastleMove object affiliated with the chessboard that contains the pieces involved in castling.
     *
     * @param board                   What the castle takes place on.
     * @param movedKing               One of the pieces involved in castling.
     * @param kingDestinationPosition Where the king wants to get placed at.
     * @param castleRook              The other piece involved in castling.
     * @param rookDestinationPosition The destination square's position for the castling rook.
     */
    protected CastleMove(final Board board,
                         final Piece movedKing,
                         final Position kingDestinationPosition,
                         final Rook castleRook,
                         final Position rookDestinationPosition) {
        super(board, movedKing, kingDestinationPosition);
        this.castleRook = castleRook;
        this.rookDestinationPosition = rookDestinationPosition;
    }

    /**
     * Returns the rook involved in the castle.
     *
     * @return The rook involved in the castle.
     */
    public Rook getCastleRook() {
        return this.castleRook;
    }

    /**
     * Returns whether the move is a castle.
     *
     * @return Whether the move is a castle.
     */
    @Override
    public boolean isCastle() {
        return true;
    }

    /**
     * Returns a new board based off the castle.
     *
     * @return A new board based off the castle.
     */
    @Override
    public Board execute() {
        final Builder builder = new Builder();

        setNonMovingCastlePieces(builder, this.board, this.movedPiece, this.castleRook);

        builder.setPiece(this.movedPiece.movePiece(this));
        // TODO: look into the first move on normal pieces
        final int rookDestinationIndex = getPositionIndex(rookDestinationPosition.rank(),
                                                          rookDestinationPosition.file());
        builder.setPiece(this.movedPiece.getPieceAlliance().isBlack() ?
                         NFM_BR_POSITIONS_CACHE.get(rookDestinationIndex) :
                         NFM_WR_POSITIONS_CACHE.get(rookDestinationIndex));
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
        if (this == other) {
            return true;
        }
        if (!(other instanceof CastleMove otherCastleMove)) {
            return false;
        }

        return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
    }

    /**
     * Returns a hashcode for a castle move.
     *
     * @return A hashcode for a castle move.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.castleRook.hashCode();
        result = 31 * result + this.castleRook.getPiecePosition().rank();
        result = 31 * result + this.castleRook.getPiecePosition().file();

        return result;
    }
}
