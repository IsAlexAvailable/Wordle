import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JPanel;

import Exceptions.DuplicateGuessException;
import Exceptions.InvalidWordException;

public class GamePanel extends JPanel {
    private PlayingPane gamePane;
    private BorderLayout layout;
    private GuessPanel guessPanel;
    private KeyTracker keyTracker;
    private WordManager wordManager;
    private char[][] guessesArr;
    private TileState[][] letterTileStates;
    private int guessRow;
    private ConcurrentLinkedQueue<Character> keyQueue;
    private int rows = Constants.MAXGUESSES;
    private int cols = Constants.MAXLETTERS;
    private String currGuess;
    private String wordleWord;
    private boolean hasWon;
    private boolean gameOver;

    public GamePanel(PlayingPane gamePane) {
        this.gamePane = gamePane;
        guessPanel = new GuessPanel();
        keyTracker = new KeyTracker();
        keyQueue = new ConcurrentLinkedQueue<>();
        initLayout();
        wordManager = new WordManager("words_alpha.txt");
        guessesArr = new char[rows][cols];
        letterTileStates = new TileState[rows][cols];
        guessRow = 0;
        wordleWord = wordManager.getWordleWord();
        System.out.println("Wordle word: " + wordleWord); //  THE MAGIC WORD
        currGuess = "";
        gameOver = false;
    }
    
    public void initLayout() {
        layout = new BorderLayout();
        setLayout(layout);
        add(guessPanel, BorderLayout.CENTER);
        setBackground(Constants.DARK_GREY);
        addKeyListener(keyTracker);
    }

    public void update() {
        Character inputChar = keyQueue.poll();
        if (inputChar != null && !gameOver) {    //  keystrokes waiting in queue
            switch (inputChar) {
                case '\n':  //  inputChar = ENTER
                    if (isWordGuessFull()) { 
                        guessWord(); } break;
                case '\b':  //  inputChar = BACKSPACE
                    removeLetter();
                    guessPanel.updateGuessTilesText(guessesArr[guessRow], guessRow);   //  update the after tile modified
                    break;
                default:    //  inputChar = 'A' - 'Z'
                    addLetter(inputChar);
                    guessPanel.updateGuessTilesText(guessesArr[guessRow], guessRow);   //  update the after tile modified
            }
        }
        if (currGuess.equals(wordleWord) && !gameOver) { 
            hasWon = true;
            gameOver = true;
            endGame(hasWon);
        }   //  won game, 
        else if (guessRow >= 6 && !gameOver) { 
            hasWon = false;
            gameOver = true;
            endGame(hasWon);
        }   //  lost game, end program
    }

    public boolean isWordGuessFull() {
        for (char c : guessesArr[guessRow]) {
            if (c == 0) { return false; }
        }
        return true;
    }

    public void guessWord() {
        currGuess = String.valueOf(guessesArr[guessRow]);  //  convert current guess array to string
        try {
            wordManager.addGuess(currGuess);
            updateTiles();
            guessRow += 1;
        } catch (InvalidWordException e) {
            System.out.println("Not a word.");
        } catch (DuplicateGuessException e) {
            System.out.println("Already guessed this word.");
        }
    }
    public void addLetter(char newLetter) {
        for (int i = 0; i < guessesArr[guessRow].length; i++) {
            if (guessesArr[guessRow][i] == 0) { //  if null character, replace with new letter
                guessesArr[guessRow][i] = newLetter; break;
            }
        }
    }

    public void removeLetter() {
        for (int i = guessesArr[guessRow].length-1; i >= 0; i--) {  //  finds and replaces the first null character, traversing backwards
            if (guessesArr[guessRow][i] != 0) { //  if not null character, set null character
                guessesArr[guessRow][i] = 0; break;
            }
        }
    }

    public void updateTiles() {
        wordManager.updateTileStates(guessesArr[guessRow], letterTileStates[guessRow]);
        guessPanel.updateGuessTilesColor(letterTileStates[guessRow], guessRow);
    }

    public void endGame(boolean hasWon) {
        gamePane.showEndPopup(hasWon);
    }

    public GuessPanel getGuessPanel() {
        return guessPanel;
    }
    class KeyTracker extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (c >= 'A' && c <= 'Z') { c += 32; }  //  convert to lowercase range
            if (c >= 'a' && c <= 'z') {
                keyQueue.add(c);
            }
            else if (c == '\b') {
                keyQueue.add(c);
            }
            else if (c == '\n') {
                keyQueue.add(c);
            }
        }
    }
}