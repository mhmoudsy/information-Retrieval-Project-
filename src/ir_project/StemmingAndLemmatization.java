package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.tartarus.snowball.ext.PorterStemmer;

public class StemmingAndLemmatization {

    public static String stemText(String text) {
        PorterStemmer stemmer = new PorterStemmer();
        String[] wordTokens = text.split("\\s+");
        StringBuilder stemmedText = new StringBuilder();
        for (String word : wordTokens) {
            stemmer.setCurrent(word);
            stemmer.stem();
            stemmedText.append(stemmer.getCurrent()).append(" ");
        }
        return stemmedText.toString().trim();
    }

    public static String lemmatizeText(String text) {
        // For simplicity, just return the original text without lemmatization
        return text;
    }

    public static void main(String[] args) {
        String fileName = "f1.txt"; // Replace "f1.txt" with your file name
        try {
            String content = readFile(fileName);
            String stemmedContent = stemText(content);
            System.out.println("Stemmed Content:");
            System.out.println(stemmedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
