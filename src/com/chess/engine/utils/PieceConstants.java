package com.chess.engine.utils;

/**
 * Holds offsets and constants related to the pieces.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public class PieceConstants {
    public static final int[] PAWN_FILE_OFFSETS = {  0,  0 };
    public static final int[] PAWN_RANK_OFFSETS = { -1, -2 };

    public static final int[] PAWN_FILE_ATTACK_OFFSETS = { -1,  1 };
    public static final int[] PAWN_RANK_ATTACK_OFFSETS = { -1, -1 };

    public static final int[] KNIGHT_FILE_OFFSETS = { -2, -2, -1,  1,  2, 2, 1, -1 };
    public static final int[] KNIGHT_RANK_OFFSETS = {  1, -1, -2, -2, -1, 1, 2,  2 };

    public static final int[] BISHOP_FILE_OFFSETS = { -1, -1,  1, 1 };
    public static final int[] BISHOP_RANK_OFFSETS = { -1,  1, -1, 1 };

    public static final int[] ROOK_FILE_OFFSETS = { -1,  0, 1, 0 };
    public static final int[] ROOK_RANK_OFFSETS = {  0, -1, 0, 1 };

    public static final int[] QUEEN_KING_FILE_OFFSETS = { -1, -1,  1, 1, -1,  0, 1, 0 };
    public static final int[] QUEEN_KING_RANK_OFFSETS = { -1,  1, -1, 1,  0, -1, 0, 1 };
}
