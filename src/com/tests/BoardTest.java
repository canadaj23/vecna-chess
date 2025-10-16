package com.tests;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveFactory;
import com.chess.engine.moves.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.movestrategy.MiniMax;
import com.chess.engine.player.ai.movestrategy.MoveStrategy;
import com.chess.engine.utils.BoardUtils;
import org.junit.jupiter.api.Test;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static org.junit.jupiter.api.Assertions.*;
class BoardTest {
    @Test
    public void initialBoard() {

        final Board board = Board.createInitialBoard();
        assertEquals(20, board.getCurrentPlayer().getLegalMoves().size());
        assertEquals(20, board.getCurrentPlayer().getOpponent().getLegalMoves().size());
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckmate());
        assertFalse(board.getCurrentPlayer().isCastled());
//        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getPlayer(Alliance.WHITE));
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getPlayer(Alliance.BLACK));
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckmate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
//        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.getPlayer(Alliance.WHITE).toString());
        assertEquals("Black", board.getPlayer(Alliance.BLACK).toString());
    }

    @Test
    public void testFoolsMate() {
        final Board board = Board.createInitialBoard();

        final MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory.
                                                                    findMove(board,
                                                                    ALL_BOARD_POSITIONS_CACHE.
                                                                    get(BoardUtils.getIndexAtPosition("f2")),
                                                                    ALL_BOARD_POSITIONS_CACHE.
                                                                    get(BoardUtils.getIndexAtPosition("f3"))));
        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard().
                                     getCurrentPlayer().makeMove(MoveFactory.
                                                                 findMove(t1.getTransitionBoard(),
                                                                          ALL_BOARD_POSITIONS_CACHE.
                                                                          get(BoardUtils.getIndexAtPosition("e7")),
                                                                          ALL_BOARD_POSITIONS_CACHE.
                                                                          get(BoardUtils.getIndexAtPosition("e5"))));
        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard().
                                     getCurrentPlayer().
                                     makeMove(MoveFactory.
                                              findMove(t2.getTransitionBoard(),
                                                       ALL_BOARD_POSITIONS_CACHE.
                                                       get(BoardUtils.getIndexAtPosition("g2")),
                                                       ALL_BOARD_POSITIONS_CACHE.
                                                       get(BoardUtils.getIndexAtPosition("g4"))));
        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new MiniMax(4);
        final Move aiMove = strategy.execute(t3.getTransitionBoard());
        final Move bestMove = MoveFactory.findMove(t3.getTransitionBoard(),
                                                   ALL_BOARD_POSITIONS_CACHE.get(BoardUtils.getIndexAtPosition("d8")),
                                                   ALL_BOARD_POSITIONS_CACHE.get(BoardUtils.getIndexAtPosition("h4")));

        assertEquals(bestMove, aiMove);
    }
}