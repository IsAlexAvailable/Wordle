import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class LetterTile extends JButton {
    private TileColor tileColor;

    public LetterTile(int width, int height) {
        tileColor = TileColor.BLACK;
        setPreferredSize(new Dimension(width, height));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setText("");
        setFont(new Font(Constants.FONT, Font.BOLD, 30));
        setForeground(Constants.WHITE);
        setTileColor(tileColor);
        setBorder(BorderFactory.createLineBorder(Constants.DARK_GREY,2));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(true);
    }

    public LetterTile(char c, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setText(("" + c).toUpperCase());
        setFont(new Font(Constants.FONT, Font.BOLD, 28));
        setForeground(Constants.WHITE);
        setBorder(null);
    }

    public void setTileText(String letter) {
        setText(letter.toUpperCase());
    }

    public void setTileColor(TileColor tileColor) {
        this.tileColor = tileColor;
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
            setBorder(BorderFactory.createLineBorder(Constants.GREEN,2));
        }
        else {
            setBackground(Constants.YELLOW);
            setBorder(BorderFactory.createLineBorder(Constants.YELLOW,2));
        }
    }

    public TileColor getTileColor() {
        return tileColor;
    }
}