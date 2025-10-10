package com.chess.gui.utils;

import java.awt.Dimension;

/**
 * Holds all constants related to dimensions for various pieces of the GUI.
 */
public class DimensionConstants {
    public static final float SCALE = 1.5f;

    private static final int DEFAULT_OUTER_FRAME_WIDTH = 600, DEFAULT_OUTER_FRAME_HEIGHT = 600;
    private static final int OUTER_FRAME_WIDTH = (int) (DEFAULT_OUTER_FRAME_WIDTH * SCALE);
    private static final int OUTER_FRAME_HEIGHT = (int) (DEFAULT_OUTER_FRAME_HEIGHT * SCALE);

    private static final int DEFAULT_BOARD_PANEL_WIDTH = 400, DEFAULT_BOARD_PANE_HEIGHT = 350;
    private static final int BOARD_PANEL_WIDTH = (int) (DEFAULT_BOARD_PANEL_WIDTH * SCALE);
    private static final int BOARD_PANEL_HEIGHT = (int) (DEFAULT_BOARD_PANE_HEIGHT * SCALE);

    private static final int DEFAULT_TILE_PANEL_WIDTH = 400, DEFAULT_TILE_PANEL_HEIGHT = 350;
    private static final int TILE_PANEL_WIDTH = (int) (DEFAULT_TILE_PANEL_WIDTH * SCALE);
    private static final int TILE_PANEL_HEIGHT = (int) (DEFAULT_TILE_PANEL_HEIGHT * SCALE);

    public static final Dimension OUTER_FRAME_DIMENSION = new Dimension(OUTER_FRAME_WIDTH, OUTER_FRAME_HEIGHT);
    public static final Dimension BOARD_PANEL_DIMENSION = new Dimension(BOARD_PANEL_WIDTH, BOARD_PANEL_HEIGHT);
    public static final Dimension TILE_PANEL_DIMENSION = new Dimension(TILE_PANEL_WIDTH, TILE_PANEL_HEIGHT);
}
