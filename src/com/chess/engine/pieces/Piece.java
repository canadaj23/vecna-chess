package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

import java.util.List;
import java.util.Objects;

/**
 * Represents a chess piece. This abstract class extends for every piece in the game of chess.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public abstract class Piece {
    protected Alliance pieceAlliance;
    protected final Position piecePosition;
    protected final boolean firstMove;
    private final PieceType pieceType;
    private final int cachedHashCode;

    /**
     * Creates a Piece object with a piece alliance and a piece position.
     *
     * @param pieceType     The piece's type.
     * @param pieceAlliance WHITE or BLACK
     * @param piecePosition Where the piece is on the chessboard.
     * @param firstMove     Whether it is the piece's first move.
     */
    Piece(final PieceType pieceType,
          final Alliance pieceAlliance,
          final Position piecePosition,
          final Boolean firstMove) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.firstMove = firstMove;
        this.cachedHashCode = computeHashCode();
    }

    /**
     * Returns a list of all the piece's legal moves on a given chessboard.
     *
     * @param board What the piece traverses on.
     * @return A list of all the piece's legal moves on a given chessboard.
     */
    public abstract List<Move> calculateLegalMoves(final Board board);

    /**
     * Returns the "same" piece with an updated position.
     *
     * @param move What changes the piece's position.
     * @return The "same" piece with an updated position.
     */
    public abstract Piece movePiece(final Move move);

    /**
     * Returns the piece's alliance.
     *
     * @return White or black.
     */
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    /**
     * Returns the piece's position.
     *
     * @return The piece's position.
     */
    public Position getPiecePosition() {
        return this.piecePosition;
    }

    /**
     * Returns whether it is the piece's first move.
     *
     * @return Whether it is the piece's first move.
     */
    public boolean isFirstMove() {
        return this.firstMove;
    }

    /**
     * Returns the piece's type.
     *
     * @return The piece's type.
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Returns a computed hash code for the piece.
     *
     * @return A computed hash code for the piece.
     */
    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition.rank();
        result = 31 * result + piecePosition.file();
        result = 31 * result + (firstMove ? 1 : 0);

        return result;
    }

    /**
     * Returns whether two objects are the same (including reference and object equality).
     *
     * @param other The other object to compare to.
     * @return Whether two objects are the same (including reference and object equality).
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Piece otherPiece)) {
            return false;
        }

        return piecePosition == otherPiece.getPiecePosition() &&
               pieceType == otherPiece.getPieceType() &&
               pieceAlliance == otherPiece.getPieceAlliance() &&
               firstMove == otherPiece.isFirstMove();
    }

    /**
     * Returns a unique hashcode for each piece on the chessboard.
     *
     * @return A unique hashcode for each piece on the chessboard.
     */
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    /**
     * Represents the piece's type in the game of chess.
     */
    public enum PieceType {
        PAWN(100, "P") {

        },
        KNIGHT(300, "N") {

        },
        BISHOP(300, "B") {

        },
        ROOK(500, "R") {
            /**
             * Returns whether the piece is a rook.
             *
             * @return Whether the piece is a rook.
             */
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900, "Q") {

        },
        KING(10_000, "K") {
            /**
             * Returns whether the piece is a king.
             *
             * @return Whether the piece is a king.
             */
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;
        private int pieceValue;

        /**
         * Creates the PieceType for a given piece based off the piece's name.
         *
         * @param pieceName The name of the piece based off the piece's name.
         */
        PieceType(final int pieceValue, final String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        /**
         * Returns whether the piece is a king.
         *
         * @return Whether the piece is a king.
         */
        public boolean isKing() {
            return false;
        }

        /**
         * Returns whether the piece is a rook.
         *
         * @return Whether the piece is a rook.
         */
        public boolean isRook() {
            return false;
        }

        /**
         * Returns the value of the piece.
         *
         * @return The value of the piece.
         */
        public int getPieceValue() {
            return this.pieceValue;
        }

        /**
         * Returns the String representation of the piece's type.
         *
         * @return The String representation of the piece's type.
         */
        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
