package com.chess.engine.moves.pawn.advance;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

/**
 * Represents the move when the pawn advances by one square. This class extends the Move class, and may implement some
 * of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class OneSquarePawnMove extends Move {
    /**
     * Creates a OneSquarePawnMove object in which the pawn advances by one square.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     */
    public OneSquarePawnMove(final Board board, final Piece movedPiece, final Position destinationPosition) {
        super(board, movedPiece, destinationPosition);
    }
}
