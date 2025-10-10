package com.chess.engine.moves.castle;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * Represents a queenside castle move. Queenside castling involves the king and the left-side rook and has some
 * conditions in order to be performed. This class extends the CastleMove class, and may implement some of
 * its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class QueensideCastleMove extends CastleMove {
    /**
     * Creates a QueenCastleMove object affiliated with the chessboard that contains the pieces involved in queenside
     * castling.
     *
     * @param board                   What the castle takes place on.
     * @param movedKing               One of the pieces involved in castling.
     * @param kingDestinationPosition Where the king wants to get placed at.
     * @param castleRook              The other piece involved in castling.
     * @param rookDestinationPosition The destination square's position for the castling rook.
     */
    public QueensideCastleMove(final Board board,
                               final Piece movedKing,
                               final Position kingDestinationPosition,
                               final Rook castleRook,
                               final Position rookDestinationPosition) {
        super(board, movedKing, kingDestinationPosition, castleRook, rookDestinationPosition);
    }

    /**
     * Returns the String representation of a queenside castle.
     *
     * @return The String representation of a queenside castle.
     */
    @Override
    public String toString() {
        return "O-O-O";
    }
}
