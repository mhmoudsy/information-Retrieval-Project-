package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PositionIndex {
    private Map<String, Map<Integer, List<Integer>>> index;
    private List<String[]> documents;

    public PositionIndex() {
        index = new HashMap<>();
        documents = new ArrayList<>();
    }

    public void addDocument(int documentId, String[] words) {
        documents.add(words);
        indexDocument(documentId, words);
    }

    private void indexDocument(int documentId, String[] words) {
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (!index.containsKey(word)) {
                index.put(word, new HashMap<>());
            }
            if (!index.get(word).containsKey(documentId)) {
                index.get(word).put(documentId, new ArrayList<>());
            }
            index.get(word).get(documentId).add(i);
        }
    }

    public void addDocuments() {
        for (int i = 1; i <= 3; i++) {
            String fileName = "p" + i + ".txt";
            String[] words = readFile(fileName);
            addDocument(i - 1, words);
        }
    }

//    public void addOne(int documentId, String word) {
//        if (!index.containsKey(word)) {
//            index.put(word, new HashMap<>());
//        }
//        if (!index.get(word).containsKey(documentId)) {
//            index.get(word).put(documentId, new ArrayList<>());
//        }
//        index.get(word).get(documentId).add(index.get(word).get(documentId).size()); // Add position as the last index
//    }

    private String[] readFile(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.addAll(Arrays.asList(line.toLowerCase().split("\\s+")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.toArray(new String[0]);
    }

    public Map<Integer, List<Integer>> search(String query) {
        String[] queryWords = query.toLowerCase().split("\\s+");
        Map<Integer, List<Integer>> resultMap = new HashMap<>();

        if (queryWords.length == 0) {
            return resultMap;
        }

        Set<Integer> commonDocs = new HashSet<>();
        for (String queryWord : queryWords) {
            if (index.containsKey(queryWord)) {
                Set<Integer> docIds = index.get(queryWord).keySet();
                if (commonDocs.isEmpty()) {
                    commonDocs.addAll(docIds);
                } else {
                    commonDocs.retainAll(docIds);
                }
            }
        }

        for (int docId : commonDocs) {
            List<Integer> positions = new ArrayList<>();
            for (String queryWord : queryWords) {
                if (index.containsKey(queryWord) && index.get(queryWord).containsKey(docId)) {
                    positions.addAll(index.get(queryWord).get(docId));
                } else {
                    positions.clear();
                    break;
                }
            }
            if (!positions.isEmpty()) {
                resultMap.put(docId, positions);
            }
        }

        return resultMap;
    }

    public boolean wordsInSameDoc(String word1, String word2) {
        if (!index.containsKey(word1) || !index.containsKey(word2)) {
            return false;
        }

        Set<Integer> docsForWord1 = index.get(word1).keySet();
        Set<Integer> docsForWord2 = index.get(word2).keySet();

        return !Collections.disjoint(docsForWord1, docsForWord2);
    }

   public VBox createPositionContent(PositionIndex index, BorderPane root, VBox home) {
       
    addDocuments(); // Add the three text files to the index

    VBox content = new VBox(10);
    content.setPadding(new Insets(10));

    // Display documents
    TextArea documentsArea = new TextArea();
    documentsArea.setPrefRowCount(20); // Adjust the number of rows to fit your requirement
        documentsArea.setPrefWidth(400); // Adjust the width as needed
        documentsArea.setPrefHeight(200); // Adjust the height as needed
    documentsArea.setEditable(false);
    documentsArea.setWrapText(true);
    StringBuilder documentsText = new StringBuilder("Array of words for each document:\n");
    for (int i = 0; i < documents.size(); i++) {
        documentsText.append("Doc ").append(i + 1).append(":\n").append(Arrays.toString(documents.get(i))).append("\n");
    }
    documentsArea.setText(documentsText.toString());
    content.getChildren().add(documentsArea);

    // Search input and result area
    TextArea queryInput = new TextArea();
    queryInput.setPromptText("Enter query...");
    queryInput.setPrefRowCount(2);

    Button searchButton = new Button("Search");
    TextArea resultArea = new TextArea();
    searchButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 100px;");
    resultArea.setEditable(false);
    resultArea.setWrapText(true);
resultArea.setPrefWidth(300);  // Set the preferred width to 400 pixels
resultArea.setPrefHeight(200);
    searchButton.setOnAction(e -> {
        String query = queryInput.getText();
        Map<Integer, List<Integer>> result = index.search(query);
        displaySearchResult(result, resultArea);
    });
    
    Label wordsLabel = new Label("Search about two words ...");
    wordsLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
    wordsLabel.setPadding(new Insets(0));
    // Words in the same document input and result area
    TextArea wordsInput = new TextArea();
    wordsInput.setPromptText("Enter two words...");
    wordsInput.setPrefRowCount(1);

    Button wordsButton = new Button("Check");
    TextArea wordsResult = new TextArea();
    wordsButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
    wordsResult.setEditable(false);
   wordsButton.setOnAction(e -> {
    String[] words = wordsInput.getText().toLowerCase().split("\\s+");
    if (words.length == 2) {
        Set<Integer> commonDocs = new HashSet<>();
        if (index.wordsInSameDoc(words[0], words[1])) {
            for (String word : words) {
                if (index.index.containsKey(word)) {
                    Set<Integer> docIds = index.index.get(word).keySet();
                    if (commonDocs.isEmpty()) {
                        commonDocs.addAll(docIds);
                    } else {
                        commonDocs.retainAll(docIds);
                    }
                }
            }
            StringBuilder resultText = new StringBuilder();
            resultText.append("Files containing both words:\n");
            if (!commonDocs.isEmpty()) {
                for (int docId : commonDocs) {
                    String fileName = "p" + (docId + 1) + ".txt"; // Derive file name from document ID
                    resultText.append(fileName).append("\n");
                }
            } else {
                resultText.append("No files found containing both words.\n");
            }
            wordsResult.setText(resultText.toString());
        } else {
            wordsResult.setText("The words [" + words[0] + "] and [" + words[1] + "] are not in the same document.");
        }
    } else {
        wordsResult.setText("Please enter exactly two words.");
    }
});

    // Back button
   Button backButton = new Button("⬅ Back");
backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
backButton.setOnAction(e -> root.setCenter(home));

// Set preferred width and height
backButton.setPrefWidth(100); // Adjust width as needed
backButton.setPrefHeight(40); // Adjust height as needed
// Label titleLabel = new Label("Search Engine Application");
//        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
//        titleLabel.setPadding(new Insets(10, 0, 20, 0));
//        
    content.getChildren().addAll(queryInput, searchButton, resultArea,wordsLabel, wordsInput, wordsButton, wordsResult, backButton);
    return content;
}

  private void displaySearchResult(Map<Integer, List<Integer>> result, TextArea resultArea) {
    StringBuilder resultText = new StringBuilder();
    resultText.append("Files containing the query words with positions:\n");

    boolean hasResults = false; // Flag to track if any results were found

    for (int docId : result.keySet()) {
        String fileName = "p" + (docId + 1) + ".txt"; // Derive file name from document ID
        List<Integer> positions = result.get(docId);
        List<Integer> tempPositions = new ArrayList<>(positions); // Create a copy of positions

        // Find all positions of both query words
        List<Integer> allQueryPositions = new ArrayList<>();
        for (Integer position : tempPositions) {
            if (!allQueryPositions.contains(position)) {
                allQueryPositions.add(position);
            }
        }

        // Check if the query terms are consecutive
        boolean consecutive = true;
        for (int i = 1; i < allQueryPositions.size(); i++) {
            if (allQueryPositions.get(i) - allQueryPositions.get(i - 1) != 1) {
                consecutive = false;
                break;
            }
        }

        if (!allQueryPositions.isEmpty()) { // If there are positions
            hasResults = true; // Update flag
            if (consecutive) {
                resultText.append("File: ").append(fileName).append("\tPositions: ").append(allQueryPositions).append("\n");
            } else {
                resultText.append("in same file but Positions are not in following\n");
            }
        }
    }

    if (!hasResults) { // If no results were found
        resultText.append("No files found containing the query words.\n");
    }

    resultArea.setText(resultText.toString());
}
}