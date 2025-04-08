import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordManager {
    private String filePath;
    private Collection<String> wordSet;
    private Collection<String> guessSet;
    private HashMap<Character, Integer> letterOccurences;
    private BufferedReader reader;
    private String uniqueWord;
    private char[] uniqueWordLetters;
    
    public WordManager(String filePath) {
        this.filePath = filePath;
        initReader();
        populateWordSet();
        generateUniqueWord();
        uniqueWordLetters = uniqueWord.toCharArray();
        guessSet = new HashSet<>(5);
        populateLetterOccurencesMap();
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
                if (line.length() == 5) {
                    wordSet.add(line);
                    System.out.println(line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateLetterOccurencesMap() {
        letterOccurences = new HashMap<>(5);
        for (int i = 0; i < uniqueWord.length(); i++) {
            char letter = uniqueWord.charAt(i);
            if (letterOccurences.containsKey(letter)) {
                int occurence = letterOccurences.get(letter);
                letterOccurences.replace(letter, occurence, occurence+1);
            } else {
                letterOccurences.put(letter, 1);
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

    public void generateUniqueWord() {
        Random randomInt = new Random();
        int randomUpperBound = randomInt.nextInt(1, wordSet.size());
        Iterator<String> words = wordSet.iterator();

        for (int i = 0; i < randomUpperBound; i++) {
            if (i == randomUpperBound-1) {
                uniqueWord = words.next();
                break;
            }
            words.next();
        }
    }

    public void updateTileStates(char[] letterGuesses, TileState[] letterStates) {
        HashMap<Character, Integer> letterCounts = new HashMap<>(5);

        //  initial pass to test letter absence and direct matches
        for (int i = 0; i < letterGuesses.length; i++) {
            char currLetter = letterGuesses[i];
            if (!isInWord(currLetter)) {
                letterStates[i] = TileState.GREY;
            }
            else if (isDirectMatch(currLetter, i)) {
                letterStates[i] = TileState.GREEN;
                updateLetterCounts(currLetter, letterCounts);
            }
        }

        for (int i = 0; i < letterGuesses.length; i++) {
            char currLetter = letterGuesses[i];
            if (letterStates[i] == TileState.GREEN || letterStates[i] == TileState.GREY) {  //  already set
                continue;
            }
            updateLetterCounts(currLetter, letterCounts);
            if (letterStates[i] != TileState.GREEN && shouldSetStateYellow(currLetter, i, letterCounts)) {
                letterStates[i] = TileState.YELLOW;
            }
            else {  //  although letter occurs, no more spots that aren't already filled
                letterStates[i] = TileState.GREY;
            }
        }
    }

    public boolean shouldSetStateYellow(char letter, int index, HashMap<Character, Integer> letterCounts) {
        if (letterCounts.get(letter) > letterOccurences.get(letter)) {
            return false;
        }
        return true;
    }

    public void updateLetterCounts(char letter, HashMap<Character, Integer> letterCounts) {
        if (!letterCounts.containsKey(letter)) {
            letterCounts.put(letter, 1);
        } else {
            int value = letterCounts.get(letter);
            letterCounts.replace(letter, value, value+1);
        }
    }

    public boolean isInWord(char letter) {
        for (char l : uniqueWordLetters) {
            if (l == letter) { return true;}
        }
        return false;
    }

    public boolean isDirectMatch(char letter, int index) {
        return letter == uniqueWordLetters[index];
    }

    public String getUniqueWord() {
        return uniqueWord;
    }
}