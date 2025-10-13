package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.board.Board.*;
import static com.chess.engine.utils.PiecePositions.*;

/**
 * Holds all the chessboard-relevant constants and methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class BoardUtils {
    public static final int FILE_NUM_SQUARES = 8;
    public static final int RANK_NUM_SQUARES = 8;
    public static final int BOARD_NUM_SQUARES = FILE_NUM_SQUARES * RANK_NUM_SQUARES;

    public static final String[] ALGEBRAIC_NOTATIONS = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITIONS_TO_INDICES = initializePositionToIndexMap();

    /**
     * Creates a String array that holds all possible algebraic notations.
     *
     * @return A String array that holds all possible algebraic notations.
     */
    private static String[] initializeAlgebraicNotation() {
        final String[] algebraicNotations = new String[BOARD_NUM_SQUARES];
        final int[] files = { 8, 7, 6, 5, 4, 3, 2, 1 };
        final String[] ranks = { "a", "b", "c", "d", "e", "f", "g", "h" };

        int index = 0;
        for (final int file : files) {
            for (final String rank : ranks) {
                algebraicNotations[index] = rank + file;
                index++;
            }
        }

        return algebraicNotations;
    }

    /**
     * Returns a map of a String object (the position) mapped to an index.
     *
     * @return A map of a String object (the position) mapped to an index.
     */
    private static Map<String, Integer> initializePositionToIndexMap() {
        final Map<String, Integer> positionsToIndices = new HashMap<>();

        for (int i = 0; i < BOARD_NUM_SQUARES; i++) {
            positionsToIndices.put(ALGEBRAIC_NOTATIONS[i], i);
        }

        return ImmutableMap.copyOf(positionsToIndices);
    }

    /**
     * Returns the index associated with the position.
     *
     * @param position Where the piece or square is on the chessboard.
     * @return The index associated with the position.
     */
    public static int getIndexAtPosition(final String position) {
        return POSITIONS_TO_INDICES.get(position);
    }

    /**
     * Returns the position associated with the index.
     *
     * @param index What to use for position retrieval.
     * @return The position associated with the index.
     */
    public static String getPositionAtIndex(final int index) {
        return ALGEBRAIC_NOTATIONS[index];
    }

    /**
     * Returns whether the rank and file are on the chessboard.
     *
     * @param rank The y-coordinate of the piece's position.
     * @param file The x-coordinate of the piece's position.
     * @return Whether the rank and file are on the chessboard.
     */
    public static boolean isValidPosition(final int rank, final int file) {
        return rank >= 0 && rank < RANK_NUM_SQUARES && file >= 0 && file < FILE_NUM_SQUARES;
    }

    /**
     * Sets the pieces to their initial positions on the chessboard.
     *
     * @param alliance The alliance of the pieces.
     * @param builder  What will set the pieces on the chessboard.
     */
    public static void setInitialPieces(final Alliance alliance,
                                        final Builder builder) {
        // Pawns
        for (int i = 0; i < RANK_NUM_SQUARES; i++) {
            final int pawnPositionIndex = alliance.isBlack() ? getPositionIndex(1, i) :
                                                               getPositionIndex(6, i);
            builder.setPiece(alliance.isBlack() ? FM_BP_POSITIONS_CACHE.get(pawnPositionIndex) :
                                                  FM_WP_POSITIONS_CACHE.get(pawnPositionIndex));
        }

        // Other pieces
        builder.setPiece(alliance.isBlack() ? FM_BR_POSITIONS_CACHE.get(getPositionIndex(0, 0)) :
                                              FM_WR_POSITIONS_CACHE.get(getPositionIndex(7, 0)));

        builder.setPiece(alliance.isBlack() ? FM_BN_POSITIONS_CACHE.get(getPositionIndex(0, 1)) :
                                              FM_WN_POSITIONS_CACHE.get(getPositionIndex(7, 1)));

        builder.setPiece(alliance.isBlack() ? FM_BB_POSITIONS_CACHE.get(getPositionIndex(0, 2)) :
                                              FM_WB_POSITIONS_CACHE.get(getPositionIndex(7, 2)));

        builder.setPiece(alliance.isBlack() ? FM_BQ_POSITIONS_CACHE.get(getPositionIndex(0, 3)) :
                                              FM_WQ_POSITIONS_CACHE.get(getPositionIndex(7, 3)));

        builder.setPiece(alliance.isBlack() ? FM_BK_POSITIONS_CACHE.get(getPositionIndex(0, 4)) :
                                              FM_WK_POSITIONS_CACHE.get(getPositionIndex(7, 4)));

        builder.setPiece(alliance.isBlack() ? FM_BB_POSITIONS_CACHE.get(getPositionIndex(0, 5)) :
                                              FM_WB_POSITIONS_CACHE.get(getPositionIndex(7, 5)));

        builder.setPiece(alliance.isBlack() ? FM_BN_POSITIONS_CACHE.get(getPositionIndex(0, 6)) :
                                              FM_WN_POSITIONS_CACHE.get(getPositionIndex(7, 6)));

        builder.setPiece(alliance.isBlack() ? FM_BR_POSITIONS_CACHE.get(getPositionIndex(0, 7)) :
                                              FM_WR_POSITIONS_CACHE.get(getPositionIndex(7, 7)));
    }

    /**
     * Returns a Collection of an alliance's currently active pieces.
     *
     * @param gameboard What the pieces are on.
     * @param alliance  The piece's alliance.
     * @return A Collection of an alliance's currently active pieces.
     */
    public static Collection<Piece> calculateActivePieces(final List<Square> gameboard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();

        for (final Square square : gameboard) {
            if (square.isSquareOccupied()) {
                final Piece piece = square.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }

        return ImmutableList.copyOf(activePieces);
    }

    /**
     * Returns a Collection of an alliance's currently active pieces' legal moves.
     *
     * @param board  What the piece wants to make a move on.
     * @param pieces All the alliance's pieces.
     * @return A Collection of an alliance's currently active pieces' legal moves.
     */
    public static Collection<Move> calculateLegalMoves(final Board board, final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(board));
        }

        return ImmutableList.copyOf(legalMoves);
    }

}
