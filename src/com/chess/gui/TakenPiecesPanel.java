package com.chess.gui;

import com.chess.engine.moves.Move;
import com.chess.engine.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.chess.gui.GameWindow.*;
import static com.chess.gui.GameWindow.DEFAULT_PIECE_PATH;
import static com.chess.gui.utils.DimensionConstants.SCALE;
import static com.chess.gui.utils.DimensionConstants.TAKEN_PIECES_PANEL_DIMENSION;

/**
 * Represents the taken pieces GUI for each player's taken pieces. The panel will be divided into a north and south
 * panel: the north panel represents the player's pieces taken by the opponent and the south panel represents
 * the opponent's pieces taken by the player.
 *
 * @author Jamie Canada
 * @since 10/13/25
 */
public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel, southPanel;

    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    /**
     * Creates a TakenPiecesPanel object to display each player's captured pieces.
     */
    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);

        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);

        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel.setBackground(PANEL_COLOR);

        this.add(this.northPanel, BorderLayout.SOUTH);
        this.add(this.southPanel, BorderLayout.NORTH);

        this.setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
    }

    /**
     * Every non-moving piece gets placed in their current position.
     *
     * @param moveLog The log holding all the made moves.
     */
    public void redo(final MoveLog moveLog) {
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        for (final Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                String sizeFolder = "";
                if (SCALE <= 1.0f) {
                    sizeFolder = "/40px_40px/";
                } else {
                    sizeFolder = "/50px_50px/";
                }
                try {
                    String imagePath = DEFAULT_PIECE_PATH +
                                       sizeFolder +
                                       takenPiece.getPieceAlliance().toString().charAt(0) +
                                       takenPiece.getPieceType().toString() +
                                       ".png";
                    final BufferedImage takenPieceImage = ImageIO.read(new File(imagePath));
                    if (takenPiece.getPieceAlliance().isWhite()) {
                        southPanel.add(new JLabel(new ImageIcon(takenPieceImage)));
                    } else {
                        northPanel.add(new JLabel(new ImageIcon(takenPieceImage)));
                    }
                } catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        validate();
    }
}
