package com.chess.engine.player.ai.boardevaluator;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

/**
 * Represents the evaluation of a standard chessboard. A standard chessboard is a chessboard with no modifications to
 * the squares, as well as the chess pieces and their initial positions.
 */
public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_BONUS = 50, CHECKMATE_BONUS = 10000, DEPTH_BONUS = 10, CASTLE_BONUS = 60;

    /**
     * Returns the score of a move after it is evaluated from a standard board.
     *
     * @param board What the move takes place on.
     * @param depth How many plies of moves to look at.
     * @return The score of a move after it is evaluated from a standard board.
     */
    @Override
    public int evaluateMove(final Board board, final int depth) {
        return scorePlayer(board, board.getPlayer(Alliance.WHITE), depth) -
               scorePlayer(board, board.getPlayer(Alliance.BLACK), depth);
    }

    /**
     * Returns the score of a player based on the number of active pieces, whether the move puts the opponent in check,
     * whether the move puts the opponent in checkmate, whether the move is a castle, and how many legal moves the
     * player has.
     *
     * @param player Who is making the move.
     * @return The score of a player based on various circumstances.
     */
    private int scorePlayer(final Board board, final Player player, final int depth) {
        return scorePieces(player) +
               scoreMobility(player) +
               scoreCheck(player) +
               scoreCheckmate(player, depth) +
               scoreCastle(player);
    }

    /**
     * Returns a score based on the sum of all the player's active pieces' values.
     *
     * @param player Who is performing the move.
     * @return A score based on the sum of all the player's active pieces' values.
     */
    private int scorePieces(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceType().getPieceValue();
        }

        return pieceValueScore;
    }

    /**
     * Returns a score based on how many legal moves the player currently has.
     *
     * @param player Who is performing the move.
     * @return A score based on how many legal moves the player currently has.
     */
    private int scoreMobility(final Player player) {
        return player.getLegalMoves().size();
    }

    /**
     * Returns a score based on whether the opponent is in check.
     *
     * @param player Who is making the move.
     * @return A score based on whether the opponent is in check.
     */
    private int scoreCheck(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    /**
     * Returns a score based on the depth at which the opponent is in checkmate.
     *
     * @param player Who is performing the move.
     * @param depth  How many plies it takes to reach checkmate.
     * @return A score based on the depth at which the opponent is in checkmate.
     */
    private int scoreCheckmate(final Player player, final int depth) {
        return player.getOpponent().isInCheckmate() ? CHECKMATE_BONUS * scoreDepthBonus(depth) : 0;
    }

    /**
     * Returns a score based on the depth of the search. A bigger depth results in a bigger bonus.
     *
     * @param depth How many plies to be looked at.
     * @return A score based on the depth of the search. A bigger depth results in a bigger bonus.
     */
    private int scoreDepthBonus(final int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    /**
     * Returns a score based on whether the move is a castle.
     *
     * @param player Who is performing the move.
     * @return A score based on whether the move is a castle.
     */
    private int scoreCastle(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
}
