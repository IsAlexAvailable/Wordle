import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import Exceptions.DuplicateGuessException;
import Exceptions.InvalidWordException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordManager {
    private String filePath;
    private Collection<String> wordSet;
    private Collection<String> guessSet;
    private HashMap<Character, Integer> wordleWordLetterCount;
    private BufferedReader reader;
    private String wordleWord;
    private char[] wordleWordLetters;
    private int cols = Constants.MAXLETTERS;
    
    public WordManager(String filePath) {
        this.filePath = filePath;
        initReader();
        populateWordSet();
        generateWordleWord();
        wordleWordLetters = wordleWord.toCharArray();
        guessSet = new HashSet<>(cols);
        populateWordleWordLetterCountMap();
    }

    public void initReader() {
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }

    public void populateWordSet() {
        wordSet = new HashSet<>();

        //  read file
        try {
            String line = reader.readLine();
            while (line != null) {
                if (line.length() == cols) {
                    wordSet.add(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateWordleWordLetterCountMap() {
        wordleWordLetterCount = new HashMap<>(5);
        for (int i = 0; i < wordleWord.length(); i++) {
            char letter = wordleWord.charAt(i);
            if (wordleWordLetterCount.containsKey(letter)) {
                int count = wordleWordLetterCount.get(letter);
                wordleWordLetterCount.replace(letter, count, count+1);
            } else {
                wordleWordLetterCount.put(letter, 1);
            }
        }
    }

    public boolean isValidWord(String word) {  
        if (!wordSet.contains(word)) {
            throw new InvalidWordException();
        } 
        if (guessSet.contains(word)) {
            throw new DuplicateGuessException();
        }
        return true;
    }

    public void addGuess(String s) {
        if (isValidWord(s)) {
            guessSet.add(s);
        }
    }

    public void generateWordleWord() {
        Random randomInt = new Random();
        int randomUpperBound = randomInt.nextInt(1, wordSet.size());
        Iterator<String> words = wordSet.iterator();

        for (int i = 0; i < randomUpperBound; i++) {
            if (i == randomUpperBound-1) {
                wordleWord = words.next();
                break;
            }
            words.next();
        }
    }

    public void updateTileStates(char[] wordGuess, TileColor[] letterStates) {
        HashMap<Character, Integer> guessWordLetterCount = new HashMap<>(5);

        //  initial pass to test letter absence and direct matches
        for (int i = 0; i < wordGuess.length; i++) {
            char currLetter = wordGuess[i];
            if (!isInWord(currLetter)) {
                letterStates[i] = TileColor.DARK_GREY;
            }
            else if (isDirectMatch(currLetter, i)) {
                letterStates[i] = TileColor.GREEN;
                updateLetterCounts(currLetter, guessWordLetterCount);
            }
        }

        for (int i = 0; i < wordGuess.length; i++) {
            char currLetter = wordGuess[i];
            if (letterStates[i] == TileColor.GREEN || letterStates[i] == TileColor.DARK_GREY) {  //  already set
                continue;
            }
            updateLetterCounts(currLetter, guessWordLetterCount);
            if (letterStates[i] != TileColor.GREEN && shouldSetStateYellow(currLetter, i, guessWordLetterCount)) {
                letterStates[i] = TileColor.YELLOW;
            }
            else {  //  although letter occurs, no more spots that aren't already filled
                letterStates[i] = TileColor.DARK_GREY;
            }
        }
    }

    public boolean shouldSetStateYellow(char letter, int index, HashMap<Character, Integer> guessWordLetterCount) {
        if (guessWordLetterCount.get(letter) > wordleWordLetterCount.get(letter)) {
            return false;
        }
        return true;
    }

    public void updateLetterCounts(char letter, HashMap<Character, Integer> guessWordLetterCount) {
        if (!guessWordLetterCount.containsKey(letter)) {
            guessWordLetterCount.put(letter, 1);
        } else {
            int count = guessWordLetterCount.get(letter);
            guessWordLetterCount.replace(letter, count, count+1);
        }
    }

    public boolean isInWord(char letter) {
        for (char l : wordleWordLetters) {
            if (l == letter) { return true;}
        }
        return false;
    }

    public boolean isDirectMatch(char letter, int index) {
        return letter == wordleWordLetters[index];
    }

    public String getWordleWord() {
        return wordleWord;
    }
}