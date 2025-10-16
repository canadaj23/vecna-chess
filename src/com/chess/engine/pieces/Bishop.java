package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.utils.PieceConstants.BISHOP_FILE_OFFSETS;
import static com.chess.engine.utils.PieceConstants.BISHOP_RANK_OFFSETS;
import static com.chess.engine.utils.PiecePositions.NFM_BB_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WB_POSITIONS_CACHE;
import static com.chess.engine.utils.PieceUtils.calculateSlidingLegalMoves;

/**
 * Represents the bishop piece in chess. The bishop can move diagonally any number of squares. This class extends
 * the Piece class, as well as implements many of its methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Bishop extends Piece {

    /**
     * Creates a bishop with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the bishop is on the chessboard.
     */
    public Bishop(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.BISHOP, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a bishop with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the bishop is on the chessboard.
     * @param firstMove     Whether it is the bishop's first move.
     */
    public Bishop(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.BISHOP, pieceAlliance, piecePosition, firstMove);
    }


    /**
     * Returns a list of all the bishop's legal moves on a given chessboard.
     *
     * @param board What the bishop traverses on.
     * @return A list of all the bishop's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();

        calculateSlidingLegalMoves(sourceRank,
                                   BISHOP_RANK_OFFSETS,
                                   sourceFile,
                                   BISHOP_FILE_OFFSETS,
                                   board,
                                   this.pieceAlliance,
                                   this,
                                   legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Returns the "same" bishop with an updated position.
     *
     * @param move What changes the bishop's position.
     * @return The "same" bishop with an updated position.
     */
    @Override
    public Bishop movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (Bishop) NFM_BB_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (Bishop) NFM_WB_POSITIONS_CACHE.get(move.getDestinationIndex());
    }

    /**
     * Returns the String representation of the bishop.
     *
     * @return The String representation of the bishop.
     */
    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }
}
