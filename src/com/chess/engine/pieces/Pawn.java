package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.PawnUtils.*;
import static com.chess.engine.utils.PiecePositions.*;

/**
 * Represents the pawn piece in chess. The pawn can move forward one or two squares on its first move, otherwise one
 * square forward. This class extends the Piece class, as well as implements many of its methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Pawn extends Piece {

    /**
     * Creates a pawn with a piece alliance, a piece position, and an initially true first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the pawn is on the chessboard.
     */
    public Pawn(final Alliance pieceAlliance, final Position piecePosition) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, true);
    }

    /**
     * Creates a pawn with a piece alliance, a piece position, and a parameter-determined first move.
     *
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the pawn is on the chessboard.
     * @param firstMove     Whether it is the pawn's first move.
     */
    public Pawn(final Alliance pieceAlliance, final Position piecePosition, final boolean firstMove) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, firstMove);
    }

    /**
     * Returns a list of all the pawn's legal moves on a given chessboard.
     *
     * @param board What the pawn traverses on.
     * @return A list of all the pawn's legal moves on a given chessboard.
     */
    @Override
    public List<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int sourceRank = this.piecePosition.rank(), sourceFile = this.piecePosition.file();

        calculatePawnLegalMoves(sourceRank, sourceFile, this.pieceAlliance, board, this, legalMoves);

        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Returns the "same" pawn with an updated position.
     *
     * @param move What changes the pawn's position.
     * @return The "same" pawn with an updated position.
     */
    @Override
    public Pawn movePiece(final Move move) {
        return this.pieceAlliance.isBlack() ? (Pawn) NFM_BP_POSITIONS_CACHE.get(move.getDestinationIndex()) :
                                              (Pawn) NFM_WP_POSITIONS_CACHE.get(move.getDestinationIndex());

    }

    /**
     * Returns the String representation of the pawn.
     *
     * @return The String representation of the pawn.
     */
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
