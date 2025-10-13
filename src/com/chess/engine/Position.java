package com.chess.engine;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.utils.BoardUtils.*;

/**
 * Represents the position of a chess piece or square on a chessboard.
 *
 * @param rank The row (y-position) of the piece or square that ranges between 0 and 7 inclusive.
 * @param file The column (x-position) of the piece or square that ranges between 0 and 7 inclusive.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public record Position(int rank, int file) {
    public static final Map<Integer, Position> ALL_BOARD_POSITIONS_CACHE = createAllBoardPositions();

    /**
     * Returns a list of all possible positions on the chessboard based on a rank and file.
     *
     * @return A list of all possible positions on the chessboard based on a rank and file.
     */
    private static Map<Integer, Position> createAllBoardPositions() {
        final Map<Integer, Position> positionsMap = new HashMap<>();

        for (int i = 0; i < BOARD_NUM_SQUARES; i++) {
            final int rank = i / RANK_NUM_SQUARES, file = i % FILE_NUM_SQUARES;
            final Position squarePosition = new Position(rank, file);
            final int squareIndex = getPositionIndex(rank, file);
            positionsMap.put(squareIndex, squarePosition);
        }

        return ImmutableMap.copyOf(positionsMap);
    }

    /**
     * Returns the index of a given position's rank and file of a square or piece on a chessboard.
     * The index will be between 0 (inclusive) and 63 (inclusive).
     *
     * @param rank The y-position of a square or piece on a chessboard.
     * @param file The x-position of a square or piece on a chessboard.
     * @return An index of a square or piece based off its rank and file.
     */
    public static int getPositionIndex(final int rank, final int file) {
        return (rank * RANK_NUM_SQUARES) + file;
    }

    /**
     * Returns whether the position and the other position are referentially and objectively equal.
     *
     * @param other The other object in question.
     * @return Whether the position and the other position are referentially and objectively equal.
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position otherPosition)) {
            return false;
        }

        return rank() == otherPosition.rank() && file() == otherPosition.file();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + rank;
        result = 31 * result + file;

        return result;
    }

    @Override
    public String toString() {
        return "(" + this.rank + ", " + this.file + ")";
    }
}
