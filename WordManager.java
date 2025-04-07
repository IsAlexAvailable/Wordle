import java.util.Collection;
import java.util.HashSet;
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
    private BufferedReader reader;
    private String uniqueWord;
    
    public WordManager(String filePath) {
        this.filePath = filePath;
        initReader();
        populateWordSet();
        generateUniqueWord();
        guessSet = new HashSet<>(5);
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
                wordSet.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidWord(String word) {   //  TODO: implement getting error code corresponding to invalid guess
        return wordSet.contains(word) && !guessSet.contains(word);
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

    public String getUniqueWord() {
        return uniqueWord;
    }
}
