package com.chess.engine.moves.castle;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * Represents a kingside castle move. Kingside castling involves the king and the right-side rook and has some
 * conditions in order to be performed. This class extends the CastleMove class, and may implement some of
 * its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class KingsideCastleMove extends CastleMove {
    /**
     * Creates a CastleMove object affiliated with the chessboard that contains the pieces involved in kingside
     * castling.
     *
     * @param board                   What the castle takes place on.
     * @param movedKing               One of the pieces involved in castling.
     * @param kingDestinationPosition Where the king wants to get placed at.
     * @param castleRook              The other piece involved in castling.
     * @param rookDestinationPosition The destination square's position for the castling rook.
     */
    public KingsideCastleMove(final Board board,
                              final Piece movedKing,
                              final Position kingDestinationPosition,
                              final Rook castleRook,
                              final Position rookDestinationPosition) {
        super(board, movedKing, kingDestinationPosition, castleRook, rookDestinationPosition);
    }

    /**
     * Returns the String representation of a kingside castle.
     *
     * @return The String representation of a kingside castle.
     */
    @Override
    public String toString() {
        return "O-O";
    }
}
