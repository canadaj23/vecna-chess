package com.chess.gui;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveFactory;
import com.chess.engine.moves.MoveTransition;
import com.chess.engine.pieces.Piece;
import com.chess.gui.utils.DimensionConstants;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.NUM_SQUARES_PER_BOARD;
import static com.chess.gui.utils.DimensionConstants.SCALE;
import static com.chess.gui.utils.DimensionConstants.TILE_PANEL_DIMENSION;
import static javax.swing.SwingUtilities.*;

/**
 * Represents some of the display of the chess game. It comprises BoardPanel and SquarePanel that will together display
 * the chessboard.
 *
 * @author Jamie Canada
 * @since 10/10/25
 */
public class GameWindow {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessboard;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private boolean shouldHighlightLegalMoves;

    private static final String DEFAULT_PIECE_PATH = "res/pieces";
    private static final String DEFAULT_GREEN_DOT_PATH = "res/misc/green_dots";
    private final Color lightTileColor = Color.decode("#eeeed2");
    private final Color darkTileColor = Color.decode("#769656");

    /**
     * Creates the GameWindow object that can display the chessboard.
     */
    public GameWindow() {
        this.gameFrame = new JFrame("Vecna");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar gameWindowMenuBar = createGameWindowMenuBar();
        this.gameFrame.setJMenuBar(gameWindowMenuBar);

        this.chessboard = Board.createInitialBoard();

        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);

        this.shouldHighlightLegalMoves = false;

        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);
        this.gameFrame.pack();
        this.gameFrame.setSize(DimensionConstants.OUTER_FRAME_DIMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
    }

    /**
     * Returns a JMenuBar object for chess-related menus.
     *
     * @return A JMenuBar object for chess-related menus.
     */
    private JMenuBar createGameWindowMenuBar() {
        final JMenuBar gameWindowMenuBar = new JMenuBar();

        gameWindowMenuBar.add(createFileMenu());
        gameWindowMenuBar.add(createPreferencesMenu());

        return gameWindowMenuBar;
    }

    /**
     * Returns the file menu to be displayed and used on the game window.
     *
     * @return The file menu to be displayed and used on the game window.
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPgn = new JMenuItem("Load PGN File");
        openPgn.addActionListener(actionEvent -> System.out.println("Open the PGN file."));
        fileMenu.add(openPgn);


        final JMenuItem exitProgram = new JMenuItem("Quit");
        exitProgram.addActionListener(actionEvent -> System.exit(0));
        fileMenu.add(exitProgram);

        return fileMenu;
    }

    /**
     * Returns the preferences menu to be displayed and used on the game window.
     *
     * @return The preferences menu to be displayed and used on the game window.
     */
    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");

        final JMenuItem flipBoard = new JMenuItem("Flip Board");
        flipBoard.addActionListener(actionEvent -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessboard);
        });
        preferencesMenu.add(flipBoard);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem highlightLegalMovesCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves",
                                                                                    false);
        highlightLegalMovesCheckbox.addActionListener(actionEvent -> {
            shouldHighlightLegalMoves = highlightLegalMovesCheckbox.isSelected();
        });
        preferencesMenu.add(highlightLegalMovesCheckbox);

        return preferencesMenu;
    }

    /**
     * Represents the way to view the chessboard.
     */
    public enum BoardDirection {
        NORMAL {
            /**
             * Returns the opposite board direction.
             *
             * @return The opposite board direction.
             */
            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            /**
             * Returns the opposite board direction.
             *
             * @return The opposite board direction.
             */
            @Override
            BoardDirection opposite() {
                return NORMAL;
            }

            /**
             * Returns the order of squares based on the direction.
             *
             * @param boardSquares The list containing all the squares on the board.
             * @return The order of squares based on the direction.
             */
            @Override
            List<SquarePanel> traverse(final List<SquarePanel> boardSquares) {
                return Lists.reverse(boardSquares);
            }
        };

        /**
         * Returns the opposite board direction.
         *
         * @return The opposite board direction.
         */
        abstract BoardDirection opposite();

        /**
         * Returns the order of squares based on the direction.
         *
         * @param boardSquares The list containing all the squares on the board.
         * @return The order of squares based on the direction.
         */
        List<SquarePanel> traverse(final List<SquarePanel> boardSquares) {
            return boardSquares;
        }
    }

    /**
     * Represents the GUI chessboard to be displayed. The BoardPanel comprises multiple SquarePanel objects.
     *
     * @author Jamie Canada
     * @since 10/10/25
     */
    private class BoardPanel extends JPanel {
        final List<SquarePanel> boardSquares;

        /**
         * Creates a BoardPanel object that contains 64 SquarePanel objects to be displayed as the chessboard.
         */
        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardSquares = new ArrayList<>();
            addSquarePanels();
            setPreferredSize(DimensionConstants.BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * Adds all the necessary SquarePanel objects for the GUI chessboard.
         */
        private void addSquarePanels() {
            for (int i = 0; i < NUM_SQUARES_PER_BOARD; i++) {
                final SquarePanel squarePanel = new SquarePanel(this, ALL_BOARD_POSITIONS_CACHE.get(i));
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
        }

        /**
         * Draws the current chessboard to be displayed.
         */
        public void drawBoard(final Board board) {
            removeAll();
            for (final SquarePanel squarePanel : boardDirection.traverse(boardSquares)) {
                squarePanel.drawSquare(board);
                add(squarePanel);
            }
            validate();
            repaint();
        }
    }

    /**
     * Represents each GUI square to be displayed. Each SquarePanel object will either be displayed as empty or occupied
     * with a chess piece icon.
     *
     * @author Jamie Canada
     * @since 10/10/25
     */
    private class SquarePanel extends JPanel {
        private final Position squarePosition;
        private final int squarePositionIndex;

        /**
         * Creates a SquarePanel object that will (not) have a piece icon associated with it.
         */
        public SquarePanel(final BoardPanel boardPanel, final Position squarePosition) {
            super(new GridBagLayout());
            this.squarePosition = squarePosition;
            this.squarePositionIndex = getPositionIndex(this.squarePosition.rank(), this.squarePosition.file());
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignSquareColor();
            assignSquarePieceIcon(chessboard);

            addMouseListener(new MouseListener() {
                /**
                 * Responds to either a right or left mouse click when playing chess.
                 *
                 * @param e The event made by the mouse.
                 */
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        cancelSelection();
                    } else if (isLeftMouseButton(e)) {
                        if (sourceSquare == null) {
                            // Selecting source
                            final Position sourcePosition = ALL_BOARD_POSITIONS_CACHE.get(squarePositionIndex);
                            sourceSquare = chessboard.getSquare(sourcePosition);
                            humanMovedPiece = sourceSquare.getPiece();
                            if (humanMovedPiece == null) {
                                sourceSquare = null;
                            }
                        } else {
                            // Selecting destination
                            final Position destinationPosition = ALL_BOARD_POSITIONS_CACHE.get(squarePositionIndex);
                            destinationSquare = chessboard.getSquare(destinationPosition);
                            final int destinationPositionIndex = getPositionIndex(destinationPosition.rank(),
                                    destinationPosition.file());
                            final Move move = MoveFactory.findMove(chessboard,
                                    sourceSquare.getSquarePosition(),
                                    destinationSquare.getSquarePosition());
                            final MoveTransition transition = chessboard.getCurrentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) {
                                chessboard = transition.getTransitionBoard();
                                // TODO: add the finished move to the move log
                            }
                            cancelSelection();
                        }
                        invokeLater(() -> {
                            boardPanel.drawBoard(chessboard);
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {}

                @Override
                public void mouseReleased(final MouseEvent e) {}

                @Override
                public void mouseEntered(final MouseEvent e) {}

                @Override
                public void mouseExited(final MouseEvent e) {}
            });

            validate();
        }

        /**
         * Assigns a color to a square based off the square's position being even or odd.
         */
        private void assignSquareColor() {
            boolean isLight = (this.squarePositionIndex + this.squarePositionIndex / 8) % 2 == 0;
            setBackground(isLight ? lightTileColor : darkTileColor);
        }

        /**
         * Assigns a piece icon to an occupied square.
         *
         * @param board What the pieces and squares are on.
         */
        private void assignSquarePieceIcon(final Board board) {
            this.removeAll();
            if (board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(squarePositionIndex)).isSquareOccupied()) {
                String imagesFolder = "";
                if (SCALE <= 1.0f) {
                    imagesFolder = "/60px_60px/";
                } else if (SCALE <= 1.25f) {
                    imagesFolder = "/70px_70px/";
                } else if (SCALE <= 1.5f) {
                    imagesFolder = "/80px_80px/";
                } else if (SCALE <= 1.75f) {
                    imagesFolder = "/90px_90px/";
                } else if (SCALE <= 2.0f) {
                    imagesFolder = "/100px_100px/";
                }

                try {
                    final Square squareOnBoard = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(squarePositionIndex));
                    final Piece pieceOnSquare = squareOnBoard.getPiece();
                    final char pieceAllianceChar = pieceOnSquare.getPieceAlliance().toString().charAt(0);
                    final BufferedImage pieceImage = ImageIO.read(new File(DEFAULT_PIECE_PATH +
                                                                           imagesFolder +
                                                                           pieceAllianceChar +
                                                                           pieceOnSquare.getPieceType().toString() +
                                                                           ".png"));
                    add(new JLabel(new ImageIcon(pieceImage)));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Cancels a selection during a move.
         */
        private void cancelSelection() {
            sourceSquare = null;
            destinationSquare = null;
            humanMovedPiece = null;
        }

        /**
         * Draws one of the squares to be placed on the GUI board.
         *
         * @param board What holds the square.
         */
        public void drawSquare(final Board board) {
            assignSquareColor();
            assignSquarePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        /**
         * Allows the user to see which squares the piece can legally move to.
         *
         * @param board What the piece is on.
         */
        private void highlightLegalMoves(final Board board) {
            if (shouldHighlightLegalMoves) {
                for (final Move move : getPieceLegalMoves(board)) {
                    if (move.getDestinationPosition() == this.squarePosition) {
                        try {
                            String greenDotSize = "";
                            if (SCALE <= 1.0f) {
                                greenDotSize = "/16px_16px/";
                            } else if (SCALE <= 1.25f) {
                                greenDotSize = "/32px_32px/";
                            } else if (SCALE <= 1.5f) {
                                greenDotSize = "/48px_48px/";
                            } else if (SCALE <= 2.0f) {
                                greenDotSize = "/64px_64px/";
                            }

                            add(new JLabel(new ImageIcon(ImageIO.read(new File(DEFAULT_GREEN_DOT_PATH +
                                                                               greenDotSize +
                                                                               "green_dot.png")))));
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * Returns a list of legal moves for the piece to be moved.
         *
         * @param board What the piece moves on.
         * @return A list of legal moves for the piece to be moved.
         */
        private Collection<Move> getPieceLegalMoves(final Board board) {
            if (humanMovedPiece != null &&
                humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }

            return Collections.emptyList();
        }
    }
}
