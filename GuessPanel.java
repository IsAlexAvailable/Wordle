import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
        revalidate();
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


    public void updateGuessTilesText(char[] letters, int guessNumber) {
        for (int i = 0; i < letters.length; i++) {
            LetterTile tile = letterTiles[guessNumber][i];
            if (letters[i] == 0) {
                tile.setLetterText("");
            } else {
                tile.setLetterText("" + letters[i]);
            }
        }
    }

    public void updateGuessTilesColor(TileState[] letterStates, int guessNumber) {
        for (int i = 0; i < letterStates.length; i++) {
            LetterTile tile = letterTiles[guessNumber][i];
            tile.setLetterColor(letterStates[i]);
        }
    }
    
    private class LetterTile extends JLabel {
        public LetterTile() {
            setPreferredSize(new Dimension(trueTileSize, trueTileSize));
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setText("");
            setFont(new Font("Courier", Font.BOLD, 28));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(75,75,75), 3));
        }

        public void setLetterText(String letter) {
            setText(letter.toUpperCase());
        }

        public void setLetterColor(TileState state) {
            if (state == TileState.GREY) {
                setOpaque(false);
            }
            else if (state == TileState.GREEN) {
                setOpaque(true);
                setBackground(new Color(83,140,79));
            }
            else {
                setOpaque(true);
                setBackground(new Color(180,158,58));
            }
        }
    }
}
