package com.chess.engine.moves.misc;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

/**
 * Represents a typical move performed in chess: moving to another square. This class extends the Move class, and may
 * implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/08/25
 */
public class MajorMove extends Move {
    /**
     * Creates a MajorMove object in which piece performs a move.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The piece performing the move.
     * @param destinationPosition Where the piece wants to get placed at.
     */
    public MajorMove(final Board board, final Piece movedPiece, final Position destinationPosition) {
        super(board, movedPiece, destinationPosition);
    }
}
