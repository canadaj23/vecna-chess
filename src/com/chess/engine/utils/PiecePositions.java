package com.chess.engine.utils;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static com.chess.engine.Alliance.*;
import static com.chess.engine.Position.ALL_BOARD_POSITIONS_CACHE;
import static com.chess.engine.utils.BoardUtils.*;

/**
 * Holds caches for each alliance's piece's position.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PiecePositions {
    public static final Map<Integer, Piece> ALL_WP_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "P"),
                                            ALL_WR_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "R"),
                                            ALL_WN_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "N"),
                                            ALL_WB_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "B"),
                                            ALL_WQ_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "Q"),
                                            ALL_WK_POSITIONS_CACHE = getAllPossiblePositions(WHITE, "K"),
                                            ALL_BP_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "P"),
                                            ALL_BR_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "R"),
                                            ALL_BN_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "N"),
                                            ALL_BB_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "B"),
                                            ALL_BQ_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "Q"),
                                            ALL_BK_POSITIONS_CACHE = getAllPossiblePositions(BLACK, "K");

    private static Map<Integer, Piece> getAllPossiblePositions(final Alliance alliance, final String pieceType) {
        final Map<Integer, Piece> piecePositionsMap = new HashMap<>();

        for (int i = 0; i < NUM_SQUARES_PER_BOARD; i++) {
            switch (pieceType) {
                case "P" -> piecePositionsMap.put(i, new Pawn(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
                case "R" -> piecePositionsMap.put(i, new Rook(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
                case "N" -> piecePositionsMap.put(i, new Knight(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
                case "B" -> piecePositionsMap.put(i, new Bishop(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
                case "Q" -> piecePositionsMap.put(i, new Queen(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
                case "K" -> piecePositionsMap.put(i, new King(alliance, ALL_BOARD_POSITIONS_CACHE.get(i)));
            }
        }

        return ImmutableMap.copyOf(piecePositionsMap);
    }
}
