package com.chess.engine.moves.pawn.attack;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;

/**
 * Represents the pawn's en passant attack. When the opponent's pawn advances by two squares (to the bottom right square
 * of the player's pawn's diagonal), the player's pawn can capture this pawn by advancing to the square behind the
 * opponent's pawn. This class extends the PawnAttackMove class, and may implement some of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class EnPassantAttack extends PawnAttackMove {
    /**
     * Creates an EnPassantAttack object.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     * @param attackedPiece       The piece the pawn wants to capture.
     */
    public EnPassantAttack(final Board board,
                           final Piece movedPiece,
                           final Position destinationPosition,
                           final Piece attackedPiece) {
        super(board, movedPiece, destinationPosition, attackedPiece);
    }
}
