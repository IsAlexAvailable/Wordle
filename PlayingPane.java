import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class PlayingPane extends JLayeredPane {
    private GamePanel game;
    private PopupPanel winPopup;
    private PopupPanel losePopup;
    private JFrame frame;

    public PlayingPane(JFrame frame) {
        this.frame = frame;
        initGamePanel();
        initPopupPanels();
    }

    public void initGamePanel() {
        game = new GamePanel(this);
        game.setBounds(new Rectangle(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        add(game, Constants.GAME_LAYER);
    }

    public void initPopupPanels() {
        winPopup = new PopupPanel(Constants.WIN_POPUP_LAYER, this);
        resizeComponent(winPopup, (frameWidth-winPopup.getWidth())/2, (frameHeight-winPopup.getHeight())/2, winPopup.getWidth(), winPopup.getHeight());
        losePopup = new PopupPanel(Constants.LOSE_POPUP_LAYER, this);
        resizeComponent(losePopup, (frameWidth-losePopup.getWidth())/2, (frameHeight-losePopup.getHeight())/2, losePopup.getWidth(), losePopup.getHeight());
        add(winPopup, Constants.WIN_POPUP_LAYER);
        add(losePopup, Constants.LOSE_POPUP_LAYER);
        winPopup.setVisible(false);
        losePopup.setVisible(false);
    }

    private int frameWidth;
    private int frameHeight;

    public void update() {
        updateFrameSize();
        resizeComponent(game, 0, 0, frameWidth, frameHeight);
        if (game.isEnabled()) {
            game.requestFocusInWindow();
            game.update();
        } else if (winPopup.isEnabled()) {
            resizeComponent(winPopup, (frameWidth-winPopup.getWidth())/2, (frameHeight-winPopup.getHeight())/2, winPopup.getWidth(), winPopup.getHeight());
            winPopup.requestFocusInWindow();
        } else if (losePopup.isEnabled()) {
            resizeComponent(losePopup, (frameWidth-losePopup.getWidth())/2, (frameHeight-losePopup.getHeight())/2, losePopup.getWidth(), losePopup.getHeight());
            losePopup.requestFocusInWindow();
        }
    }

    public void updateFrameSize() {
        frameWidth = frame.getWidth();
        frameHeight = frame.getHeight();
    }

    public void resizeComponent(JComponent component, int x, int y, int width, int height) {
        component.setBounds(new Rectangle(x, y, width, height));
    }

    public void showGame() {
        moveToFront(game);
        game.setEnabled(true);
        winPopup.setEnabled(false);
        losePopup.setEnabled(false);
        winPopup.setVisible(false);
        losePopup.setVisible(false);
    }

    public void showEndPopup(boolean hasWon) {
        game.setEnabled(false);
        if (hasWon) {
            moveToFront(winPopup);
            winPopup.setEnabled(true);
            winPopup.setVisible(true);
        } else {
            moveToFront(losePopup);
            losePopup.setEnabled(true);
            losePopup.setVisible(true);
        }
    }

    public void newGame() { //  remove current game from playing pane and add new one
        remove(game);
        game = new GamePanel(this);
        game.setBounds(new Rectangle(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        add(game, Constants.GAME_LAYER);
        showGame();
    }
}