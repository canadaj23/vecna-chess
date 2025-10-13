package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.utils.PieceConstants.*;
import static com.chess.engine.utils.PiecePositions.NFM_BN_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WN_POSITIONS_CACHE;
import static com.chess.engine.utils.PieceUtils.calculateNonSlidingLegalMoves;

/**
 * Represents the knight piece in chess. The knight can move in an L-shape: two squares one direction then one square
 * left or right of it. This class extends the Piece class, as well as implements many of its methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Knight extends Piece {

    /**
     * Creates a knight with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the knight is on the chessboard.
     */
    public Knight(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a knight with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the knight is on the chessboard.
     * @param firstMove     Whether it is the knight's first move.
     */
    public Knight(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, firstMove);
    }

    /**
     * Returns a list of all the knight's legal moves on a given chessboard.
     *
     * @param board What the knight traverses on.
     * @return A list of all the knight's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();

        calculateNonSlidingLegalMoves(sourceRank,
                                      KNIGHT_RANK_OFFSETS,
                                      sourceFile,
                                      KNIGHT_FILE_OFFSETS,
                                      board,
                                      this.pieceAlliance,
                                      this,
                                      legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Returns the "same" knight with an updated position.
     *
     * @param move What changes the knight's position.
     * @return The "same" knight with an updated position.
     */
    @Override
    public Knight movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (Knight) NFM_BN_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (Knight) NFM_WN_POSITIONS_CACHE.get(move.getDestinationIndex());
    }

    /**
     * Returns the String representation of the knight.
     *
     * @return The String representation of the knight.
     */
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
}
