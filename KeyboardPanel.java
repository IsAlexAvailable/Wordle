import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyboardPanel extends JPanel {
    private JButton[] letters;
    private HashMap<Character, KeyTile> keyTileMap;
    private GridBagLayout layout;
    private GridBagConstraints gbc;
    private int keyWidth;
    private int keyHeight;
    private int rows = 3;
    private int cols = 10;
    private ConcurrentLinkedQueue<Character> keyQueue;

    public KeyboardPanel(ConcurrentLinkedQueue<Character> keyQueue) {
        this.keyQueue = keyQueue;
        setPreferredSize(new Dimension(100, 450));
        setOpaque(false);   //  keyBoard panel is transparent, if not, gamepanel background color is hidden
        setBorder(BorderFactory.createEmptyBorder(0,0,240,0));
        initPanelLayout();
        initLetters();
        initLetterCells();
    }

    public void initLetters() {
        letters = new LetterTile[28];
        keyTileMap = new HashMap<>();
        char a = 'a';
        char currChar;
        keyWidth = Constants.SCALED_TILE_SIZE*2/3;
        keyHeight = Constants.SCALED_TILE_SIZE*7/8;
        KeyTile currKeyTile;
        int offset = 0;
        for (int i = 0; i < 28; i++) {
            if (i == 19) {
                currChar = '\n';
                currKeyTile = new KeyTile(currChar, keyWidth*3/2, keyHeight);
                keyTileMap.put(currChar, currKeyTile);
                offset--;
            } else if (i == 27) {
                currChar = '\b';
                currKeyTile = new KeyTile(currChar, keyWidth*3/2, keyHeight);
                keyTileMap.put(currChar, currKeyTile);   
            } else {
                currChar = (char) (a + offset);
                currKeyTile = new KeyTile(currChar, keyWidth, keyHeight);
                keyTileMap.put(currChar, currKeyTile);
            }
            letters[i] = currKeyTile;
            offset++;
        }
    }

    public void initPanelLayout() {
        layout = new GridBagLayout();
        setLayout(layout);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.NONE;
    }

    public void initLetterCells() {
        // TODO: ?????????????????
        // gbc.gridwidth = 1;
        // for (int j = 0; j < letters.length; j++) {
        //     if (j == 9 || j == 18) {
        //         gbc.gridwidth = GridBagConstraints.REMAINDER;
        //     }
        //     else {
        //         gbc.gridwidth = 1;
        //     }
        //     if (j == 0 || j == 10) {
        //         gbc.anchor = GridBagConstraints.EAST;
        //     } else if (j == 8 || j == 9 || j == 18) {
        //         gbc.anchor = GridBagConstraints.WEST;
        //     }
        //     add(letters[j], gbc);
        // }
        
        int i = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r != 0 && c == cols-1) {
                    break;
                }
                if (c == 0) {
                    gbc.anchor = GridBagConstraints.EAST;
                }
                else if (c == cols-2) {
                    gbc.anchor = GridBagConstraints.WEST;
                }
                else {
                    gbc.anchor = GridBagConstraints.CENTER;
                }
                gbc.gridx = c;
                gbc.gridy = r;
                add(letters[i], gbc);
                i++;
            }
        }
        
    }

    public void updateGuessTilesColor(LetterTile[] letterTiles) {
        char letter;
        TileColor letterColor;
        for (LetterTile tile : letterTiles) {
            letter = tile.getText().toLowerCase().charAt(0);
            letterColor = tile.getTileColor();
            LetterTile keyTile = keyTileMap.get(letter);
            TileColor keyTileColor = keyTile.getTileColor();
            if (keyTileColor == TileColor.GREEN || (keyTileColor == TileColor.YELLOW && letterColor != TileColor.GREEN)) {
                continue;
            }
            else if (letterColor == TileColor.GREEN) {
                keyTile.setTileColor(TileColor.GREEN);
            }
            else if (letterColor == TileColor.YELLOW) {
                keyTile.setTileColor(TileColor.YELLOW);
            }
            else {
                keyTile.setTileColor(TileColor.DARK_GREY);
            }
        }
    }

    private class KeyTile extends LetterTile {
        private char key;
        private int tileWidth;
        private int tileHeight;

        public KeyTile(char c, int width, int height) {
            super(c, width, height);
            tileWidth = width;
            tileHeight = height;
            key = c;
            setMinimumSize(new Dimension(width, height));
            setTileColor(TileColor.LIGHT_GREY);
            setFont(new Font(Constants.FONT, Font.BOLD, 18));
            if (c == '\n') {
                setFont(new Font(Constants.FONT, Font.BOLD, 14));
                setText("ENTER");
            } else if (c == '\b') {
                setFont(new Font(Constants.FONT, Font.BOLD, 14));
                setText("BACK");
            }

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setContentAreaFilled(false);
            setFocusPainted(false);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    keyQueue.add(key);

                }
            });
        }
        
        public void setTileColor(TileColor tileColor) {
            if (tileColor == TileColor.BLACK) {
                setBackground(Constants.BLACK);
            }
            else if (tileColor == TileColor.DARK_GREY) {  
                setBackground(Constants.DARK_GREY);
            }
            else if (tileColor == TileColor.LIGHT_GREY) {
                setBackground(Constants.LIGHT_GREY);
            }
            else if (tileColor == TileColor.GREEN) {
                setBackground(Constants.GREEN);
            }
            else {
                setBackground(Constants.YELLOW);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, tileWidth, tileHeight,8,8);
            super.paintComponent(g);
            g2d.dispose();
        }
    }
}