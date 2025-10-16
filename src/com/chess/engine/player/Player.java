package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveTransition;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.Collection;

import static com.chess.engine.moves.MoveStatus.*;
import static com.chess.engine.utils.PlayerUtils.*;

/**
 * Represents one of the participants in the game of chess. The player will perform moves on pieces based on their
 * alliance and can capture the opponent's pieces when permitted.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean inCheck;

    public static final int[] KINGSIDE_CASTLE_FILES = { 5, 6, 7 }, QUEENSIDE_CASTLE_FILES = { 1, 2, 3, 0 };
    public static final int WHITE_CASTLE_RANK = 7, BLACK_CASTLE_RANK = 0;

    /**
     * Creates a Player object for a given board with its moves and the opponent's moves.
     *
     * @param board         What the player moves pieces on.
     * @param legalMoves    The player's moves.
     * @param opponentMoves The opponent's moves.
     */
    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        playerKing = validateKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,
                                                                calculatePlayerCastles(legalMoves, opponentMoves)));
        this.inCheck = !calculateAttacksOnKing(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    /**
     * Returns a list of all the player's active pieces.
     *
     * @return A list of all the player's active pieces.
     */
    public abstract Collection<Piece> getActivePieces();

    /**
     * Returns the player's alliance (WHITE or BLACK).
     *
     * @return The player's alliance (WHITE or BLACK).
     */
    public abstract Alliance getAlliance();

    /**
     * Returns the player's opponent.
     *
     * @return The player's opponent.
     */
    public abstract Player getOpponent();

    /**
     * Returns a Collection of all the player's available castles.
     *
     * @param playerLegals   The player's legal moves.
     * @param opponentLegals The opponent's legal moves.
     * @return A Collection of all the player's available castles.
     */
    protected abstract Collection<Move> calculatePlayerCastles(final Collection<Move> playerLegals,
                                                               final Collection<Move> opponentLegals);

    /**
     * Returns a king for the player to use.
     *
     * @return A king for the player to use.
     */
    private King validateKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }

        throw new RuntimeException("Chess cannot be played without a king!");
    }

    /**
     * Returns whether the move in the list is legal.
     *
     * @param move The move to be determined legal.
     * @return Whether the move in the list is legal.
     */
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    /**
     * Returns the player's king.
     *
     * @return The player's king.
     */
    public King getPlayerKing() {
        return this.playerKing;
    }

    /**
     * Returns the player's legal moves.
     *
     * @return The player's legal moves.
     */
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    /**
     * Returns whether the player's king is in check.
     *
     * @return Whether the player's king is in check.
     */
    public boolean isInCheck() {
        return this.inCheck;
    }

    /**
     * Returns whether the player's king is in checkmate.
     *
     * @return Whether the player's king is in checkmate.
     */
    public boolean isInCheckmate() {
        return this.inCheck && !hasEscapeMoves();
    }

    /**
     * Returns whether the player's king is in stalemate.
     *
     * @return Whether the player's king is in stalemate.
     */
    public boolean isInStalemate() {
        return !this.inCheck && !hasEscapeMoves();
    }

    /**
     * Returns whether the player's king can escape from being in check.
     *
     * @return Whether the player's king can escape from being in check.
     */
    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether the player's king performed a castle with a rook.
     *
     * @return Whether the player's king performed a castle with a rook.
     */
    public boolean isCastled() {
        return false;
    }

    /**
     * Returns the transition (or original) board based on the given move.
     *
     * @param move What the player does with a piece.
     * @return The transition based on the given move.
     */
    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();
        final Player currentPlayer = transitionBoard.getCurrentPlayer();
        final King opponentKing = currentPlayer.getOpponent().getPlayerKing();
        final Collection<Move> attacksOnKing = calculateAttacksOnKing(opponentKing.getPiecePosition(),
                                                                      currentPlayer.getLegalMoves());
        if (!attacksOnKing.isEmpty()) {
            return new MoveTransition(this.board, move, IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, DONE);
    }
}
