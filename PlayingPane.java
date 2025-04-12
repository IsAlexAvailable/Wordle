import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class PlayingPane extends JLayeredPane {
    private GamePanel game;
    private PopupPanel winPopup;
    private PopupPanel losePopup;
    private JFrame frame;
    private int frameWidth;
    private int frameHeight;
    private boolean gameOver = false;
    private String wordleWord;

    public PlayingPane(JFrame frame) {
        this.frame = frame;
        initGamePanel();
        initPopupPanels();
    }

    public void initGamePanel() {
        game = new GamePanel(this);
        wordleWord = game.getWordleWord();
        updateFrameSize();
        game.setBounds(0, 0, frameWidth, frameHeight);
        add(game, Constants.GAME_LAYER);
    }

    public void initPopupPanels() {
        winPopup = new PopupPanel(Constants.WIN_POPUP_LAYER, null, this);
        losePopup = new PopupPanel(Constants.LOSE_POPUP_LAYER, wordleWord, this);
        add(winPopup, Constants.WIN_POPUP_LAYER);
        add(losePopup, Constants.LOSE_POPUP_LAYER);
        winPopup.setVisible(false);
        losePopup.setVisible(false);
    }
    
    public void update() {
        if (game.isEnabled() && !gameOver) {
            game.update();
            game.requestFocusInWindow();
        }
    }

    public void updateFrameSize() {
        frameWidth = frame.getWidth();
        frameHeight = frame.getHeight();
    }

    public void resize() {  //  FIXME: shitty results
        updateFrameSize();
        game.setBounds(0, 0, frameWidth, frameHeight);
        winPopup.setBounds((frameWidth-winPopup.getWidth())/2, (frameHeight-winPopup.getHeight())/2, winPopup.getWidth(), winPopup.getHeight());
        losePopup.setBounds((frameWidth-losePopup.getWidth())/2, (frameHeight-losePopup.getHeight())/2, losePopup.getWidth(), losePopup.getHeight());
        revalidate();
        repaint();
    }

    public void showGame() {
        winPopup.activate(false);
        losePopup.activate(false);        
        game.setEnabled(true);
        moveToFront(game);
    }

    public void showEndPopup(boolean hasWon) {
        game.setEnabled(false);
        if (hasWon) {
            losePopup.activate(false);
            winPopup.activate(true);
            moveToFront(winPopup);
        } else {
            winPopup.activate(false);
            losePopup.activate(true);
            moveToFront(losePopup);
        }
    }

    public void endGame() {
        gameOver = true;
    }

    public void newGame() { //  remove current game from playing pane and add new one
        remove(game);
        game = new GamePanel(this);
        add(game, Constants.GAME_LAYER);
        gameOver = false;
        resize();
        showGame();
    }
}