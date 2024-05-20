//
//package ir_project;
//
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.VBox;
//import javafx.geometry.Insets;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Label;
//
//public class MatrixGUI {
//
//    private TextField wordField1;
//    private TextField wordField2;
//    private ComboBox<String> operatorComboBox;
//    private Button searchButton;
//    private TextArea resultTextArea;
//
//    // Define your stop words
//    private Set<String> stopWords = new HashSet<>(Arrays.asList("-", "'", ".", "a", "an", "and", "are", "as", "at", "be", "but", "by", "for",
//            "if", "in", "into", "is", "it", "no", "not", "of", "on", "or",
//            "such", "that", "the", "their", "then", "there", "these", "they",
//            "this", "to", "was", "will", "with", " "));
//
//    // Lemmatization rules
//    private static final Map<String, String> LEMMA_RULES = new HashMap<>();
//    static {
////        LEMMA_RULES.put("ing$", "e");
////        LEMMA_RULES.put("ies$", "y");
////        LEMMA_RULES.put("es$", "");
////        LEMMA_RULES.put("ed$", "");
////        LEMMA_RULES.put("s$", "");
////        LEMMA_RULES.put("ing", "e");
////        LEMMA_RULES.put("ies", "y");
////        LEMMA_RULES.put("es", "");
////        LEMMA_RULES.put("ed", "");
////        LEMMA_RULES.put("s", "");
//        LEMMA_RULES.put("are", "be");
//    }
//
//    public MatrixGUI() {
//        wordField1 = new TextField();
//        wordField1.setPromptText("Enter word 1");
//
//        wordField2 = new TextField();
//        wordField2.setPromptText("Enter word 2");
//
//        operatorComboBox = new ComboBox<>();
//        operatorComboBox.getItems().addAll("AND", "OR", "NOT");
//        operatorComboBox.setValue("AND");
//        operatorComboBox.setStyle("-fx-background-color: #551606; -fx-text-fill: black; -fx-font-weight: bold;");
//
//
//        searchButton = new Button("Search");
//        searchButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.txt", "f2.txt", "f3.txt", "f4.txt", "f5.txt", "f6.txt", "f7.txt", "f8.txt", "f9.txt", "f10.txt");
//                for (String result : searchResults) {
//                    resultTextArea.appendText(result + "\n");
//                }
//            }
//        });
//
//        resultTextArea = new TextArea();
//        resultTextArea.setEditable(false);
//        resultTextArea.setWrapText(true);
//        resultTextArea.setPrefWidth(400);
//        resultTextArea.setPrefHeight(300);
//    }
//
//    public VBox createMatrixContent(BorderPane root, VBox home) {
//        VBox matrixContent = new VBox(20); // Increased spacing between components
//        matrixContent.setPadding(new Insets(0)); // Increased padding for better spacing
//
//        Label titleLabel = new Label("Incidence Matrix - Inverted Matrix");
//        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
//        titleLabel.setPadding(new Insets(0));
//
//        wordField1 = new TextField();
//        wordField1.setPromptText("Enter word 1");
//
//        wordField2 = new TextField();
//        wordField2.setPromptText("Enter word 2");
//
//        operatorComboBox = new ComboBox<>();
//        operatorComboBox.getItems().addAll("AND", "OR", "NOT");
//        operatorComboBox.setValue("AND");
//
//        searchButton = new Button("Search");
//        searchButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
//        searchButton.setOnAction(event -> {
//            List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.txt", "f2.txt", "f3.txt", "f4.txt", "f5.txt", "f6.txt", "f7.txt", "f8.txt", "f9.txt", "f10.txt");
//            resultTextArea.clear(); // Clear previous results
//            for (String result : searchResults) {
//                resultTextArea.appendText(result + "\n");
//            }
//        });
//
//        resultTextArea = new TextArea();
//        resultTextArea.setEditable(false);
//        resultTextArea.setPrefHeight(400); // Set preferred height for the TextArea
//
//        Button backButton = new Button("⬅ Back");
//        backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
//        backButton.setOnAction(e -> root.setCenter(home));
//
//        // Set preferred width and height
//        backButton.setPrefWidth(100); // Adjust width as needed
//        backButton.setPrefHeight(40); // Adjust height as needed
//
//        matrixContent.getChildren().addAll(titleLabel, wordField1, wordField2, operatorComboBox, searchButton, resultTextArea, backButton);
//        // Display matrix data in the text area
//        displayMatrixData();
//
//        // Set alignment to center
//        matrixContent.setAlignment(Pos.CENTER);
//
//        return matrixContent;
//    }
//
//    void displayMatrixData() {
//        // Read documents from files
//        String[] documents = readDocumentsFromFiles("f1.txt", "f2.txt", "f3.txt", "f4.txt", "f5.txt", "f6.txt", "f7.txt", "f8.txt", "f9.txt", "f10.txt");
//
//        StringBuilder matrixText = new StringBuilder();
//        matrixText.append("\t\t\t\t\tIncidince MATRIX\n\n");
//
//        Map<String, List<Integer>> termDocumentIncidence = new HashMap<>();
//        Set<String> uniqueTerms = new HashSet<>();
//
//        for (String document : documents) {
//            String[] words = document.split(" ");
//            List<String> termsInDocument = new ArrayList<>(Arrays.asList(words));
//            uniqueTerms.addAll(termsInDocument);
//        }
//
//        List<String> terms = new ArrayList<>(uniqueTerms);
//        Collections.sort(terms);
//
//        for (String term : terms) {
//            Integer[] incidenceArray = new Integer[documents.length];
//
//            for (int i = 0; i < documents.length; i++) {
//                String document = documents[i];
//                String[] words = document.split(" ");
//
//                int incidence = 0;
//                for (String word : words) {
//                    if (lemmatize(word).equalsIgnoreCase(lemmatize
//
//(term))) {
//                        incidence = 1;
//                        break;
//                    }
//                }
//
//                incidenceArray[i] = incidence;
//            }
//
//            termDocumentIncidence.put(term, Arrays.asList(incidenceArray));
//        }
//
//        matrixText.append("\t\t");
//for (int i = 1; i <= documents.length; i++) {
//    matrixText.append(String.format("\t\t%-10s", "Doc " + i));
//}
//matrixText.append("\n");
//matrixText.append("\n");
//
//
//        for (String term : terms) {
//            matrixText.append(String.format("%-30s", term));
//            List<Integer> incidenceList = termDocumentIncidence.get(term);
//            for (int incidence : incidenceList) {
//                matrixText.append(String.format("\t\t%-10s", incidence));
//            }
//            matrixText.append("\n");
//            matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
//
//        }
//
//            matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
//        matrixText.append("\n\t\tInverted INDEX\n\n");
//
//        for (String term : terms) {
//            matrixText.append(String.format("%-20s", term));
//            List<Integer> docList = termDocumentIncidence.get(term);
//            for (int i = 0; i < documents.length; i++) {
//                matrixText.append(String.format("%-10s", docList.get(i) == 1 ? "doc" + (i + 1) : ""));
//            }
//            matrixText.append("\n");
//            matrixText.append("\n");
//            matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
//        }
//
//        // Display matrix data in the resultTextArea
//        resultTextArea.setText(matrixText.toString());
//    }
//
//    private List<String> performSearch(String word1, String word2, String operator, String filename1, String filename2, String filename3, String filename4, String filename5, String filename6, String filename7, String filename8, String filename9, String filename10) {
//        String[] documents = readDocumentsFromFiles(filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10);
//        List<String> searchResults = new ArrayList<>(); // List to store search results
//
//        word1 = lemmatize(word1.toLowerCase().trim()); // Convert search words to lowercase and lemmatize
//        word2 = lemmatize(word2.toLowerCase().trim());
//
//        StringBuilder searchResultText = new StringBuilder();
//        searchResultText.append("Query: ").append(word1).append(" ").append(operator).append(" ").append(word2).append("\n\n");
//
//        for (int i = 0; i < documents.length; i++) {
//            String document = documents[i];
//            document = document.replace(word1, "<" + word1 + ">");
//            document = document.replace(word2, "<" + word2 + ">");
//            documents[i] = document;
//
//            String[] words = document.split("\\s+");
//
//            switch (operator) {
//                case "AND":
//                    if (containsIgnoreCase(words, "<" + word1 + ">") && containsIgnoreCase(words, "<" + word2 + ">")) {
//                        // Append both document number and filename to the search result
//                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
//                    }
//                    break;
//                case "OR":
//                    if (containsIgnoreCase(words, "<" + word1 + ">") || containsIgnoreCase(words, "<" + word2 + ">")) {
//                        // Append both document number and filename to the search result
//                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
//                    }
//                    break;
//                case "NOT":
//                    if (!containsIgnoreCase(words, "<" + word1 + ">") && !containsIgnoreCase(words, "<" + word2 + ">")) {
//                        // Append both document number and filename to the search result
//                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
//                    }
//                    break;
//            }
//        }
//
//        return searchResults;
//    }
//
//    // Helper method to get the filename based on the document index
//    private String getFileName(int index, String... filenames) {
//        int sum = 0;
//        for (String filename : filenames) {
//            sum += numDocsInFile(filename);
//            if (index < sum) {
//                return filename;
//            }
//        }
//        return "";
//    }
//
//    // Helper method to count the number of documents in a file
//    private int numDocsInFile(String filename) {
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(filename));
//            int count = 0;
//            while (br.readLine() != null) {
//                count++;
//            }
//            br.close();
//            return count;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    // Helper method to check if an array contains a string (case insensitive)
//    private boolean containsIgnoreCase(String[] array, String str) {
//        for (String s : array) {
//            if (s.equalsIgnoreCase(str)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Lemmatize a word using simple rules
//    private String lemmatize(String word) {
//        for (Map.Entry<String, String> rule : LEMMA_RULES.entrySet()) {
//            if (word.endsWith(rule.getKey())) {
//                return word.substring(0, word.length() - rule.getKey().length()) + rule.getValue();
//            }
//        }
//        return word;
//    }
//
//    private String[] readDocumentsFromFiles(String filename1, String filename2, String filename3, String filename4, String filename5, String filename6, String filename7, String filename8, String filename9, String filename10) {
//        List<String> documents = new ArrayList<>();
//        try {
//            BufferedReader br1 = new BufferedReader(new FileReader(filename1));
//            BufferedReader br2 = new BufferedReader(new FileReader(filename2));
//            BufferedReader br3 = new BufferedReader(new FileReader(filename3));
//            BufferedReader br4 = new BufferedReader(new FileReader(filename4));
//            BufferedReader br5 = new BufferedReader(new FileReader(filename5));
//            BufferedReader br6 = new BufferedReader(new FileReader(filename6));
//            BufferedReader br7 = new BufferedReader(new FileReader(filename7));
//            BufferedReader br8 = new BufferedReader(new FileReader(filename8));
//            BufferedReader br9 = new BufferedReader(new FileReader(filename9));
//            BufferedReader br10 = new BufferedReader(new FileReader(filename10));
//
//            // Read documents from the first file
//            readDocumentsFromFile(br1, documents);
//
//            // Read documents from the second file
//            readDocumentsFromFile(br2, documents);
//
//            // Read documents from the third file
//            readDocumentsFromFile(br3, documents);
//
//            // Read documents from the fourth file
//           
//
// readDocumentsFromFile(br4, documents);
//
//            // Read documents from the fifth file
//            readDocumentsFromFile(br5, documents);
//
//            // Read documents from the sixth file
//            readDocumentsFromFile(br6, documents);
//
//            // Read documents from the seventh file
//            readDocumentsFromFile(br7, documents);
//
//            // Read documents from the eighth file
//            readDocumentsFromFile(br8, documents);
//
//            // Read documents from the ninth file
//            readDocumentsFromFile(br9, documents);
//
//            // Read documents from the tenth file
//            readDocumentsFromFile(br10, documents);
//
//            // Close readers
//            br1.close();
//            br2.close();
//            br3.close();
//            br4.close();
//            br5.close();
//            br6.close();
//            br7.close();
//            br8.close();
//            br9.close();
//            br10.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return documents.toArray(new String[0]);
//    }
//
//    // Helper method to read documents from a single file
//    private void readDocumentsFromFile(BufferedReader br, List<String> documents) throws IOException {
//        String line;
//        while ((line = br.readLine()) != null) {
//            documents.add(filterStopWordsAndLemmatize(line)); // Filter stop words and lemmatize
//        }
//    }
//
//    private String filterStopWordsAndLemmatize(String document) {
//        // Remove punctuation and special characters
//        String cleanedDocument = document.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
//
//        // Remove stop words from the document
//        String[] words = cleanedDocument.split("\\s+");
//        StringBuilder filteredDocument = new StringBuilder();
//        for (String word : words) {
//            if (!stopWords.contains(word) && !word.trim().isEmpty()) {
//                // Lemmatize the word
//                String lemma = lemmatize(word);
//                filteredDocument.append(lemma).append(" ");
//            }
//        }
//        return filteredDocument.toString().trim();
//    }
//
//}
//


package ir_project;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.io.IOException;
import java.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class MatrixGUI {

    private TextField wordField1;
    private TextField wordField2;
    private ComboBox<String> operatorComboBox;
    private Button searchButton;
    private TextArea resultTextArea;

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

    public MatrixGUI() {
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
                List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.pdf", "f2.pdf", "f3.pdf", "f4.pdf", "f5.pdf", "f6.pdf", "f7.pdf", "f8.pdf", "f9.pdf", "f10.pdf");
                for (String result : searchResults) {
                    resultTextArea.appendText(result + "\n");
                }
            }
        });

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);
        resultTextArea.setPrefWidth(400);
        resultTextArea.setPrefHeight(300);
    }

    public VBox createMatrixContent(BorderPane root, VBox home) {
        VBox matrixContent = new VBox(20); // Increased spacing between components
        matrixContent.setPadding(new Insets(0)); // Increased padding for better spacing

        Label titleLabel = new Label("Incidence Matrix - Inverted Matrix");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
        titleLabel.setPadding(new Insets(0));

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
            List<String> searchResults = performSearch(wordField1.getText(), wordField2.getText(), operatorComboBox.getValue(), "f1.pdf", "f2.pdf", "f3.pdf", "f4.pdf", "f5.pdf", "f6.pdf", "f7.pdf", "f8.pdf", "f9.pdf", "f10.pdf");
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
        String[] documents = readDocumentsFromFiles("f1.pdf", "f2.pdf", "f3.pdf", "f4.pdf", "f5.pdf", "f6.pdf", "f7.pdf", "f8.pdf", "f9.pdf", "f10.pdf");

        StringBuilder matrixText = new StringBuilder();
        matrixText.append("\t\t\t\t\tIncidince MATRIX\n\n");

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
            matrixText.append(String.format("\t\t%-10s", "Doc " + i));
        }
        matrixText.append("\n");
        matrixText.append("\n");


        for (String term : terms) {
            matrixText.append(String.format("%-30s", term));
            List<Integer> incidenceList = termDocumentIncidence.get(term);
            for (int incidence : incidenceList) {
                matrixText.append(String.format("\t\t%-10s", incidence));
            }
            matrixText.append("\n");
            matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        }

        matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        matrixText.append("\n\t\tInverted INDEX\n\n");

        for (String term : terms) {
            matrixText.append(String.format("%-20s", term));
            List<Integer> docList = termDocumentIncidence.get(term);
            for (int i = 0; i < documents.length; i++) {
                matrixText.append(String.format("%-10s", docList.get(i) == 1 ? "doc" + (i + 1) : ""));
            }
            matrixText.append("\n");
            matrixText.append("\n");
            matrixText.append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }

        // Display matrix data in the resultTextArea
        resultTextArea.setText(matrixText.toString());
    }

    private List<String> performSearch(String word1, String word2, String operator, String filename1, String filename2, String filename3, String filename4, String filename5, String filename6, String filename7, String filename8, String filename9, String filename10) {
        String[] documents = readDocumentsFromFiles(filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10);
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
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
                    }
                    break;
                case "OR":
                    if (containsIgnoreCase(words, "<" + word1 + ">") || containsIgnoreCase(words, "<" + word2 + ">")) {
                        // Append both document number and filename to the search result
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
                    }
                    break;
                case "NOT":
                    if (!containsIgnoreCase(words, "<" + word1 + ">") && !containsIgnoreCase(words, "<" + word2 + ">")) {
                        // Append both document number and filename to the search result
                        searchResults.add("Doc: " + (i + 1) + ", " + "File: " + getFileName(i, filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10) + ", " + "\n" + document);
                    }
                    break;
            }
        }

        return searchResults;
    }

    // Helper method to get the filename based on the document index
    private String getFileName(int index, String... filenames) {
        int sum = 0;
        for (String filename : filenames) {
            sum += numDocsInFile(filename);
            if (index < sum) {
                return filename;
            }
        }
        return "";
    }

    // Helper method to count the number of pages in a PDF file
    private int numDocsInFile(String filename) {
        try {
            PDDocument document = PDDocument.load(new File(filename));
            int count = document.getNumberOfPages();
            document.close();
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

    private String[] readDocumentsFromFiles(String filename1, String filename2, String filename3, String filename4, String filename5, String filename6, String filename7, String filename8, String filename9, String filename10) {
        List<String> documents = new ArrayList<>();
        try {
            String[] filenames = {filename1, filename2, filename3, filename4, filename5, filename6, filename7, filename8, filename9, filename10};
            for (String filename : filenames) {
                PDDocument document = PDDocument.load(new File(filename));
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                documents.add(filterStopWordsAndLemmatize(text));
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents.toArray(new String[0]);
    }

    // Helper method to remove stop words and lemmatize words in a document
    private String filterStopWordsAndLemmatize(String document) {
        // Remove punctuation and special characters
        String cleanedDocument = document.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();

        // Remove stop words and empty spaces from the document
        String[] words = cleanedDocument.split("\\s+");
        StringBuilder filteredDocument = new StringBuilder();
        for (String word : words) {
            if (!stopWords.contains(word) && !word.trim().isEmpty()) {
                // Lemmatize the word
                String lemma = lemmatize(word);
                filteredDocument.append(lemma).append(" ");
            }
        }
        return filteredDocument.toString().trim();
    }

}
