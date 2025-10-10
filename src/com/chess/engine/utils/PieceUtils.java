package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.misc.AttackMove;
import com.chess.engine.moves.misc.MajorMove;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import java.util.List;
import java.util.Map;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.isValidPosition;
import static com.chess.engine.utils.PieceConstants.*;

/**
 * Holds all the piece-relevant constants and methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PieceUtils {

    /**
     * Determines whether the pawn is black and is on the second rank or the pawn is white and is on the seventh
     * rank.
     *
     * @param pawnAlliance The alliance of the pawn.
     * @param pawnRank     The pawn's rank on the chessboard.
     * @return Whether the pawn is black and is on the second rank or the pawn is white and is on the seventh rank.
     */
    public static boolean isInitialPawnPosition(final Alliance pawnAlliance, final int pawnRank) {
        return (pawnAlliance.isBlack() && pawnRank == 1) || (pawnAlliance.isWhite() && pawnRank == 6);
    }

    /**
     * Determines all the legal moves for the pawn on the current chessboard.
     *
     * @param sourceRank    The pawn's rank before a move.
     * @param sourceFile    The pawn's file before a move.
     * @param pieceAlliance The pawn's alliance.
     * @param board         What the move takes place on.
     * @param movedPawn     The pawn that wants to make a move.
     * @param legalMoves    The list that holds all the legal moves for the pawn on the current chessboard.
     */
    public static void calculatePawnLegalMoves(final int sourceRank,
                                                final int sourceFile,
                                                final Alliance pieceAlliance,
                                                final Board board,
                                                final Pawn movedPawn,
                                                final List<Move> legalMoves) {
        // Pawn advancements
        calculatePawnAdvancements(sourceRank, sourceFile, pieceAlliance, board, movedPawn, legalMoves);

        // Pawn attacks
        calculatePawnAttacks(sourceRank, sourceFile, pieceAlliance, board, movedPawn, legalMoves);
    }

    /**
     * Determines the number of advancements the pawn can make on the current chessboard.
     *
     * @param sourceRank    The pawn's rank before advancement.
     * @param sourceFile    The pawn's file before advancement.
     * @param pieceAlliance The pawn's alliance.
     * @param board         What the advancement takes place on.
     * @param movedPawn     The pawn that wants to advance.
     * @param legalMoves    The list that holds all the legal moves for the pawn on the current chessboard.
     */
    private static void calculatePawnAdvancements(final int sourceRank,
                                                  final int sourceFile,
                                                  final Alliance pieceAlliance,
                                                  final Board board,
                                                  final Pawn movedPawn,
                                                  final List<Move> legalMoves) {
        for (int i = 0; i < PAWN_RANK_OFFSETS.length; i++) {
            final int destRank = sourceRank + (PAWN_RANK_OFFSETS[i] * pieceAlliance.getDirection());
            final int destFile = sourceFile + (PAWN_FILE_OFFSETS[i] * pieceAlliance.getDirection());
            final int destIndex = getPositionIndex(destRank, destFile);
            final Square destSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex));
            final Position destPosition = destSquare.getSquarePosition();

            if (isValidPosition(destRank, destFile)) {
                if (!destSquare.isSquareOccupied()) {
                    // Move on an empty square
                    if (PAWN_RANK_OFFSETS[i] == -1) {
                        // One square advance with possible pawn promotion
                        // TODO: implement one square advance with possible pawn promotion
                        legalMoves.add(new MajorMove(board, movedPawn, destPosition));
                    } else if (PAWN_RANK_OFFSETS[i] == -2) {
                        if (movedPawn.isFirstMove() && isInitialPawnPosition(pieceAlliance, sourceRank)) {
                            final int behindDestRank = sourceRank + (PAWN_RANK_OFFSETS[0] *
                                                                     pieceAlliance.getDirection());
                            final int behindDestFile = sourceRank + (PAWN_FILE_OFFSETS[0] *
                                                                     pieceAlliance.getDirection());
                            final int behindDestIndex = getPositionIndex(behindDestRank, behindDestFile);
                            if (!board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(behindDestIndex)).isSquareOccupied()) {
                                // Two square advance (pawn jump)
                                // TODO: implement two square advance
                                legalMoves.add(new MajorMove(board, movedPawn, destPosition));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines the number of attacks the pawn can make on the current chessboard.
     *
     * @param sourceRank    The pawn's rank before the attack.
     * @param sourceFile    The pawn's file before the attack.
     * @param pieceAlliance The pawn's alliance.
     * @param board         What the advancement takes place on.
     * @param movedPawn     The pawn that wants to attack.
     * @param legalMoves    The list that holds all the legal moves for the pawn on the current chessboard.
     */
    private static void calculatePawnAttacks(final int sourceRank,
                                             final int sourceFile,
                                             final Alliance pieceAlliance,
                                             final Board board,
                                             final Pawn movedPawn,
                                             final List<Move> legalMoves) {
        for (int i = 0; i < PAWN_RANK_ATTACK_OFFSETS.length; i++) {
            final int destRank = sourceRank + (PAWN_RANK_ATTACK_OFFSETS[i] * pieceAlliance.getDirection());
            final int destFile = sourceFile + (PAWN_FILE_ATTACK_OFFSETS[i] * pieceAlliance.getDirection());
            final int destIndex = getPositionIndex(destRank, destFile);
            final Square destSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex));
            final Position destPosition = destSquare.getSquarePosition();

            if (isValidPosition(destRank, destFile)) {
                if (destSquare.isSquareOccupied()) {
                    final Piece pieceOnSquare = destSquare.getPiece();
                    if (pieceAlliance != pieceOnSquare.getPieceAlliance()) {
                        // Attack opponent's piece
                        // TODO: implement pawn attack into pawn promotion
                        legalMoves.add(new AttackMove(board, movedPawn, destPosition, pieceOnSquare));
                    }
                }
            }
        }
    }

    /**
     * Determines all the legal moves for the non-sliding piece on the current chessboard.
     *
     * @param sourceRank    The non-sliding piece's rank before a move.
     * @param rankOffsets   The rank offsets when performing a move.
     * @param sourceFile    The non-sliding piece's file before a move.
     * @param fileOffsets   The file offsets when performing a move.
     * @param board         What the move takes place on.
     * @param pieceAlliance The non-sliding piece's alliance.
     * @param movedPiece    The non-sliding piece that wants to make a move.
     * @param legalMoves    The list that holds all the legal moves for the non-sliding piece on the current chessboard.
     */
    public static void calculateNonSlidingLegalMoves(final int sourceRank,
                                                     final int[] rankOffsets,
                                                     final int sourceFile,
                                                     final int[] fileOffsets,
                                                     final Board board,
                                                     final Alliance pieceAlliance,
                                                     final Piece movedPiece,
                                                     final List<Move> legalMoves) {
        for (int i = 0; i < rankOffsets.length; i++) {
            final int destRank = sourceRank + rankOffsets[i];
            final int destFile = sourceFile + fileOffsets[i];
            if (!isValidPosition(destRank, destFile)) {
                continue;
            }
            final int destIndex = getPositionIndex(destRank, destFile);
            final Square destSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex));
            final Position destPosition = destSquare.getSquarePosition();
            if (!destSquare.isSquareOccupied()) {
                // Non-attack move on empty square
                legalMoves.add(new MajorMove(board, movedPiece, destPosition));
            } else {
                // Occupied square
                final Piece pieceOnSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex)).getPiece();
                if (pieceAlliance != pieceOnSquare.getPieceAlliance()) {
                    // Attack move on occupied square
                    legalMoves.add(new AttackMove(board, movedPiece, destPosition, pieceOnSquare));
                }
            }
        }
    }

    /**
     * Determines all the legal moves for the sliding piece on the current chessboard.
     *
     * @param sourceRank    The sliding piece's rank before a move.
     * @param rankOffsets   The rank offsets when performing a move.
     * @param sourceFile    The sliding piece's file before a move.
     * @param fileOffsets   The file offsets when performing a move.
     * @param board         What the move takes place on.
     * @param pieceAlliance The sliding piece's alliance.
     * @param movedPiece    The sliding piece that wants to make a move.
     * @param legalMoves    The list that holds all the legal moves for the sliding piece on the current chessboard.
     */
    public static void calculateSlidingLegalMoves(final int sourceRank,
                                                  final int[] rankOffsets,
                                                  final int sourceFile,
                                                  final int[] fileOffsets,
                                                  final Board board,
                                                  final Alliance pieceAlliance,
                                                  final Piece movedPiece,
                                                  final List<Move> legalMoves) {
        for (int i = 0; i < rankOffsets.length; i++) {
            int destRank = sourceRank + rankOffsets[i], destFile = sourceFile + fileOffsets[i];
            while (isValidPosition(destRank, destFile)) {
                if (!isValidPosition(destRank, destFile)) {
                    break;
                }
                int destIndex = getPositionIndex(destRank, destFile);
                Square destSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex));
                Position destPosition = destSquare.getSquarePosition();
                if (!destSquare.isSquareOccupied()) {
                    // Non-attack move on empty square
                    legalMoves.add(new MajorMove(board, movedPiece, destPosition));
                } else {
                    // Occupied square
                    final Piece pieceOnSquare = board.getSquare(ALL_BOARD_POSITIONS_CACHE.get(destIndex)).getPiece();
                    if (pieceAlliance != pieceOnSquare.getPieceAlliance()) {
                        // Attack move on occupied square
                        legalMoves.add(new AttackMove(board, movedPiece, destPosition, pieceOnSquare));
                    }
                    break;
                }
                destRank += rankOffsets[i];
                destFile += fileOffsets[i];
            }
        }
    }
}
