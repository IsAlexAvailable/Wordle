import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Dimension;

public class Wordle extends JFrame {
    private CardLayout cardLayout;
    private MenuPanel menu;
    private GamePanel game;
    private boolean gameRunning;
    private boolean gamePaused;

    public Wordle() {
        menu = new MenuPanel();
        game = new GamePanel();
        initWindow();
        gameRunning = true;
        gamePaused = true;
        resumeGame();
    }

    public void initWindow() {
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);
        getContentPane().add(Constants.MENU_NAME, menu);
        getContentPane().add(Constants.GAME_NAME, game);
        setLayout(cardLayout);

        setTitle("Wordle");
        setPreferredSize(new Dimension(1000, 1000));  //  TODO determine dimensions
        setMinimumSize(new Dimension(500, 500));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void runGameLoop() {
        while (gameRunning) {
            if (!gamePaused) {
                game.update();
            } else {
                menu.update();
            }
            repaint();
        }
    }

    public void pauseGame() {
        cardLayout.show(getContentPane(), Constants.MENU_NAME);
        menu.requestFocusInWindow();
        gamePaused = true;
    }

    public void resumeGame() {
        cardLayout.show(getContentPane(), Constants.GAME_NAME);
        game.requestFocusInWindow();
        gamePaused = false;
    }

    public static void main(String[] args) {
        Wordle wordle = new Wordle();
        wordle.runGameLoop();
    }
}