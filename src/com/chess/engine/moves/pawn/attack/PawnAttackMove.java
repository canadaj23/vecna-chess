package com.chess.engine.moves.pawn.attack;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.misc.AttackMove;
import com.chess.engine.pieces.Piece;

/**
 * Represents the pawn's attacks. This class extends the AttackMove class, and may implement some of
 * its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PawnAttackMove extends AttackMove {
    /**
     * Creates a PawnAttackMove object based on a normal attack or an en passant.
     *
     * @param board               What the move takes place on.
     * @param movedPiece          The pawn performing the move.
     * @param destinationPosition Where the pawn wants to get placed at.
     * @param attackedPiece       The piece the pawn wants to capture.
     */
    public PawnAttackMove(final Board board,
                          final Piece movedPiece,
                          final Position destinationPosition,
                          final Piece attackedPiece) {
        super(board, movedPiece, destinationPosition, attackedPiece);
    }
}
