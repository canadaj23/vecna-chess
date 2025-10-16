package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static com.chess.engine.utils.PlayerUtils.*;

/**
 * Represents the white or light player. The white player initially moves first then alternates with the black player.
 * This class extends the Player class and will implement many of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class WhitePlayer extends Player {

    /**
     * Creates a WhitePlayer object for a given board with its moves and the opponent's moves.
     *
     * @param board           What the white player moves pieces on.
     * @param whiteLegalMoves The player's moves.
     * @param blackLegalMoves The opponent's moves.
     */
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    /**
     * Returns a list of all the white player's active pieces.
     *
     * @return A list of all the white player's active pieces.
     */
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getPlayerPieces(WHITE);
    }

    /**
     * Returns the player's alliance (WHITE or BLACK).
     *
     * @return The player's alliance (WHITE or BLACK).
     */
    @Override
    public Alliance getAlliance() {
        return WHITE;
    }

    /**
     * Returns the player's opponent's alliance (WHITE or BLACK).
     *
     * @return The player's opponent's alliance (WHITE or BLACK).
     */
    @Override
    public Player getOpponent() {
        return this.board.getPlayer(BLACK);
    }

    /**
     * Returns a list of all the white player's available castles.
     *
     * @param playerLegals   The white player's legal moves.
     * @param opponentLegals The black player's legal moves.
     * @return A list of all the white player's available castles.
     */
    @Override
    protected Collection<Move> calculatePlayerCastles(final Collection<Move> playerLegals,
                                                      final Collection<Move> opponentLegals) {
        return calculateCastles(this.playerKing,
                                this,
                                this.board,
                                KINGSIDE_CASTLE_FILES,
                                QUEENSIDE_CASTLE_FILES,
                                opponentLegals);
    }

    /**
     * Returns a String representation of the white player.
     *
     * @return A String representation of the white player.
     */
    @Override
    public String toString() {
        return "White";
    }
}
