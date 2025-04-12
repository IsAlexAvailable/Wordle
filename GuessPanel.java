import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GuessPanel extends JPanel {
    private GridBagLayout layout;
    private GridBagConstraints gbc;
    private LetterTile[][] letterTiles;
    private int tileWidth = Constants.SCALED_TILE_SIZE;
    private int rows = Constants.MAXGUESSES;
    private int cols = Constants.MAXLETTERS;

    public GuessPanel() {
        setPreferredSize(new Dimension(tileWidth*cols, tileWidth*rows));
        setOpaque(false);   //  guessPanel is transparent, if not, gamepanel background color is hidden
        setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        initPanelLayout();
        initGuessCells();
    }

    public void initPanelLayout() {
        layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);    //  space around grid cells
    }

    public void initGuessCells() {
        letterTiles = new LetterTile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                letterTiles[r][c] = new LetterTile(tileWidth, tileWidth);
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

    public void updateGuessTilesColor(TileColor[] letterColors, int guessNumber) {
        for (int i = 0; i < letterColors.length; i++) {
            LetterTile tile = letterTiles[guessNumber][i];
            tile.setTileColor(letterColors[i]);
        }
    }

    public LetterTile[] getLetterTiles(int guessNumber) {
        return letterTiles[guessNumber];
    }
}