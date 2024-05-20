package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WildcardSearchHandler {
    public static List<String> performWildcardSearch(String pattern) {
        List<String> searchResults = new ArrayList<>();

        // Read the file and search for words matching the pattern
        try (BufferedReader fileReader = new BufferedReader(new FileReader("f1.txt"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Split by whitespace
                for (String word : words) {
                    word = word.toLowerCase(); // Convert word to lowercase
                    // Check if the word starts with the pattern
                    if (pattern.startsWith("*")) {   //*sor
                        if (word.endsWith(pattern.substring(1))) {
                            // Remove dot or comma from the end, if present
                            word = removeDotOrComma(word);
                            searchResults.add(word);
                        }
                    }
                    // Check if the word ends with the pattern
                    else if (pattern.endsWith("*")) {//sor*
                        if (word.startsWith(pattern.substring(0, pattern.length() - 1))) {
                            // Remove dot or comma from the end, if present
                            word = removeDotOrComma(word);
                            searchResults.add(word);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return searchResults;
    }

    private static String removeDotOrComma(String word) {
        // Remove dot or comma from the end of the word, if present
        if (word.endsWith(".") || word.endsWith(",")) {
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }
}
