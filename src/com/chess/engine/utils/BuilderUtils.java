package com.chess.engine.utils;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;

/**
 * Holds builder-related methods.
 *
 * @author Jamie Canada
 * @since 10/13/25
 */
public class BuilderUtils {
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

    /**
     * Sets the player's non-moving castle pieces on the chessboard, and also sets all the opponent's pieces on the
     * chessboard.
     *
     * @param builder       What sets the pieces on the chessboard.
     * @param board         What the pieces will be placed on.
     * @param movedPiece    The piece that is going to move.
     * @param attackedPiece The piece to be captured en passant.
     */
    public static void setNonMovingEnPassantPieces(final Builder builder,
                                                   final Board board,
                                                   final Piece movedPiece,
                                                   final Piece attackedPiece) {
        // Set all the player's non-moving pieces in their current positions
        for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
            if (!movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        // Set all the opponent's non-moving pieces
        for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
            if (!piece.equals(attackedPiece)) {
                builder.setPiece(piece);
            }
        }
    }
}
