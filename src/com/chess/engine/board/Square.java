package com.chess.engine.board;

import com.chess.engine.Position;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.*;

/**
 * Represents a square on a chessboard. This abstract class extends for every version of a square on a chessboard.
 * The two subclasses in this file are EmptySquare and OccupiedSquare.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public abstract class Square {
    protected final Position squarePosition;

    private static final Map<Integer, EmptySquare> EMPTY_SQUARES_CACHE = createAllPossibleEmptySquares();

    /**
     * Creates a Square object with the given square position.
     *
     * @param squarePosition The square's position on the chessboard.
     */
    Square(final Position squarePosition) {
        this.squarePosition = squarePosition;
    }

    /**
     * Each possible position on a chessboard will be mapped to an empty square.
     *
     * @return A map containing a position mapped to an empty square.
     */
    private static Map<Integer, EmptySquare> createAllPossibleEmptySquares() {
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();

        for (int i = 0; i < BOARD_NUM_SQUARES; i++) {
            final int rank = i / RANK_NUM_SQUARES, file = i % FILE_NUM_SQUARES;
            final Position squarePosition = ALL_BOARD_POSITIONS_CACHE.get(getPositionIndex(rank, file));
            final int squareIndex = getPositionIndex(rank, file);
            emptySquareMap.put(squareIndex, new EmptySquare(squarePosition));
        }

        return ImmutableMap.copyOf(emptySquareMap);
    }

    /**
     * Returns a new occupied square if a piece is not null, or retrieves a cached empty square.
     *
     * @param squarePosition Where the square is on the chessboard.
     * @param piece          The possible chess piece on the chessboard.
     * @return A new occupied square if a piece is not null, or retrieves a cached empty square.
     */
    public static Square createSquare(final Position squarePosition, final Piece piece) {
        final int squareIndex = getPositionIndex(squarePosition.rank(), squarePosition.file());
        return piece != null ? new OccupiedSquare(squarePosition, piece) : EMPTY_SQUARES_CACHE.get(squareIndex);
    }

    /**
     * Determines if the square has a chess piece on it.
     *
     * @return Whether the square is occupied by a chess piece.
     */
    public boolean isSquareOccupied() {
        return false;
    }

    /**
     * Returns the piece on the square if applicable.
     *
     * @return The piece on the square if the piece exists.
     */
    public Piece getPiece() {
        return null;
    }

    /**
     * Returns the position of the square on the chessboard.
     *
     * @return The position of the square on the chessboard.
     */
    public Position getSquarePosition() {
        return this.squarePosition;
    }

    /**
     * Represents an empty square seen on a chessboard.
     *
     * @author Jamie Canada
     * @since 10/07/25
     */
    public static final class EmptySquare extends Square {

        /**
         * Creates an EmptySquare object with the given square position.
         *
         * @param squarePosition The empty square's position on the chessboard.
         */
        private EmptySquare(final Position squarePosition) {
            super(squarePosition);
        }

        /**
         * Returns a String representation of an empty square.
         *
         * @return The String representation of an empty square.
         */
        @Override
        public String toString() {
            return "-";
        }
    }

    /**
     * Represents an occupied square on a chessboard.
     *
     * @author Jamie Canada
     * @since 10/07/25
     */
    public static final class OccupiedSquare extends Square {
        private final Piece pieceOnSquare;

        /**
         * Creates an OccupiedSquare object with the given square position.
         *
         * @param squarePosition The occupied square's position on the chessboard.
         * @param pieceOnSquare  The piece that occupies the square.
         */
        private OccupiedSquare(final Position squarePosition, final Piece pieceOnSquare) {
            super(squarePosition);
            this.pieceOnSquare = pieceOnSquare;
        }

        /**
         * Returns true for having a piece on the occupied square.
         *
         * @return The occupied square has a piece on it.
         */
        @Override
        public boolean isSquareOccupied() {
            return true;
        }

        /**
         * Returns the piece on the occupied square.
         *
         * @return The piece on the occupied square.
         */
        @Override
        public Piece getPiece() {
            return this.pieceOnSquare;
        }

        /**
         * Returns a String representation of an occupied square.
         *
         * @return The String representation of an occupied square.
         */
        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase():
                                                                  getPiece().toString();
        }
    }
}
