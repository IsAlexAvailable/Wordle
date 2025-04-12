import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopupPanel extends JPanel {
    private PlayingPane playingPane;
    private int popupType;
    private String wordleWord;
    private int width;
    private int height;
    private JPanel headerPanel;
    private JLabel headerText;
    private JLabel correctWordText;
    private JPanel buttonPanel;
    private EndGameButton playAgainButton;
    private EndGameButton exitButton;

    public PopupPanel(int popupType, String wordleWord, PlayingPane playingPane) {
        this.playingPane = playingPane;
        if (popupType != Constants.WIN_POPUP_LAYER && popupType != Constants.LOSE_POPUP_LAYER) {
            throw new IllegalArgumentException("Popup type int must be in range " + Constants.WIN_POPUP_LAYER + " - " + Constants.LOSE_POPUP_LAYER);
        }
        this.popupType = popupType;
        this.wordleWord = wordleWord;
        width = Constants.POPUP_WIDTH;
        height = Constants.POPUP_HEIGHT;
        setLayout(new BorderLayout());
        setBackground(Constants.BLACK);
        setBorder(BorderFactory.createLineBorder(Constants.DARK_GREY, 3));

        initHeader();
        initButtonPanel();
    }

    public void initHeader() {
        if (popupType == Constants.WIN_POPUP_LAYER) {
            headerText = new JLabel(Constants.WIN_TEXT);
        }
        else {
            headerText = new JLabel(Constants.LOSE_TEXT);
            correctWordText = new JLabel("The word was: " + wordleWord);
            correctWordText.setAlignmentX(Component.CENTER_ALIGNMENT);
            correctWordText.setFont(new Font(Constants.FONT, Font.BOLD, 16));
            correctWordText.setForeground(Constants.WHITE);
        }
        headerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerText.setFont(new Font(Constants.FONT, Font.BOLD, 20));
        headerText.setForeground(Constants.WHITE);
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(Box.createVerticalStrut(25));
        headerPanel.add(headerText);
        if (popupType == Constants.LOSE_POPUP_LAYER) {
            headerPanel.add(Box.createVerticalStrut(5));
            headerPanel.add(correctWordText);
        }
        headerPanel.add(Box.createVerticalStrut(15));
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    public void initButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        playAgainButton = new EndGameButton(Constants.PLAY_AGAIN);
        playAgainButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playingPane.newGame();
            }
         });

        exitButton = new EndGameButton(Constants.EXIT);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(Box.createVerticalStrut(50));
        buttonPanel.add(playAgainButton);
        buttonPanel.add(Box.createVerticalStrut(50));
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.CENTER);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void activate(boolean b) {
        setEnabled(b);
        setVisible(b);
    }

    private class EndGameButton extends JButton {
        public EndGameButton(String title) {
            super(title);
            setFont(new Font(Constants.FONT, Font.BOLD, 14));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setMaximumSize(new Dimension(175, 50));
            setPreferredSize(new Dimension(175, 50));
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        public void paintComponent(Graphics g) {
            
            //  draw text bubble
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Constants.GREEN);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            
            //  draw text
            g2d.setColor(Constants.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int width = fm.stringWidth(text);
            int height = fm.getHeight();
            int x = (getWidth()-width)/2;
            int y = (getHeight()-height)/2+14;
            g2d.drawString(text, x, y);

            g2d.dispose();
        }
    }
}