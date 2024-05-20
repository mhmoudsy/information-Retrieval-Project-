package ir_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.tartarus.snowball.ext.EnglishStemmer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class MatrixGUIwithStemming {

    private TextField wordField1;
    private TextField wordField2;
    private ComboBox<String> operatorComboBox;
    private Button searchButton;
    private TextArea resultTextArea;
    private EnglishStemmer stemmer = new EnglishStemmer();

    // Define your stop words
    private Set<String> stopWords = new HashSet<>(Arrays.asList("-", "'", ".", "a", "an", "and", "are", "as", "at", "be", "but", "by", "for",
            "if", "in", "into", "is", "it", "no", "not", "of", "on", "or",
            "such", "that", "the", "their", "then", "there", "these", "they",
            "this", "to", "was", "will", "with", " "));

    // Lemmatization rules
    private static final Map<String, String> LEMMA_RULES = new HashMap<>();
    static {
      LEMMA_RULES.put("are", "be");
    }

    public MatrixGUIwithStemming() {
        wordField1 = new TextField();
        wordField1.setPromptText("Enter word 1");

        wordField2 = new TextField();
        wordField2.setPromptText("Enter word 2");

        operatorComboBox = new ComboBox<>();
        operatorComboBox.getItems().addAll("AND", "OR", "NOT");
        operatorComboBox.setValue("AND");
        operatorComboBox.setStyle("-fx-background-color: #551606; -fx-text-fill: black; -fx-font-weight: bold;");

        searchButton = new Button("Search");
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.txt", "f2.txt", "f3.txt");
                for (String result : searchResults) {
                    resultTextArea.appendText(result + "\n");
                }
            }
        });

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPrefHeight(400); // Set preferred height for the TextArea
    }

    public VBox createMatrixContent(BorderPane root, VBox home) {
        VBox matrixContent = new VBox(20); // Increased spacing between components
        matrixContent.setPadding(new Insets(20)); // Increased padding for better spacing

        Label titleLabel = new Label("Incidence Matrix - Inverted Matrix");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
        titleLabel.setPadding(new Insets(10, 0, 20, 0));

        wordField1 = new TextField();
        wordField1.setPromptText("Enter word 1");

        wordField2 = new TextField();
        wordField2.setPromptText("Enter word 2");

        operatorComboBox = new ComboBox<>();
        operatorComboBox.getItems().addAll("AND", "OR", "NOT");
        operatorComboBox.setValue("AND");

        searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
        searchButton.setOnAction(event -> {
            List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.txt", "f2.txt", "f3.txt");
            resultTextArea.clear(); // Clear previous results
            for (String result : searchResults) {
                resultTextArea.appendText(result + "\n");
            }
        });

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPrefHeight(400); // Set preferred height for the TextArea

        Button backButton = new Button("⬅ Back");
        backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
        backButton.setOnAction(e -> root.setCenter(home));

        // Set preferred width and height
        backButton.setPrefWidth(100); // Adjust width as needed
        backButton.setPrefHeight(40); // Adjust height as needed

        matrixContent.getChildren().addAll(titleLabel, wordField1, wordField2, operatorComboBox, searchButton, resultTextArea, backButton);
        // Display matrix data in the text area
        displayMatrixData();

        // Set alignment to center
        matrixContent.setAlignment(Pos.CENTER);

        return matrixContent;
    }

    void displayMatrixData() {
        // Read documents from files
        String[] documents = readDocumentsFromFiles("f1.txt", "f2.txt", "f3.txt");

        StringBuilder matrixText = new StringBuilder();
        matrixText.append("\t\t\t\t\tMATRIX\n\n");

        Map<String, List<Integer>> termDocumentIncidence = new HashMap<>();
        Set<String> uniqueTerms = new HashSet<>();

        for (String document : documents) {
            String[] words = document.split(" ");
            List<String> termsInDocument = new ArrayList<>(Arrays.asList(words));
            uniqueTerms.addAll(termsInDocument);
        }

        List<String> terms = new ArrayList<>(uniqueTerms);
        Collections.sort(terms);

        for (String term : terms) {
            Integer[] incidenceArray = new Integer[documents.length];

            for (int i = 0; i < documents.length; i++) {
                String document = documents[i];
                String[] words = document.split(" ");

                int incidence = 0;
                for (String word : words) {
                    if (lemmatize(word).equalsIgnoreCase(lemmatize(term))) {
                        incidence = 1;
                        break;
                    }
                }

                incidenceArray[i] = incidence;
            }

            termDocumentIncidence.put(term, Arrays.asList(incidenceArray));
        }

        matrixText.append("\t\t");
        for (int i = 1; i <= documents.length; i++) {
            matrixText.append("\t").append("Doc ").append(i).append("\t");
        }
        matrixText.append("\n");
        matrixText.append("\n");


        for (String term : terms) {
            matrixText.append(String.format("%-30s", term));
            List<Integer> incidenceList = termDocumentIncidence.get(term);
            for (int incidence : incidenceList) {
                matrixText.append(String.format("%-20d", incidence));
            }
            matrixText.append("\n");
            matrixText.append("------------------------------------------------------------------------------------------\n");

        }

        matrixText.append("________________________________________________________________________________________________________________________________________________________________________________________________________________________\n");
        matrixText.append("\n\t\tInverted INDEX\n\n");

        for (String term : terms) {
            matrixText.append(String.format("%-20s", term));
            List<Integer> docList = termDocumentIncidence.get(term);
            for (int i = 0; i < documents.length; i++) {
                matrixText.append(String.format("%-10s", docList.get(i) == 1 ? "doc" + (i + 1) : ""));
            }
            matrixText.append("\n");
            matrixText.append("\n");
            matrixText.append("----------------------------------------------\n");
        }

        // Display matrix data in the resultTextArea
        resultTextArea.setText(matrixText.toString());
    }

    private List<String> performSearch(String word1, String word2, String operator, String filename1, String filename2, String filename3) {
        String[] documents = readDocumentsFromFiles(filename1, filename2, filename3);
        List<String> searchResults = new ArrayList<>(); // List to store search results

        word1 = lemmatize(word1.toLowerCase().trim()); // Convert search words to lowercase and lemmatize
        word2 = lemmatize(word2.toLowerCase().trim());

        StringBuilder searchResultText = new StringBuilder();
        searchResultText.append("Query: ").append(word1).append(" ").append(operator).append(" ").append(word2).append("\n\n");

        for (int i = 0; i < documents.length; i++) {
            String document = documents[i];
            document = document.replace(word1, "<" + word1 + ">");
            document = document.replace(word2, "<" + word2 + ">");
            documents[i] = document;

            String[] words = document.split("\\s+");

            switch (operator) {
                case "AND":
                    if (containsIgnoreCase(words, "<" + word1 + ">") && containsIgnoreCase(words, "<" + word2 + ">")) {
                        // Append both document number and filename to the search result
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3) + ", " + "\n" + document);
                    }
                    break;
                case "OR":
                    if (containsIgnoreCase(words, "<" + word1 + ">") || containsIgnoreCase(words, "<" + word2 + ">")) {
                        // Append both document number and filename to the search result
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3) + ", " + "\n" + document);
                    }
                    break;
                case "NOT":
                    if (!containsIgnoreCase(words, "<" + word1 + ">") && !containsIgnoreCase(words, "<" + word2 + ">")) {
                        // Append both document number and filename to the search result
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3) + ", " + "\n" + document);
                    }
                    break;
            }
        }

        return searchResults;
    }

    // Helper method to get the filename based on the document index
    private String getFileName(int index, String filename1, String filename2, String filename3) {
        if (index < numDocsInFile(filename1)) {
            return filename1;
        } else if (index < numDocsInFile(filename1) + numDocsInFile(filename2)) {
            return filename2;
        } else {
            return filename3;
        }
    }

    // Helper method to count the number of documents in a file
    private int numDocsInFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int count = 0;
            while (br.readLine() != null) {
                count++;
            }
            br.close();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Helper method to check if an array contains a string (case insensitive)
    private boolean containsIgnoreCase(String[] array, String str) {
        for (String s : array) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    // Lemmatize a word using simple rules
    private String lemmatize(String word) {
        for (Map.Entry<String, String> rule : LEMMA_RULES.entrySet()) {
            if (word.endsWith(rule.getKey())) {
                return word.substring(0, word.length() - rule.getKey().length()) + rule.getValue();
            }
        }
        return word;
    }

    // Method to perform stemming
    private String stem(String word) {
        stemmer.setCurrent(word);
        if (stemmer.stem()) {
            return stemmer.getCurrent();
        } else {
            // Stemming failed, return the original word
            return word;
        }
    }

    // Update the filterStopWordsAndLemmatize method to include stemming
    private String filterStopWordsAndStem(String document) {
        // Remove punctuation and special characters
        String cleanedDocument = document.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();

        // Remove stop words from the document and perform stemming
        String[] words = cleanedDocument.split("\\s+");
        StringBuilder filteredDocument = new StringBuilder();
        for (String word : words) {
            if (!stopWords.contains(word) && !word.trim().isEmpty()) {
                // Stem the word
                String stemmedWord = stem(word);
                filteredDocument.append(stemmedWord).append(" ");
            }
        }
        return filteredDocument.toString().trim();
    }

    private String[] readDocumentsFromFiles(String filename1, String filename2, String filename3) {
        List<String> documents = new ArrayList<>();
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(filename1));
            BufferedReader br2 = new BufferedReader(new FileReader(filename2));
            BufferedReader br3 = new BufferedReader(new FileReader(filename3));

            // Read documents from the first file
            String line;
            while ((line = br1.readLine()) != null) {
                documents.add(filterStopWordsAndStem(line)); // Filter stop words and stem
            }

            // Read documents from the second file
            while ((line = br2.readLine()) != null) {
                documents.add(filterStopWordsAndStem(line)); // Filter stop words and stem
            }

            // Read documents from the third file
            while ((line = br3.readLine()) != null) {
                documents.add(filterStopWordsAndStem(line)); // Filter stop words and stem
            }

            // Close readers
            br1.close();
            br2.close();
            br3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents.toArray(new String[0]);
    }
}
