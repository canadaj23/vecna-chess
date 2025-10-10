package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

import static com.chess.engine.Alliance.*;
import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.board.Square.*;
import static com.chess.engine.utils.BoardUtils.*;

/**
 * Represents the chessboard seen in the game of chess. The board consists of squares and may or may not have pieces
 * on those squares.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class Board {
    private final List<Square> gameboard;
    private final Collection<Piece> whitePieces, blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    /**
     * Creates a Board object with input from the builder.
     *
     * @param builder What will set the pieces and set the move maker.
     */
    private Board(final Builder builder) {
        gameboard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameboard, WHITE);
        this.blackPieces = calculateActivePieces(this.gameboard, BLACK);

        final Collection<Move> whiteLegalMoves = calculateLegalMoves(this, this.whitePieces);
        final Collection<Move> blackLegalMoves = calculateLegalMoves(this, this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    /**
     * Returns an array of all the chess squares on the chessboard. The squares will (not) have pieces on them based
     * off the builder.
     *
     * @param builder What sets the pieces on a new chessboard.
     * @return An array of all the chess squares on the chessboard.
     */
    private static List<Square> createGameBoard(final Builder builder) {
        final Square[] squares = new Square[NUM_SQUARES_PER_BOARD];

        for (int i = 0; i < squares.length; i++) {
            final int rank = i / NUM_SQUARES_PER_RANK, file = i % NUM_SQUARES_PER_FILE;
            final int piecePositionIndex = getPositionIndex(rank, file);
            final Position squarePosition = ALL_BOARD_POSITIONS_CACHE.get(piecePositionIndex);
            squares[i] = createSquare(squarePosition, builder.boardConfig.get(piecePositionIndex));
        }

        return ImmutableList.copyOf(squares);
    }

    /**
     * Returns the initial chessboard, meaning all the pieces are in their initial positions and none have made a move.
     *
     * @return The initial chessboard for the start of a new chess game.
     */
    public static Board createInitialBoard() {
        final Builder builder = new Builder();

        // Black's initial pieces
        setInitialPieces(BLACK, builder);

        // White's initial pieces
        setInitialPieces(WHITE, builder);

        // White is the first move maker
        builder.setMoveMaker(WHITE);

        return builder.build();
    }

    /**
     * Returns the square based off a given square position.
     *
     * @param squarePosition The position of the square to be retrieved.
     * @return The square with the given position.
     */
    public Square getSquare(final Position squarePosition) {
        return this.gameboard.get(getPositionIndex(squarePosition.rank(), squarePosition.file()));
    }

    /**
     * Returns a list of a player's active pieces based on their alliance.
     *
     * @param alliance The player's alliance.
     * @return A list of a player's active pieces based on their alliance.
     */
    public Collection<Piece> getPlayerPieces(final Alliance alliance) {
        return alliance.isBlack() ? this.blackPieces : this.whitePieces;
    }

    /**
     * Returns the player based on the given alliance.
     *
     * @param alliance The player's alliance (WHITE or BLACK).
     * @return The player based on the given alliance.
     */
    public Player getPlayer(final Alliance alliance) {
        return alliance.isBlack() ? this.blackPlayer : this.whitePlayer;
    }

    /**
     * Returns the current player making a move.
     *
     * @return The current player making a move.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Returns an iterable of white's and black players' legal moves.
     *
     * @return An iterable of white's and black players' legal moves.
     */
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),
                                                               this.blackPlayer.getLegalMoves()));
    }

    /**
     * Returns the String representation of the chessboard.
     *
     * @return The String representation of the chessboard.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < NUM_SQUARES_PER_BOARD; i++) {
            final String squareText = this.gameboard.get(i).toString();
            builder.append(String.format("%3s", squareText));
            if ((i + 1) % NUM_SQUARES_PER_RANK == 0) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    /**
     * Creates the current chessboard for chess to be played on. The builder will set pieces and set the next move
     * maker for the next instance of the chessboard (i.e., a move was made).
     *
     * @author Jamie Canada
     * @since 10/09/25
     */
    public static class Builder {
        final HashMap<Integer, Piece> boardConfig;
        private Alliance nextMoveMaker;
        private Pawn enPassantPawn;

        /**
         * Creates a Builder object that will create a chessboard.
         */
        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        /**
         * Returns a new board after the builder sets the pieces and next move maker.
         *
         * @return A new board with updated pieces and move maker.
         */
        public Board build() {
            return new Board(this);
        }

        /**
         * Returns the builder after a piece is set on the chessboard.
         *
         * @param piece The piece to set on the chessboard.
         * @return The builder after a piece is set on the chessboard.
         */
        public Builder setPiece(final Piece piece) {
            final int pieceRank = piece.getPiecePosition().rank();
            final int pieceFile = piece.getPiecePosition().file();
            this.boardConfig.put(getPositionIndex(pieceRank, pieceFile), piece);

            return this;
        }

        /**
         * Returns the builder after the next move maker is set on the chessboard.
         *
         * @param nextMoveMaker Whose turn it is now.
         * @return The builder after the next move maker is set on the chessboard.
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;

            return this;
        }

        /**
         * Returns the en passant pawn.
         *
         * @return The en passant pawn.
         */
        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }

        /**
         * Designates the en passant pawn as the pawn that just moved.
         *
         * @param movedPawn The pawn that just moved.
         */
        public void setEnPassantPawn(final Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }
    }
}
