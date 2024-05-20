package ir_project;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PermutermIndex {
    private Map<String, List<String>> index;

    public PermutermIndex() {
        index = new HashMap<>();
    }

    public void buildIndexFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addTerm(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTerm(String term) {
        // Add all rotations of the term to the index
        for (int i = 0; i < term.length(); i++) {
            String rotatedTerm = term.substring(i) + term.substring(0, i);
            if (!index.containsKey(rotatedTerm)) {
                index.put(rotatedTerm, new ArrayList<>());
            }
            index.get(rotatedTerm).add(term);
        }
    }

    public List<String> searchWildcard(String wildcard) {
        List<String> result = new ArrayList<>();
        for (String key : index.keySet()) {
            if (key.startsWith(wildcard)) {
                result.addAll(index.get(key));
            }
        }
        return result;
    }

    // Method to create GUI content for Permuterm Index
    public VBox createPermutermContent(PermutermIndex permutermIndex, BorderPane root, VBox home) {
        VBox permutermContent = new VBox(10);
        permutermContent.setPadding(new Insets(10));

        // Example usage: Building index from a file
        permutermIndex.buildIndexFromFile("f3.txt");
        
        Button searchButton = new Button("Search Wildcard");
        TextField wildcardField = new TextField();
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        searchButton.setOnAction(e -> {
            String wildcard = wildcardField.getText();
            if (!wildcard.isEmpty()) {
                List<String> searchResults = permutermIndex.searchWildcard(wildcard);
                if (!searchResults.isEmpty()) {
                    StringBuilder message = new StringBuilder("Search Results:\n");
                    for (String word : searchResults) {
                        message.append(word).append("\n");
                    }
                    resultArea.setText(message.toString());
                } else {
                    resultArea.setText("No results found for the given wildcard.");
                }
            } else {
                resultArea.setText("Please enter a wildcard.");
            }
        });

        permutermContent.getChildren().addAll(new Label("Enter wildcard:"), wildcardField, searchButton, resultArea);
        return permutermContent;
    }
}
