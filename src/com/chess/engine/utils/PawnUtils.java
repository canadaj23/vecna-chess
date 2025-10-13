package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.Move;
import com.chess.engine.moves.pawn.advance.OneSquarePawnMove;
import com.chess.engine.moves.pawn.advance.TwoSquarePawnMove;
import com.chess.engine.moves.pawn.attack.EnPassantAttack;
import com.chess.engine.moves.pawn.attack.PawnAttackMove;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import java.util.List;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.*;
import static com.chess.engine.utils.PieceConstants.*;

/**
 * Holds methods relevant to the pawn in chess.
 *
 * @author Jamie Canada
 * @since 10/13/25
 */
public class PawnUtils {
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
                        legalMoves.add(new OneSquarePawnMove(board, movedPawn, destPosition));
                    } else if (PAWN_RANK_OFFSETS[i] == -2) {
                        if (movedPawn.isFirstMove() && isInitialPawnPosition(pieceAlliance, sourceRank)) {
                            final Position behindDestRank = ALL_BOARD_POSITIONS_CACHE.get(destIndex + RANK_NUM_SQUARES);
                            if (!board.getSquare(behindDestRank).isSquareOccupied()) {
                                // Two square advance (pawn jump)
                                legalMoves.add(new TwoSquarePawnMove(board, movedPawn, destPosition));
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
                        legalMoves.add(new PawnAttackMove(board, movedPawn, destPosition, pieceOnSquare));
                    }
                } else if (board.getEnPassantPawn() != null) {
                    final Pawn enPassantPawn = board.getEnPassantPawn();
                    final int attackFileOffset = PAWN_FILE_ATTACK_OFFSETS[i];
                    if (enPassantPawnOnSide(enPassantPawn, sourceRank, sourceFile, pieceAlliance, attackFileOffset)) {
                        if (pieceAlliance != enPassantPawn.getPieceAlliance()) {
                            legalMoves.add(new EnPassantAttack(board, movedPawn, destPosition, enPassantPawn));
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether the en passant pawn is on either side of the piece to move.
     *
     * @param enPassantPawn The pawn the advanced by two squares.
     * @param sourceRank    The rank of the piece to move.
     * @param sourceFile    The file of the piece to move.
     * @param pieceAlliance The alliance of the piece to move.
     * @param attackOffset  The offset used for the attack of the piece to move.
     * @return Whether the en passant pawn is on either side of the piece to move.
     */
    private static boolean enPassantPawnOnSide(final Piece enPassantPawn,
                                               final int sourceRank,
                                               final int sourceFile,
                                               final Alliance pieceAlliance,
                                               final int attackOffset) {
        final Position enPassantPawnPosition = enPassantPawn.getPiecePosition();
        final int enPassantPawnIndex =  getPositionIndex(enPassantPawnPosition.rank(), enPassantPawnPosition.file());
        final int leftOfSourceIndex = getPositionIndex(sourceRank, sourceFile) + pieceAlliance.getOppositeDirection();
        final int rightOfSourceIndex = getPositionIndex(sourceRank, sourceFile) - pieceAlliance.getOppositeDirection();

        return enPassantPawnIndex == leftOfSourceIndex && attackOffset == -1 ||
               enPassantPawnIndex == rightOfSourceIndex && attackOffset == 1;

    }
}
