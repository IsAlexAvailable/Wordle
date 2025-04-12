import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyboardPanel extends JPanel {
    private JButton[] letters;
    private HashMap<Character, KeyTile> keyTileMap;
    private BoxLayout layout;
    private JPanel row1;
    private JPanel row2;
    private JPanel row3;
    private int keyWidth;
    private int keyHeight;
    private ConcurrentLinkedQueue<Character> keyQueue;

    public KeyboardPanel(ConcurrentLinkedQueue<Character> keyQueue) {
        this.keyQueue = keyQueue;
        setPreferredSize(new Dimension(100, 450));
        setOpaque(false);   //  keyBoard panel is transparent, if not, gamepanel background color is hidden
        setBorder(BorderFactory.createEmptyBorder(0,0,240,0));  //  push the keyboard up towards guess panel
        initPanelLayout();
        initLetters();
        initKeyRowCells();
    }

    public void initLetters() {
        letters = new LetterTile[28];
        keyTileMap = new HashMap<>();
        String keys = "qwertyuiopasdfghjkl\nzxcvbnm\b";
        char[] keyArr = keys.toCharArray();
        char currChar;
        keyWidth = Constants.SCALED_TILE_SIZE*2/3;
        keyHeight = Constants.SCALED_TILE_SIZE*7/8;
        KeyTile currKeyTile;

        for (int i = 0; i < keyArr.length; i++) {
            currChar = keyArr[i];
            if (currChar == '\n' || currChar == '\b') {
                currKeyTile = new KeyTile(currChar, keyWidth*3/2, keyHeight);
            } else {
                currKeyTile = new KeyTile(currChar, keyWidth, keyHeight);
            }
            keyTileMap.put(currChar, currKeyTile);
            letters[i] = currKeyTile;
        }
    }

    public void initPanelLayout() {
        layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        row1 = getNewKeyRow();
        row2 = getNewKeyRow();
        row3 = getNewKeyRow();
    }

    public JPanel getNewKeyRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        row.setMaximumSize(new Dimension(500, 63));
        row.setPreferredSize(new Dimension(500, 63));
        row.setOpaque(false);
        return row;
    }

    public void initKeyRowCells() {
        int i;
        int j;
        for (i = 0 ; i < 10; i++) {
            row1.add(letters[i]);
        }
        for (j = 0; j < 9; j++) {
            row2.add(letters[j+i]);
        }
        for (int k = 0; k < 9; k++) {
            row3.add(letters[k+i+j]);
        }
        add(row1);
        add(row2);
        add(row3);
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
            setMinimumSize(new Dimension(tileWidth, tileHeight));
            setTileColor(TileColor.LIGHT_GREY);
            initText();
            addActionListener(new ActionListener() {    //  clicking this adds key to tasks
                @Override
                public void actionPerformed(ActionEvent e) {
                    keyQueue.add(key);
                }
            });

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setContentAreaFilled(false);
            setFocusPainted(false);
        }
        
        public void initText() {
            setFont(new Font(Constants.FONT, Font.BOLD, 18));
            if (key == '\n') {
                setFont(new Font(Constants.FONT, Font.BOLD, 14));
                setText("ENTER");
            } else if (key == '\b') {
                setFont(new Font(Constants.FONT, Font.BOLD, 14));
                setText("BACK");
            }
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
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);   //  prevent aliasing on tile's curved corners
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, tileWidth, tileHeight,8,8);
            super.paintComponent(g);
            g2d.dispose();
        }
    }
}