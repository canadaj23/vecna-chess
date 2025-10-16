package com.chess.engine.player.ai.movestrategy;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.boardevaluator.BoardEvaluator;
import com.chess.engine.player.ai.boardevaluator.StandardBoardEvaluator;

/**
 * Represents the minimax move strategy. The white player wants to increase their score the most (approaching +inf)
 * while limiting the black player's score from decreasing much. The black player wants to decrease their score the
 * most (approaching -inf) while limiting the white player's score from increasing much. Minimax can be restrained by
 * using a depth, which limits how many plies the player will be searching on. The more plies the player has to search,
 * the longer it takes to search for the best move. Alpha-beta pruning is a technique that can be used to significantly
 * decrease the amount of moves to look at.
 *
 * @author Jamie Canada
 * @since 10/16/25
 */
public class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    /**
     * Creates a MiniMax object with an initialized board evaluator.
     */
    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    /**
     * Returns a move based on the minimax algorithm.
     *
     * @param board What the move will take place on.
     * @return A move based on the minimax algorithm.
     */
    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE, lowestSeenValue = Integer.MAX_VALUE, currentValue;

        System.out.printf("%s is thinking at a depth of %d ...\n",
                          board.getCurrentPlayer().toString(),
                          this.searchDepth);

        final Player currentPlayer = board.getCurrentPlayer();
        int numMoves = currentPlayer.getLegalMoves().size();

        for (final Move move : currentPlayer.getLegalMoves()) {
            final MoveTransition moveTransition = currentPlayer.makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = currentPlayer.getAlliance().isWhite() ?
                                                                       min(moveTransition.getTransitionBoard(),
                                                                           this.searchDepth - 1) :
                                                                       max(moveTransition.getTransitionBoard(),
                                                                           this.searchDepth - 1);
                if (currentPlayer.getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (currentPlayer.getAlliance().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    /**
     * Returns an as minimized score as possible based on the current player's legal moves per ply.
     *
     * @param board What the moves take place on.
     * @param depth How many plies of moves to look at.
     * @return An as minimized score as possible based on the current player's legal moves per ply.
     */
    public int min(final Board board, final int depth) {
        if (depth == 0 || isEndGame(board)) {
            return this.boardEvaluator.evaluateMove(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }

        return lowestSeenValue;
    }

    /**
     * Returns an as maximized score as possible based on the current player's legal moves per ply.
     *
     * @param board What the moves take place on.
     * @param depth How many plies of moves to look at.
     * @return An as maximized score as possible based on the current player's legal moves per ply.
     */
    public int max(final Board board, final int depth) {
        if (depth == 0 || isEndGame(board)) {
            return this.boardEvaluator.evaluateMove(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }

        return highestSeenValue;
    }

    /**
     * Determines whether the game is over.
     *
     * @param board What the moves take place on.
     * @return Whether the game is over.
     */
    private static boolean isEndGame(final Board board) {
        return board.getCurrentPlayer().isInCheckmate() ||
               board.getCurrentPlayer().isInStalemate();
    }

    /**
     * Returns the String representation of the minimax move strategy.
     *
     * @return The String representation of the minimax move strategy.
     */
    @Override
    public String toString() {
        return "Minimax";
    }
}
