package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Square;
import com.chess.engine.moves.misc.MajorAttackMove;
import com.chess.engine.moves.misc.MajorMove;
import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

import java.util.List;

import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.Position.getPositionIndex;
import static com.chess.engine.utils.BoardUtils.isValidPosition;

/**
 * Holds all the piece-relevant constants and methods.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PieceUtils {

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
                    legalMoves.add(new MajorAttackMove(board, movedPiece, destPosition, pieceOnSquare));
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
                        legalMoves.add(new MajorAttackMove(board, movedPiece, destPosition, pieceOnSquare));
                    }
                    break;
                }
                destRank += rankOffsets[i];
                destFile += fileOffsets[i];
            }
        }
    }
}
