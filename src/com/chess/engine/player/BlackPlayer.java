package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.utils.PlayerUtils.*;

/**
 * Represents the black or dark player. The black player initially moves after the white player then alternates with
 * them. This class extends the Player class and will implement many of its methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class BlackPlayer extends Player {

    /**
     * Creates a BlackPlayer object for a given board with its moves and the opponent's moves.
     *
     * @param board           What the black player moves pieces on.
     * @param blackLegalMoves The player's moves.
     * @param whiteLegalMoves The opponent's moves.
     */
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    /**
     * Returns a list of all the black player's active pieces.
     *
     * @return A list of all the black player's active pieces.
     */
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getPlayerPieces(BLACK);
    }

    /**
     * Returns the player's alliance (WHITE or BLACK).
     *
     * @return The player's alliance (WHITE or BLACK).
     */
    @Override
    public Alliance getAlliance() {
        return BLACK;
    }

    /**
     * Returns the player's opponent's alliance (WHITE or BLACK).
     *
     * @return The player's opponent's alliance (WHITE or BLACK).
     */
    @Override
    public Player getOpponent() {
        return this.board.getPlayer(WHITE);
    }

    /**
     * Returns a list of all the black player's available castles.
     *
     * @param playerLegals   The black player's legal moves.
     * @param opponentLegals The white player's legal moves.
     * @return A list of all the black player's available castles.
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
     * Returns a String representation of the black player.
     *
     * @return A String representation of the black player.
     */
    @Override
    public String toString() {
        return "Black";
    }
}
