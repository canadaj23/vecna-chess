package com.chess.engine.utils;

import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.castle.KingsideCastleMove;
import com.chess.engine.moves.castle.QueensideCastleMove;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.player.Player.BLACK_CASTLE_RANK;
import static com.chess.engine.player.Player.WHITE_CASTLE_RANK;

/**
 * Holds all the player-relevant constants and methods.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class PlayerUtils {
    /**
     * Returns a list of all the attacks on the player's king.
     *
     * @param kingPosition The king's position on the chessboard.
     * @param moves        The list of the opponent's moves.
     * @return A list of all the attacks on the player's king.
     */
    public static Collection<Move> calculateAttacksOnKing(final Position kingPosition, final Collection<Move> moves) {
        final List<Move> attacksOnKing = new ArrayList<>();

        for (final Move move : moves) {
            final int kingRank = kingPosition.rank(), kingFile = kingPosition.file();
            if (getPositionIndex(kingRank, kingFile) == move.getDestinationIndex()) {
                attacksOnKing.add(move);
            }
        }

        return ImmutableList.copyOf(attacksOnKing);
    }

    /**
     * Returns a list of all the available castles for a player.
     *
     * @param playerKing             The player's king.
     * @param player                 The player making the castle.
     * @param board                  What the castle takes place on.
     * @param kingsideCastleFiles  An array of square indices for kingside castling.
     * @param queensideCastleFiles An array of square indices for queenside castling.
     * @param opponentLegals         The opponent's legal moves.
     * @return A list of all the available castles for a player.
     */
    public static Collection<Move> calculateCastles(final King playerKing,
                                                    final Player player,
                                                    final Board board,
                                                    final int[] kingsideCastleFiles,
                                                    final int[] queensideCastleFiles,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> castles = new ArrayList<>();
        final int castleRank = player.getAlliance().isBlack() ? BLACK_CASTLE_RANK : WHITE_CASTLE_RANK;

        if (playerKing.isFirstMove() && !player.isInCheck()) {
            if (isKingsideClear(board, kingsideCastleFiles, castleRank)) {
                // Kingside castle
                final int rookStartIndex = getPositionIndex(castleRank, kingsideCastleFiles[2]);
                final Position rookStartPosition = ALL_BOARD_POSITIONS_CACHE.get(rookStartIndex);
                final Square rookSquare = board.getSquare(rookStartPosition);
                if (isFirstMoveRook(rookSquare)) {
                    if (noKingsideCheck(kingsideCastleFiles, opponentLegals)) {
                        final int kingDestinationIndex = getPositionIndex(castleRank, kingsideCastleFiles[1]);
                        final int rookDestinationIndex = getPositionIndex(castleRank, kingsideCastleFiles[0]);
                        castles.add(new KingsideCastleMove(board,
                                                           playerKing,
                                                           ALL_BOARD_POSITIONS_CACHE.get(kingDestinationIndex),
                                                           (Rook) rookSquare.getPiece(),
                                                           ALL_BOARD_POSITIONS_CACHE.get(rookDestinationIndex)));
                    }
                }
            } else if (isQueensideClear(board, queensideCastleFiles, castleRank)) {
                // Queenside castle
                final int rookStartIndex = getPositionIndex(castleRank, kingsideCastleFiles[3]);
                final Position rookStartPosition = ALL_BOARD_POSITIONS_CACHE.get(rookStartIndex);
                final Square rookSquare = board.getSquare(rookStartPosition);
                if (isFirstMoveRook(rookSquare)) {
                    if (noQueensideCheck(queensideCastleFiles, opponentLegals)) {
                        final int kingDestinationIndex = getPositionIndex(castleRank, queensideCastleFiles[1]);
                        final int rookDestinationIndex = getPositionIndex(castleRank, queensideCastleFiles[2]);
                        castles.add(new QueensideCastleMove(board,
                                                            playerKing,
                                                            ALL_BOARD_POSITIONS_CACHE.get(kingDestinationIndex),
                                                            (Rook) rookSquare.getPiece(),
                                                            ALL_BOARD_POSITIONS_CACHE.get(rookDestinationIndex)));
                    }
                }
            }
        }

        return ImmutableList.copyOf(castles);
    }

    /**
     * Returns whether the kingside squares in between the king and rook are clear.
     *
     * @param board               What the castle takes place on.
     * @param kingsideCastleFiles The indices of the squares in between the king and kingside rook.
     * @param castleRank          The rank the castle takes place on.
     * @return Whether the kingside squares in between the king and rook are clear.
     */
    private static boolean isKingsideClear(final Board board, final int[] kingsideCastleFiles, final int castleRank) {
        final int leftSquareIndex = getPositionIndex(castleRank, kingsideCastleFiles[0]);
        final int rightSquareIndex = getPositionIndex(castleRank, kingsideCastleFiles[1]);
        final Position leftSquarePosition = ALL_BOARD_POSITIONS_CACHE.get(leftSquareIndex);
        final Position rightSquarePosition = ALL_BOARD_POSITIONS_CACHE.get(rightSquareIndex);

        return !board.getSquare(leftSquarePosition).isSquareOccupied() &&
               !board.getSquare(rightSquarePosition).isSquareOccupied();
    }

    /**
     * Returns whether the queenside squares in between the king and rook are clear.
     *
     * @param board                What the castle takes place on.
     * @param queensideCastleFiles The indices of the squares in between the king and queenside rook.
     * @param castleRank           The rank the castle takes place on.
     * @return Whether the queenside squares in between the king and rook are clear.
     */
    private static boolean isQueensideClear(final Board board, final int[] queensideCastleFiles, final int castleRank) {
        final int leftSquareIndex = getPositionIndex(castleRank, queensideCastleFiles[0]);
        final int middleSquareIndex = getPositionIndex(castleRank, queensideCastleFiles[1]);
        final int rightSquareIndex = getPositionIndex(castleRank, queensideCastleFiles[2]);
        final Position leftSquarePosition = ALL_BOARD_POSITIONS_CACHE.get(leftSquareIndex);
        final Position middleSquarePosition = ALL_BOARD_POSITIONS_CACHE.get(middleSquareIndex);
        final Position rightSquarePosition = ALL_BOARD_POSITIONS_CACHE.get(rightSquareIndex);

        return !board.getSquare(leftSquarePosition).isSquareOccupied() &&
               !board.getSquare(middleSquarePosition).isSquareOccupied() &&
               !board.getSquare(rightSquarePosition).isSquareOccupied();
    }

    /**
     * Returns whether the square is occupied by a rook and if it is its first move.
     *
     * @param rookSquare The square with a possible rook on it.
     * @return Whether the square is occupied by a rook and if it is its first move.
     */
    private static boolean isFirstMoveRook(final Square rookSquare) {
        return rookSquare.isSquareOccupied() &&
               rookSquare.getPiece().getPieceType().isRook() &&
               rookSquare.getPiece().isFirstMove();
    }

    /**
     * Returns whether there are any checks on the kingside during a possible castle.
     *
     * @param kingsideSquareIndices The indices of the squares in between the king and kingside rook.
     * @param opponentLegals        The opponent's legal moves.
     * @return Whether there are any checks on the kingside during a possible castle.
     */
    private static boolean noKingsideCheck(final int[] kingsideSquareIndices, final Collection<Move> opponentLegals) {
        return calculateAttacksOnKing(ALL_BOARD_POSITIONS_CACHE.get(kingsideSquareIndices[0]),
                                                                    opponentLegals).isEmpty() &&
               calculateAttacksOnKing(ALL_BOARD_POSITIONS_CACHE.get(kingsideSquareIndices[1]),
                                                                    opponentLegals).isEmpty();
    }

    /**
     * Returns whether there are any checks on the queenside during a possible castle.
     *
     * @param queensideSquareIndices The indices of the squares in between the king and queenside rook.
     * @param opponentLegals         The opponent's legal moves.
     * @return Whether there are any checks on the queenside during a possible castle.
     */
    private static boolean noQueensideCheck(final int[] queensideSquareIndices, final Collection<Move> opponentLegals) {
        return calculateAttacksOnKing(ALL_BOARD_POSITIONS_CACHE.get(queensideSquareIndices[0]),
                                                                    opponentLegals).isEmpty() &&
               calculateAttacksOnKing(ALL_BOARD_POSITIONS_CACHE.get(queensideSquareIndices[1]),
                                                                    opponentLegals).isEmpty();
    }
}
