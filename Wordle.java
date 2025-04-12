import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Wordle extends JFrame {
    private CardLayout cardLayout;
    private MenuPanel menuPanel;
    private PlayingPane playingPane;
    private boolean gameRunning;
    private boolean gamePaused;

    public Wordle() {
        initWindow();
        gameRunning = true;
        gamePaused = false;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                playingPane.resize();
            }
        });
        resumeGame();
    }

    public void initWindow() {
        int minWindowWidth = Constants.MINIMUM_WIDTH;
        int minWindowHeight = Constants.MINIMUM_HEIGHT;
        int windowWidth = Constants.SCREEN_WIDTH;
        int windowHeight = Constants.SCREEN_HEIGHT;

        setTitle("Wordle");
        setPreferredSize(new Dimension(windowWidth, windowHeight));  //  TODO determine dimensions
        setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuPanel = new MenuPanel();
        playingPane = new PlayingPane(this);
        
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        getContentPane().add(Constants.MENU_NAME, menuPanel);
        getContentPane().add(Constants.GAME_NAME, playingPane);
        setLayout(cardLayout);

        pack();
        setVisible(true);
    }

    public void runGameLoop() {
        while (gameRunning) {
            if (!gamePaused) {
                playingPane.update();
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
        playingPane.requestFocusInWindow();
        gamePaused = false;
    }

    public static void main(String[] args) {
        Wordle wordle = new Wordle();
        wordle.runGameLoop();
    }
}