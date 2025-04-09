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
    private int rows = Constants.MAXGUESSES;
    private int cols = Constants.MAXLETTERS;

    public GuessPanel() {
        initLayout();
        letterTiles = new LetterTile[rows][cols];
        initCells();
        setPreferredSize(new Dimension(trueTileSize*cols, trueTileSize*rows));
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
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
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
                tile.setTileText("");
            } else {
                tile.setTileText("" + letters[i]);
            }
        }
    }

    public void updateGuessTilesColor(TileState[] letterStates, int guessNumber) {
        for (int i = 0; i < letterStates.length; i++) {
            LetterTile tile = letterTiles[guessNumber][i];
            tile.setTileColor(letterStates[i]);
        }
    }
    
    private class LetterTile extends JLabel {
        public LetterTile() {
            setPreferredSize(new Dimension(trueTileSize, trueTileSize));
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setText("");
            setFont(new Font(Constants.FONT, Font.BOLD, 28));
            setForeground(Constants.WHITE);
            setBorder(BorderFactory.createLineBorder(Constants.LIGHT_GREY, 3));
        }

        public void setTileText(String letter) {
            setText(letter.toUpperCase());
        }

        public void setTileColor(TileState state) {
            if (state == TileState.GREY) {  
                setOpaque(false);   //  maintains the background panel color
            }
            else if (state == TileState.GREEN) {
                setOpaque(true);    //  sets its own color to green separate from the background
                setBackground(Constants.GREEN);
            }
            else {
                setOpaque(true);    //  sets its own color to yellow separate from the background
                setBackground(Constants.YELLOW);
            }
        }
    }
}