package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public static final int NUM_SQUARES_PER_FILE = 8;
    public static final int NUM_SQUARES_PER_RANK = 8;
    public static final int NUM_SQUARES_PER_BOARD = NUM_SQUARES_PER_FILE * NUM_SQUARES_PER_RANK;

    /**
     * Returns whether the rank and file are on the chessboard.
     *
     * @param rank The y-coordinate of the piece's position.
     * @param file The x-coordinate of the piece's position.
     * @return Whether the rank and file are on the chessboard.
     */
    public static boolean isValidPosition(final int rank, final int file) {
        return rank >= 0 && rank < NUM_SQUARES_PER_RANK && file >= 0 && file < NUM_SQUARES_PER_FILE;
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
        for (int i = 0; i < NUM_SQUARES_PER_RANK; i++) {
            final int pawnPositionIndex = alliance.isBlack() ? getPositionIndex(1, i) :
                                                               getPositionIndex(6, i);
            builder.setPiece(alliance.isBlack() ? ALL_BP_POSITIONS_CACHE.get(pawnPositionIndex) :
                                                  ALL_WP_POSITIONS_CACHE.get(pawnPositionIndex));
        }

        // Other pieces
        builder.setPiece(alliance.isBlack() ? ALL_BR_POSITIONS_CACHE.get(getPositionIndex(0, 0)) :
                                              ALL_WR_POSITIONS_CACHE.get(getPositionIndex(7, 0)));

        builder.setPiece(alliance.isBlack() ? ALL_BN_POSITIONS_CACHE.get(getPositionIndex(0, 1)) :
                                              ALL_WN_POSITIONS_CACHE.get(getPositionIndex(7, 1)));

        builder.setPiece(alliance.isBlack() ? ALL_BB_POSITIONS_CACHE.get(getPositionIndex(0, 2)) :
                                              ALL_WB_POSITIONS_CACHE.get(getPositionIndex(7, 2)));

        builder.setPiece(alliance.isBlack() ? ALL_BQ_POSITIONS_CACHE.get(getPositionIndex(0, 3)) :
                                              ALL_WQ_POSITIONS_CACHE.get(getPositionIndex(7, 3)));

        builder.setPiece(alliance.isBlack() ? ALL_BK_POSITIONS_CACHE.get(getPositionIndex(0, 4)) :
                                              ALL_WK_POSITIONS_CACHE.get(getPositionIndex(7, 4)));

        builder.setPiece(alliance.isBlack() ? ALL_BB_POSITIONS_CACHE.get(getPositionIndex(0, 5)) :
                                              ALL_WB_POSITIONS_CACHE.get(getPositionIndex(7, 5)));

        builder.setPiece(alliance.isBlack() ? ALL_BN_POSITIONS_CACHE.get(getPositionIndex(0, 6)) :
                                              ALL_WN_POSITIONS_CACHE.get(getPositionIndex(7, 6)));

        builder.setPiece(alliance.isBlack() ? ALL_BR_POSITIONS_CACHE.get(getPositionIndex(0, 7)) :
                                              ALL_WR_POSITIONS_CACHE.get(getPositionIndex(7, 7)));
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

    /**
     * Sets the player's non-moving pieces on the chessboard, and also sets all the opponent's pieces on the chessboard.
     *
     * @param builder    What sets the pieces on the chessboard.
     * @param board      What the pieces will be placed on.
     * @param movedPiece The piece that is going to move.
     */
    public static void setNonMovingPieces(final Builder builder, final Board board, final Piece movedPiece) {
        // Set all the player's non-moving pieces in their current positions
        for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        // Set all the opponent's pieces
        for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
    }

    /**
     * Sets the player's non-moving castle pieces on the chessboard, and also sets all the opponent's pieces on the
     * chessboard.
     *
     * @param builder    What sets the pieces on the chessboard.
     * @param board      What the pieces will be placed on.
     * @param movedPiece The piece that is going to move.
     * @param castleRook The rook that is part of the castle.
     */
    public static void setNonMovingCastlePieces(final Builder builder,
                                                final Board board,
                                                final Piece movedPiece,
                                                final Piece castleRook) {
        // Set all the player's non-moving pieces in their current positions
        for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!movedPiece.equals(piece) && !castleRook.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        // Set all the opponent's pieces
        for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
    }
}
