import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuessPanel extends JPanel {
    private GridBagLayout layout;
    private GridBagConstraints gbc;
    private LetterTile[][] letterTiles;
    private int trueTileSize = Constants.SCALED_TILE_SIZE;

    public GuessPanel() {
        initLayout();
        letterTiles = new LetterTile[6][5];
        initCells();
        setPreferredSize(new Dimension(trueTileSize*5, trueTileSize*6));
        setOpaque(false);   //  guessPanel is transparent, if not, gamepanel background color is hidden
    }

    public void initLayout() {
        layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);    //  space around grid cells
    }

    public void initCells() {
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                letterTiles[r][c] = new LetterTile();
                gbc.gridx = c;  //  specify cell posX
                gbc.gridy = r;  //  specify cell posY
                add(letterTiles[r][c], gbc);
            }
        }
    }

    private class LetterTile extends JLabel {
        public LetterTile() {
            setPreferredSize(new Dimension(trueTileSize, trueTileSize));
            setText("");
            setFont(new Font("Courier", Font.BOLD, 28));
            setForeground(Color.WHITE);
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createLineBorder(new Color(75,75,75), 3));
        }

        public void setText(String letter) {
            setText(letter);
        }
    }

}
