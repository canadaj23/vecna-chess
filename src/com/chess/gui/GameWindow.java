package com.chess.gui;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.MoveFactory;
import com.chess.engine.moves.MoveTransition;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.ai.movestrategy.MiniMax;
import com.chess.engine.player.ai.movestrategy.MoveStrategy;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.BOARD_NUM_SQUARES;
import static com.chess.gui.utils.DimensionConstants.SCALE;
import static com.chess.gui.utils.DimensionConstants.TILE_PANEL_DIMENSION;
import static javax.swing.SwingUtilities.*;

/**
 * Represents some of the display of the chess game. It comprises multiple GUI elements that will together display
 * the chess game.
 *
 * @author Jamie Canada
 * @since 10/10/25
 */
public class GameWindow extends Observable {
    private final JFrame gameFrame;
    private final MoveHistoryPanel moveHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;

    private Board chessboard;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private Move computerMove;

    private boolean shouldHighlightLegalMoves;

    protected static final String DEFAULT_PIECE_PATH = "res/pieces";
    private static final String DEFAULT_GREEN_DOT_PATH = "res/misc/green_dots";
    private final Color lightTileColor = Color.decode("#eeeed2");
    private final Color darkTileColor = Color.decode("#769656");

    private static final int SEARCH_DEPTH = 4;
    private static final GameWindow INSTANCE = new GameWindow();

    /**
     * Creates the GameWindow object that can display the chessboard.
     */
    private GameWindow() {
        this.gameFrame = new JFrame("Vecna");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar gameWindowMenuBar = createGameWindowMenuBar();
        this.gameFrame.setJMenuBar(gameWindowMenuBar);

        this.chessboard = Board.createInitialBoard();

        this.moveHistoryPanel = new MoveHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();

        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new ChessGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);

        this.boardDirection = BoardDirection.NORMAL;

        this.gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(moveHistoryPanel, BorderLayout.EAST);

        this.shouldHighlightLegalMoves = false;

        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setResizable(false);
        this.gameFrame.pack();
        this.gameFrame.setSize(DimensionConstants.OUTER_FRAME_DIMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.gameFrame.setVisible(true);
    }

    /**
     * Returns the GameWindow singleton.
     *
     * @return The GameWindow singleton.
     */
    public static GameWindow get() {
        return INSTANCE;
    }

    /**
     * Resets certain components to their initial states.
     */
    public void show() {
        GameWindow.get().getMoveLog().clear();
        GameWindow.get().getMoveHistoryPanel().redo(chessboard, GameWindow.get().getMoveLog());
        GameWindow.get().getTakenPiecesPanel().redo(GameWindow.get().getMoveLog());
        GameWindow.get().getBoardPanel().drawBoard(chessboard);
    }

    /**
     * Returns the current game setup.
     *
     * @return The current game setup.
     */
    public GameSetup getGameSetup() {
        return this.gameSetup;
    }

    /**
     * Returns the current game board.
     *
     * @return The current game board.
     */
    public Board getGameboard() {
        return this.chessboard;
    }

    /**
     * Sets the current game board to the parameter board.
     *
     * @param board The board to use as the current board.
     */
    public void updateGameboard(final Board board) {
        this.chessboard = board;
    }

    /**
     * Returns the MoveHistoryPanel object.
     *
     * @return The MoveHistoryPanel object.
     */
    public MoveHistoryPanel getMoveHistoryPanel() {
        return this.moveHistoryPanel;
    }

    /**
     * Returns the TakenPiecesPanel object.
     *
     * @return The TakenPiecesPanel object.
     */
    public TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    /**
     * Returns the BoardPanel object.
     *
     * @return The BoardPanel object.
     */
    BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    /**
     * Returns the move log.
     *
     * @return The move log.
     */
    public MoveLog getMoveLog() {
        return this.moveLog;
    }

    /**
     * Sets the current computer move to the parameter move.
     *
     * @param move The move to use as the current computer move.
     */
    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    /**
     * Makes an update of the move made based on the player type.
     *
     * @param playerType The current type of player.
     */
    public void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    /**
     * Gets the update ready to be sent to observers.
     */
    public void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    /**
     * Represents an observer for the AI to be notified when to make a move.
     *
     * @author Jamie Canada
     * @since 10/16/25
     */
    private static class ChessGameAIWatcher implements Observer {
        /**
         * Gives an update to the observer based on the player switching from human to AI.
         *
         * @param observable What was updated.
         * @param o
         */
        @Override
        public void update(final Observable observable, final Object o) {
            if (GameWindow.get().getGameSetup().isAIPlayer(GameWindow.get().getGameboard().getCurrentPlayer()) &&
                !GameWindow.get().getGameboard().getCurrentPlayer().isInCheckmate() &&
                !GameWindow.get().getGameboard().getCurrentPlayer().isInStalemate()) {
                // TODO: create an AI thread and execute AI work
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
            if (GameWindow.get().getGameboard().getCurrentPlayer().isInCheckmate()) {
                System.out.printf("Game Over! %s is in checkmate!",
                                  GameWindow.get().getGameboard().getCurrentPlayer().toString());
            }
            if (GameWindow.get().getGameboard().getCurrentPlayer().isInStalemate()) {
                System.out.printf("Game Over! %s is in stalemate!",
                        GameWindow.get().getGameboard().getCurrentPlayer().toString());
            }
        }
    }

    /**
     * Represents the move making process of the AI on a separate thread. This class extends SwingWorker and implements
     * some of its methods.
     *
     * @author Jamie Canada
     * @since 10/16/25
     */
    private static class AIThinkTank extends SwingWorker<Move, String> {
        /**
         * Creates an AIThinkTank object to be used for background move processing.
         */
        private AIThinkTank() {

        }

        /**
         * Returns a move done on a separate thread by the AI.
         *
         * @return A move done on a separate thread by the AI.
         * @throws Exception Thrown when a move cannot be made.
         */
        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy minimax = new MiniMax(SEARCH_DEPTH);

            return minimax.execute(GameWindow.get().getGameboard());
        }

        /**
         * Cleanup will be made before the thread is exterminated. I.e., several GUI components are updated after a
         * move performed by the AI is done.
         */
        @Override
        public void done() {
            try {
                final Move bestMove = get();
                GameWindow.get().updateComputerMove(bestMove);
                GameWindow.get().updateGameboard(GameWindow.get().getGameboard().getCurrentPlayer().
                                                                                 makeMove(bestMove).
                                                                                 getTransitionBoard());
                GameWindow.get().getMoveLog().addMove(bestMove);
                GameWindow.get().getMoveHistoryPanel().redo(GameWindow.get().getGameboard(),
                                                            GameWindow.get().getMoveLog());
                GameWindow.get().getTakenPiecesPanel().redo(GameWindow.get().getMoveLog());
                GameWindow.get().getBoardPanel().drawBoard(GameWindow.get().getGameboard());
                GameWindow.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
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
        gameWindowMenuBar.add(createOptionsMenu());

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

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");

        final JMenuItem setupGame = new JMenuItem("Setup Game");
        setupGame.addActionListener(actionEvent -> {
            GameWindow.get().getGameSetup().promptUser();
            GameWindow.get().setupUpdate(GameWindow.get().getGameSetup());
        });
        optionsMenu.add(setupGame);

        return optionsMenu;
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
     * Represents the two types of players: human and computer.
     *
     * @author Jamie Canada
     * @since 10/16/25
     */
    public enum PlayerType {
        HUMAN, COMPUTER;
    }

    /**
     * Represents the move log for the game of chess. The move log will keep track of what moves were made (attacks,
     * castling, normal capture, etc.).
     */
    public static class MoveLog {
        private final List<Move> moves;

        /**
         * Creates a MoveLog object to be used for move logging.
         */
        MoveLog() {
            this.moves = new ArrayList<>();
        }

        /**
         * Returns the move log containing all moves made.
         *
         * @return The move log containing all moves made.
         */
        public List<Move> getMoves() {
            return this.moves;
        }

        /**
         * Adds a made move to the move log.
         */
        public void addMove(final Move move) {
            this.moves.add(move);
        }

        /**
         * Returns the size of the move log.
         *
         * @return The size of the move log.
         */
        public int size() {
            return this.moves.size();
        }

        /**
         * Clears the move log.
         */
        public void clear() {
            this.moves.clear();
        }

        /**
         * Returns the move based on the given index.
         *
         * @return The move based on the given index.
         */
        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        /**
         * Returns the move if in the move log.
         *
         * @return The move if in the move log.
         */
        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
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
            for (int i = 0; i < BOARD_NUM_SQUARES; i++) {
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
        private int squarePositionIndex;

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

                            final Move move = MoveFactory.findMove(chessboard,
                                                                   sourceSquare.getSquarePosition(),
                                                                   destinationSquare.getSquarePosition());
                            final MoveTransition transition = chessboard.getCurrentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) {
                                chessboard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            cancelSelection();
                        }
                        invokeLater(() -> {
                            moveHistoryPanel.redo(chessboard, moveLog);
                            takenPiecesPanel.redo(moveLog);

                            if (gameSetup.isAIPlayer(GameWindow.get().getGameboard().getCurrentPlayer())) {
                                GameWindow.get().moveMadeUpdate(PlayerType.HUMAN);
                            }

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
                String imagesFolder = getImagesFolder();

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
         * Returns the directory of images based on the game scale.
         *
         * @return The directory of images based on the game scale.
         */
        private String getImagesFolder() {
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

            return imagesFolder;
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
                    if (move.getDestinationPosition() == squarePosition) {
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
