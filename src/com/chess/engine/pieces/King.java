package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.utils.PieceConstants.QUEEN_KING_RANK_OFFSETS;
import static com.chess.engine.utils.PieceConstants.QUEEN_KING_FILE_OFFSETS;
import static com.chess.engine.utils.PiecePositions.NFM_BK_POSITIONS_CACHE;
import static com.chess.engine.utils.PiecePositions.NFM_WK_POSITIONS_CACHE;
import static com.chess.engine.utils.PieceUtils.*;

/**
 * Represents the king piece in chess. The king can move one square in any direction. This class extends the Piece
 * class, as well as implements many of its methods.
 *
 * @author Jamie Candaa
 * @since 10/07/25
 */
public class King extends Piece {

    /**
     * Creates a king with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the king is on the chessboard.
     */
    public King(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.KING, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a king with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the king is on the chessboard.
     * @param firstMove     Whether it is the king's first move.
     */
    public King(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.KING, pieceAlliance, piecePosition, firstMove);
    }

    /**
     * Returns a list of all the king's legal moves on a given chessboard.
     *
     * @param board What the king traverses on.
     * @return A list of all the king's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();
        calculateNonSlidingLegalMoves(sourceRank,
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
     * Returns the "same" king with an updated position.
     *
     * @param move What changes the king's position.
     * @return The "same" king with an updated position.
     */
    @Override
    public King movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (King) NFM_BK_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (King) NFM_WK_POSITIONS_CACHE.get(move.getDestinationIndex());
    }

    /**
     * Returns the String representation of the king.
     *
     * @return The String representation of the king.
     */
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
