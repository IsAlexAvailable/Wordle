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
    private KeyboardPanel keyboardPanel;
    private KeyTracker keyTracker;
    private WordManager wordManager;
    private char[][] letterGuessChars;
    private TileColor[][] letterGuessColors;
    private int guessNumber;
    private ConcurrentLinkedQueue<Character> keyQueue;
    private int rows = Constants.MAXGUESSES;
    private int cols = Constants.MAXLETTERS;
    private String currGuess;
    private String wordleWord;
    private boolean hasWon;

    public GamePanel(PlayingPane gamePane) {
        this.gamePane = gamePane;
        guessPanel = new GuessPanel();
        keyQueue = new ConcurrentLinkedQueue<>();
        keyboardPanel = new KeyboardPanel(keyQueue);
        initPanelLayout();
        wordManager = new WordManager("wordle_words_nyt.txt");
        wordleWord = wordManager.getWordleWord();
        letterGuessChars = new char[rows][cols];    //  TODO: should this be a stack instead? fixed length but stack behaviour
        letterGuessColors = new TileColor[rows][cols];
        guessNumber = 0;
        currGuess = "";

        System.out.println("Wordle word: " + wordleWord); //  THE MAGIC WORD
    }
    
    public void initPanelLayout() {
        layout = new BorderLayout();
        setLayout(layout);
        add(guessPanel, BorderLayout.CENTER);
        add(keyboardPanel, BorderLayout.SOUTH);
        setBackground(Constants.BLACK);
        keyTracker = new KeyTracker();
        addKeyListener(keyTracker);
    }

    public void update() {
        Character inputChar = keyQueue.poll();
        if (inputChar != null) {    //  keystrokes waiting in queue
            switch (inputChar) {
                case '\n':  //  inputChar = ENTER
                    if (isWordGuessFull()) { 
                        guessWord();
                    } 
                    break;
                case '\b':  //  inputChar = BACKSPACE
                    if (removeLetter()) {
                        guessPanel.updateGuessTilesText(letterGuessChars[guessNumber], guessNumber);   //  update the after tile modified
                    }
                    break;
                default:    //  inputChar = 'a' - 'z'
                    if (addLetter(inputChar)) {
                        guessPanel.updateGuessTilesText(letterGuessChars[guessNumber], guessNumber);   //  update the after tile modified
                    }
            }
        }
        if (currGuess.equals(wordleWord)) { 
            hasWon = true;
            endGame(hasWon);
        }   //  won game, 
        else if (guessNumber >= 6) { 
            hasWon = false;
            endGame(hasWon);
        }   //  lost game, end program
    }

    public boolean isWordGuessFull() {
        for (char c : letterGuessChars[guessNumber]) {
            if (c == 0) { return false; }
        }
        return true;
    }

    public void guessWord() {
        currGuess = String.valueOf(letterGuessChars[guessNumber]);  //  convert current guess array to string
        System.out.println(currGuess);
        try {
            wordManager.addGuess(currGuess);
            updateTiles();
            updateKeyboard();
            guessNumber += 1;
        } catch (InvalidWordException e) {
            System.out.println("Not a word.");
        } catch (DuplicateGuessException e) {
            System.out.println("Already guessed this word.");
        }
    }

    public boolean addLetter(char newLetter) {
        for (int i = 0; i < letterGuessChars[guessNumber].length; i++) {
            if (letterGuessChars[guessNumber][i] == 0) { //  if null character, replace with new letter
                letterGuessChars[guessNumber][i] = newLetter;
                return true;
            }
        }
        return false;
    }

    public boolean removeLetter() {
        for (int i = letterGuessChars[guessNumber].length-1; i >= 0; i--) {  //  finds and replaces the first null character, traversing guessrow backwards
            if (letterGuessChars[guessNumber][i] != 0) { //  if not null character, set to null character                
                letterGuessChars[guessNumber][i] = 0;
                return true;
            }
        }
        return false;
    }

    public void updateTiles() {
        wordManager.updateTileStates(letterGuessChars[guessNumber], letterGuessColors[guessNumber]);
        guessPanel.updateGuessTilesColor(letterGuessColors[guessNumber], guessNumber);
    }

    public void updateKeyboard() {
        LetterTile[] letterTiles = guessPanel.getLetterTiles(guessNumber);
        keyboardPanel.updateGuessTilesColor(letterTiles);     
    }

    public void endGame(boolean hasWon) {
        gamePane.endGame();
        gamePane.showEndPopup(hasWon);
    }

    public String getWordleWord() {
        return wordleWord;
    }

    class KeyTracker extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (c >= 'A' && c <= 'Z') { c += 32; }  //  convert to lowercase range
            if (c >= 'a' && c <= 'z') {
                keyQueue.add(c);
            }
            else if (c == '\b') {   //  entered backspace
                keyQueue.add(c);
            }
            else if (c == '\n') {   //  entered enter
                keyQueue.add(c);
            }
        }
    }
}