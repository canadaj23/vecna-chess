package com.chess.engine.moves.pawn;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BuilderUtils.*;
import static com.chess.engine.utils.PiecePositions.FM_BQ_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.FM_WQ_POSITIONS_CACHE;

/**
 * Represents the promoting of a pawn after advancing to its last rank. The player can decide to promote the pawn to a
 * knight, bishop, rook, or queen. The pawn can be promoted by advancing normally to the last rank or by capturing a
 * piece on its last rank. This class extends Move and may implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/14/25
 */
public class PawnPromotion extends Move {
    final Move pawnMove;
    final Pawn promotedPawn;

    /**
     * Creates a PawnPromotion object by using a move parameter that follow the decorator pattern, utilizing the
     * decorated move's board, moved piece, and destination position attributes.
     *
     * @param pawnMove The move the pawn performed before the promotion.
     */
    public PawnPromotion(final Move pawnMove) {
        super(pawnMove.getBoard(), pawnMove.getMovedPiece(), pawnMove.getDestinationPosition());
        this.pawnMove = pawnMove;
        this.promotedPawn = (Pawn) pawnMove.getMovedPiece();
    }

    /**
     * Returns a new board based off the move performed.
     *
     * @return A new board based off the move performed.
     */
    @Override
    public Board execute() {
        final Board pawnMovedBoard = this.pawnMove.execute();
        final Builder builder = new Builder();

        setNonMovingPieces(builder, pawnMovedBoard, this.promotedPawn);

        final Position promotionPosition = this.promotedPawn.getPiecePosition();
        final int promotionIndex = getPositionIndex(promotionPosition.rank(), promotionPosition.file());
        final Queen promotedPiece = (Queen) (this.promotedPawn.getPieceAlliance().isBlack() ?
                                             FM_BQ_POSITIONS_CACHE.get(promotionIndex) :
                                             FM_WQ_POSITIONS_CACHE.get(promotionIndex));
        builder.setPiece(promotedPiece.movePiece(this));
        // getCurrentPlayer() because of pawnMovedBoard setting the move maker previously
        builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());

        return builder.build();
    }

    /**
     * Returns whether the move is an attack.
     *
     * @return Whether the move is an attack.
     */
    @Override
    public boolean isAttack() {
        return super.isAttack();
    }

    /**
     * Returns the piece currently under attack.
     *
     * @return The piece currently under attack.
     */
    @Override
    public Piece getAttackedPiece() {
        return super.getAttackedPiece();
    }

    /**
     * Returns whether the move and the other move are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the move and the other move are referentially and objectively equal.
     */
    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof PawnPromotion && super.equals(other);
    }

    /**
     * Returns a hashcode for an individual move.
     *
     * @return A hashcode for an individual move.
     */
    @Override
    public int hashCode() {
        return this.pawnMove.hashCode() + (31 * promotedPawn.hashCode());
    }

    /**
     * Returns the String representation of a pawn promotion.
     *
     * @return The String representation of a pawn promotion.
     */
    @Override
    public String toString() {
        return "";
    }
}
