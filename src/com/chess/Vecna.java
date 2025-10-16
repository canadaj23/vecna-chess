package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.GameWindow;

/**
 * Serves as the driver of the program.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Vecna {
    public static void main(String[] args) {
        Board board = Board.createInitialBoard();
        System.out.println(board);

        GameWindow.get().show();
    }
}
