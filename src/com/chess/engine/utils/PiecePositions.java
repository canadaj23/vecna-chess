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
 * Holds caches for each alliance's piece's position based on the first move boolean.
 *
 * @author Jamie Canada
 * @since 10/09/25
 */
public class PiecePositions {
    // FM = first move
    // NFM = not first move
    public static final Map<Integer, Piece> FM_WP_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "P",
                                                                                            true),
                                            FM_WR_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "R",
                                                                                            true),
                                            FM_WN_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "N",
                                                                                            true),
                                            FM_WB_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "B",
                                                                                            true),
                                            FM_WQ_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "Q",
                                                                                            true),
                                            FM_WK_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                            "K",
                                                                                            true),
                                            FM_BP_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "P",
                                                                                            true),
                                            FM_BR_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "R",
                                                                                            true),
                                            FM_BN_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "N",
                                                                                            true),
                                            FM_BB_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "B",
                                                                                            true),
                                            FM_BQ_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "Q",
                                                                                            true),
                                            FM_BK_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                            "K",
                                                                                            true);

    public static final Map<Integer, Piece> NFM_WP_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "P",
                                                                                             false),
                                            NFM_WR_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "R",
                                                                                             false),
                                            NFM_WN_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "N",
                                                                                             false),
                                            NFM_WB_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "B",
                                                                                             false),
                                            NFM_WQ_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "Q",
                                                                                             false),
                                            NFM_WK_POSITIONS_CACHE = getAllPossiblePositions(WHITE,
                                                                                             "K",
                                                                                             false),
                                            NFM_BP_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "P",
                                                                                             false),
                                            NFM_BR_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "R",
                                                                                             false),
                                            NFM_BN_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "N",
                                                                                             false),
                                            NFM_BB_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "B",
                                                                                             false),
                                            NFM_BQ_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "Q",
                                                                                             false),
                                            NFM_BK_POSITIONS_CACHE = getAllPossiblePositions(BLACK,
                                                                                             "K",
                                                                                             false);

    /**
     * Returns a map of an index mapped to a piece.
     *
     * @param alliance  The player's alliance.
     * @param pieceType The piece's type (name).
     * @param firstMove Whether it's the piece's first move.
     * @return A map of an index mapped to a piece.
     */
    private static Map<Integer, Piece> getAllPossiblePositions(final Alliance alliance,
                                                               final String pieceType,
                                                               final Boolean firstMove) {
        final Map<Integer, Piece> piecePositionsMap = new HashMap<>();

        for (int i = 0; i < BOARD_NUM_SQUARES; i++) {
            switch (pieceType) {
                case "P" -> piecePositionsMap.put(i, new Pawn(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
                case "R" -> piecePositionsMap.put(i, new Rook(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
                case "N" -> piecePositionsMap.put(i, new Knight(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
                case "B" -> piecePositionsMap.put(i, new Bishop(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
                case "Q" -> piecePositionsMap.put(i, new Queen(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
                case "K" -> piecePositionsMap.put(i, new King(alliance, ALL_BOARD_POSITIONS_CACHE.get(i), firstMove));
            }
        }

        return ImmutableMap.copyOf(piecePositionsMap);
    }
}
