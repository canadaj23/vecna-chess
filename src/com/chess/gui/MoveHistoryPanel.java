package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.moves.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.chess.gui.GameWindow.*;
import static com.chess.gui.utils.DimensionConstants.GAME_HISTORY_PANEL_DIMENSION;
import static com.chess.gui.utils.DimensionConstants.SCALE;

/**
 * Represents the game's history (i.e., all moves made). The panel will display the first move by the player and
 * opponent, followed by the next move for each, then so on until the game concludes. Each move made will be divided
 * into a row and column, where the row represents the move number and the column represents who made the move. The
 * entry represents the move made.
 *
 * @author Jamie Canada
 * @since 10/13/25
 */
public class MoveHistoryPanel extends JPanel {
    private final DataModel model;
    private final JScrollPane scrollPane;

    /**
     * Creates a MoveHistoryPanel object that displays all the moves made by the player and opponent.
     */
    MoveHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight((int) (15 * SCALE));
        table.setFont(new Font("SansSerif", Font.PLAIN, (int) (10 * SCALE)));
        this.scrollPane = new JScrollPane(table);
        final JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, (int) (12 * SCALE)));
        this.scrollPane.setColumnHeaderView(header);
        this.scrollPane.setPreferredSize(GAME_HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Displays the previous moves before the next move is made.
     *
     * @param board       What the move takes place on.
     * @param moveHistory What contains the move to be redone.
     */
    void redo(final Board board, final MoveLog moveHistory) {
        placeRemainingMoves(board, moveHistory);
    }

    /**
     * Places all the moves prior to the move to be made.
     *
     * @param board       What the moves take place on.
     * @param moveHistory The log of all moves made.
     */
    private void placeRemainingMoves(final Board board, final MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();

        for (final Move move : moveHistory.getMoves()) {
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(moveText, currentRow, 0);
            } else if(move.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        if (!moveHistory.getMoves().isEmpty()) {
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckmateHash(board),
                                      currentRow,
                                      0);
            } else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckmateHash(board),
                                      currentRow - 1,
                                      1);
            }
        }

        final JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    /**
     * Returns a String representation of a checkmate, check, or neither.
     *
     * @param board What the move takes place on.
     * @return A String representation of a checkmate, check, or neither.
     */
    private String calculateCheckAndCheckmateHash(final Board board) {
        if (board.getCurrentPlayer().isInCheckmate()) {
            return "#";
        } else if (board.getCurrentPlayer().isInCheck()) {
            return "+";
        }

        return "";
    }

    /**
     * Represents the model for data relevant to chess. The table consists of rows and columns that hold a player or
     * opponent's move to be displayed.
     *
     * @author Jamie Canada
     * @since 10/13/25
     */
    private static class DataModel extends DefaultTableModel {
        private final List<Row> values;
        private static final String[] PLAYER_NAMES = { "White", "Black" };

        /**
         * Creates a DataModel object that initializes the list of rows for the game history table.
         */
        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            this.setRowCount(0);
        }

        /**
         * Returns the number of rows in the game history table.
         *
         * @return The number of rows in the game history table.
         */
        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }

            return this.values.size();
        }

        /**
         * Returns the number of columns in the game history table.
         *
         * @return The number of columns in the game history table.
         */
        @Override
        public int getColumnCount() {
            return PLAYER_NAMES.length;
        }

        /**
         * Returns the value at a given (row, column).
         *
         * @param row The row the value is on.
         * @param column The column the value is on.
         * @return The value at a given (row, column).
         */
        @Override
        public Object getValueAt(final int row, final int column) {
            final Row currentRow = this.values.get(row);
            if (column == 0) {
                return currentRow.getWhiteMove();
            } else if (column == 1) {
                return currentRow.getBlackMove();
            }

            return null;
        }

        /**
         * Sets the current value at a (row, column) to a new value.
         *
         * @param newValue What replaces the current value.
         * @param row      The row the current value is on.
         * @param column   The column the current value is on.
         */
        @Override
        public void setValueAt(final Object newValue, final int row, final int column) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if (column == 0) {
                currentRow.setWhiteMove((String) newValue);
                fireTableRowsInserted(row, row);
            } else if (column == 1) {
                currentRow.setBlackMove((String) newValue);
                fireTableCellUpdated(row, column);
            }
        }

        /**
         * Returns the class associated with the column.
         *
         * @param column Where to identify a class.
         * @return The class associated with the column.
         */
        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        /**
         * Returns the column's name.
         *
         * @param column Represents the player's alliance.
         * @return The column's name.
         */
        @Override
        public String getColumnName(final int column) {
            return PLAYER_NAMES[column];
        }
    }

    /**
     * Represents each row for the game history table. The row will consist of two moves: one for the player and
     * one for the opponent.
     *
     * @author Jamie Canada
     * @since 10/13/25
     */
    private static class Row {
        private String whiteMove, blackMove;

        /**
         * Creates a Row object to represent white's and black's moves.
         */
        Row() {

        }

        /**
         * Returns the String representation of white's move.
         *
         * @return The String representation of white's move.
         */
        public String getWhiteMove() {
            return this.whiteMove;
        }

        /**
         * Sets white's move to the parameter move.
         *
         * @param move The move to set white's move to.
         */
        public void setWhiteMove(final String move) {
            this.whiteMove = move;
        }

        /**
         * Returns the String representation of black's move.
         *
         * @return The String representation of black's move.
         */
        public String getBlackMove() {
            return this.blackMove;
        }

        /**
         * Sets black's move to the parameter move.
         *
         * @param move The move to set black's move to.
         */
        public void setBlackMove(final String move) {
            this.blackMove = move;
        }
    }
}
