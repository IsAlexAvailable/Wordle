import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.Dimension;

public class Wordle extends JFrame {
    private CardLayout cardLayout;
    private MenuPanel menuPanel;
    private PlayingPane gamePane;
    private boolean gameRunning;
    private boolean gamePaused;

    public Wordle() {
        initWindow();
        gameRunning = true;
        gamePaused = true;
        resumeGame();
    }

    public void initWindow() {
        int minWindowWidth = Constants.MINIMUM_WIDTH;
        int minWindowHeight = Constants.MINIMUM_HEIGHT;

        setTitle("Wordle");
        setPreferredSize(new Dimension(minWindowWidth*2, minWindowHeight*2));  //  TODO determine dimensions
        setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuPanel = new MenuPanel();
        gamePane = new PlayingPane(this);
        
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        getContentPane().add(Constants.MENU_NAME, menuPanel);
        getContentPane().add(Constants.GAME_NAME, gamePane);
        setLayout(cardLayout);

        pack();
        setVisible(true);
    }

    public void runGameLoop() {
        while (gameRunning) {
            if (!gamePaused) {
                gamePane.update();
            } else {
                menuPanel.update();
            }
            repaint();
        }
    }

    public void pauseGame() {
        cardLayout.show(getContentPane(), Constants.MENU_NAME);
        menuPanel.requestFocusInWindow();
        gamePaused = true;
    }

    public void resumeGame() {
        cardLayout.show(getContentPane(), Constants.GAME_NAME);
        gamePane.requestFocusInWindow();
        gamePaused = false;
    }

    public static void main(String[] args) {
        Wordle wordle = new Wordle();
        wordle.runGameLoop();
    }
}