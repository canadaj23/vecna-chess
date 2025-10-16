package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.utils.PieceConstants.QUEEN_KING_FILE_OFFSETS;
import static com.chess.engine.utils.PieceConstants.QUEEN_KING_RANK_OFFSETS;
import static com.chess.engine.utils.PiecePositions.NFM_BQ_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WQ_POSITIONS_CACHE;
import static com.chess.engine.utils.PieceUtils.calculateSlidingLegalMoves;

/**
 * Represents the queen piece in chess. The queen can move diagonally, horizontally, or vertically any number of
 * squares. This class extends the Piece class, as well as implements many of its methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Queen extends Piece {

    /**
     * Creates a queen with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the queen is on the chessboard.
     */
    public Queen(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.QUEEN, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a queen with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the queen is on the chessboard.
     * @param firstMove     Whether it is the queen's first move.
     */
    public Queen(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.QUEEN, pieceAlliance, piecePosition, firstMove);
    }

    /**
     * Returns a list of all the queen's legal moves on a given chessboard.
     *
     * @param board What the queen traverses on.
     * @return A list of all the queen's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();

        calculateSlidingLegalMoves(sourceRank,
                                   QUEEN_KING_RANK_OFFSETS,
                                   sourceFile,
                                   QUEEN_KING_FILE_OFFSETS,
                                   board,
                                   this.pieceAlliance,
                                   this,
                                   legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Returns the "same" queen with an updated position.
     *
     * @param move What changes the queen's position.
     * @return The "same" queen with an updated position.
     */
    @Override
    public Queen movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (Queen) NFM_BQ_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (Queen) NFM_WQ_POSITIONS_CACHE.get(move.getDestinationIndex());
    }

    /**
     * Returns the String representation of the queen.
     *
     * @return The String representation of the queen.
     */
    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
}
