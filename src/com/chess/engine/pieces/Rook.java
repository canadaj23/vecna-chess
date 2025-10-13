package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.utils.PieceConstants.ROOK_FILE_OFFSETS;
import static com.chess.engine.utils.PieceConstants.ROOK_RANK_OFFSETS;
import static com.chess.engine.utils.PiecePositions.NFM_BR_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WR_POSITIONS_CACHE;
import static com.chess.engine.utils.PieceUtils.calculateSlidingLegalMoves;

/**
 * Represents the rook piece in chess. The rook can move horizontally or vertically any number of squares. This class
 * extends the Piece class, as well as implements many of its methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Rook extends Piece {

    /**
     * Creates a rook with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the rook is on the chessboard.
     */
    public Rook(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.ROOK, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a rook with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the rook is on the chessboard.
     * @param firstMove     Whether it is the rook's first move.
     */
    public Rook(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.ROOK, pieceAlliance, piecePosition, firstMove);
    }

    /**
     * Returns a list of all the rook's legal moves on a given chessboard.
     *
     * @param board What the rook traverses on.
     * @return A list of all the rook's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();

        calculateSlidingLegalMoves(sourceRank,
                                   ROOK_RANK_OFFSETS,
                                   sourceFile,
                                   ROOK_FILE_OFFSETS,
                                   board,
                                   this.pieceAlliance,
                                   this,
                                   legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Returns the "same" rook with an updated position.
     *
     * @param move What changes the rook's position.
     * @return The "same" rook with an updated position.
     */
    @Override
    public Rook movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (Rook) NFM_BR_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (Rook) NFM_WR_POSITIONS_CACHE.get(move.getDestinationIndex());
    }

    /**
     * Returns the String representation of the rook.
     *
     * @return The String representation of the rook.
     */
    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
}
