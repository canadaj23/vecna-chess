package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

/**
 * Holds constants related to the two alliances seen in chess: black and white.
 *
 * @author Jamie Canada
 * @since 10/07/25
 */
public enum Alliance {
    WHITE {
        /**
         * Returns a chosen move maker.
         *
         * @param whitePlayer A possible move maker.
         * @param blackPlayer A possible move maker.
         * @return A chosen move maker.
         */
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        /**
         * Returns the alliance's direction.
         *
         * @return The direction of the alliance.
         */
        @Override
        public int getDirection() {
            return UP_DIRECTION;
        }

        /**
         * Returns true or false if the alliance is white.
         *
         * @return Whether the alliance is white.
         */
        @Override
        public boolean isWhite() {
            return true;
        }

        /**
         * Returns the alliance's opposite direction.
         *
         * @return The opposite direction of the alliance.
         */
        @Override
        public int getOppositeDirection() {
            return DOWN_DIRECTION;
        }
    },
    BLACK {
        /**
         * Returns a chosen move maker.
         *
         * @param whitePlayer A possible move maker.
         * @param blackPlayer A possible move maker.
         * @return A chosen move maker.
         */
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        /**
         * Returns the alliance's direction.
         *
         * @return The direction of the alliance.
         */
        @Override
        public int getDirection() {
            return DOWN_DIRECTION;
        }
        /**
         * Returns true or false if the alliance is black.
         *
         * @return Whether the alliance is black.
         */
        @Override
        public boolean isBlack() {
            return true;
        }

        /**
         * Returns the alliance's opposite direction.
         *
         * @return The opposite direction of the alliance.
         */
        @Override
        public int getOppositeDirection() {
            return UP_DIRECTION;
        }
    };

    /**
     * Returns the alliance's direction.
     *
     * @return Either 1 or -1.
     */
    public abstract int getDirection();
    public abstract int getOppositeDirection();

    /**
     * Returns a chosen move maker.
     *
     * @param whitePlayer A possible move maker.
     * @param blackPlayer A possible move maker.
     * @return A chosen move maker.
     */
    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    /**
     * Returns true or false if the alliance is white.
     *
     * @return Whether the alliance is white.
     */
    public boolean isWhite() {
        return false;
    }

    /**
     * Returns true or false if the alliance is black.
     *
     * @return Whether the alliance is black.
     */
    public boolean isBlack() {
        return false;
    }

    public static final int UP_DIRECTION = 1, DOWN_DIRECTION = -1;
}
