package com.chess.engine.moves.misc;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

/**
 * Represents a null move. A null move is a move that does not exist on the chessboard. This class extends the Move
 * class, and may implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class NullMove extends Move {
    /**
     * Creates a NullMove object affiliated with the chessboard.
     */
    public NullMove() {
        super(null, null, null);
    }

    /**
     * Throws a runtime exception because a non-existing move should not be able to be performed.
     */
    @Override
    public Board execute() {
        throw new RuntimeException("A null move cannot be performed.");
    }
}
