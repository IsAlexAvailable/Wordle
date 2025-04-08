import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private BorderLayout layout;
    private GuessPanel guessPanel;
    private KeyTracker keyTracker;
    private WordManager wordManager;
    private char[][] letterGuesses;
    private TileState[][] letterTileStates;
    private int guessNumber;
    private ConcurrentLinkedQueue<Character> keyQueue;


    public GamePanel() {
        guessPanel = new GuessPanel();
        keyTracker = new KeyTracker();
        keyQueue = new ConcurrentLinkedQueue<>();
        initLayout();
        wordManager = new WordManager("words_alpha.txt");
        letterGuesses = new char[6][5];
        letterTileStates = new TileState[6][5];
        guessNumber = 0;
    }
    
    public void initLayout() {
        layout = new BorderLayout();
        setLayout(layout);
        add(guessPanel, BorderLayout.CENTER);
        setBackground(new Color(30,30,30));
        addKeyListener(keyTracker);
    }

    public void update() {
        Character inputChar = keyQueue.poll();
        if (inputChar != null) {    //  keystrokes waiting in queue
            switch (inputChar) {
                case '\n':  //  inputChar = ENTER
                    if (isWordGuessFull()) { 
                        guessWord(); } break;
                case '\b':  //  inputChar = BACKSPACE
                    removeLetter(); break;
                default:    //  inputChar = 'A' - 'Z'
                    addLetter(inputChar);
            }
            if (guessNumber >= 6) { System.exit(0); }   //  exit program
            guessPanel.updateGuessTilesText(letterGuesses[guessNumber], guessNumber);   //  update the gui at current guess using current letters
        }
    }

    public boolean isWordGuessFull() {
        for (char c : letterGuesses[guessNumber]) {
            if (c == 0) { return false; }
        }
        return true;
    }

    public void guessWord() {
        String guess = String.valueOf(letterGuesses[guessNumber]);
        try {
            wordManager.addGuess(guess);
            updateGuessInformation();
        } catch (InvalidWordException e) {
            System.out.println("Not a word.");
        } catch (DuplicateGuessException e) {
            System.out.println("Already guessed this word.");
        }
    }
    public void addLetter(char newLetter) {
        for (int i = 0; i < letterGuesses[guessNumber].length; i++) {
            if (letterGuesses[guessNumber][i] == 0) {
                letterGuesses[guessNumber][i] = newLetter; break;
            }
        }
    }

    public void removeLetter() {
        for (int i = letterGuesses[guessNumber].length-1; i >= 0; i--) {
            if (letterGuesses[guessNumber][i] != 0) {
                letterGuesses[guessNumber][i] = 0; break;
            }
        }
    }

    public void updateGuessInformation() {  //  TODO: give better name
        wordManager.updateTileStates(letterGuesses[guessNumber], letterTileStates[guessNumber]);
        guessPanel.updateGuessTilesColor(letterTileStates[guessNumber], guessNumber);
        guessNumber += 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    //  very necessary
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