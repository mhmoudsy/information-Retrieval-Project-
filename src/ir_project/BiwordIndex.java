//package ir_project;
//
//import javafx.geometry.Insets;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.VBox;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BiwordIndex {
//    private Map<String, List<Integer>> index;
//
//    public BiwordIndex() {
//        index = new HashMap<>();
//    }
//
//    public void addDocument(int documentId, String text) {
//        String[] words = text.toLowerCase().split("\\s+");
//        List<String> biwords = new ArrayList<>();
//        for (int i = 0; i < words.length - 1; i++) {
//            biwords.add(words[i] + " " + words[i + 1]);
//        }
//        for (String biword : biwords) {
//            if (!index.containsKey(biword)) {
//                index.put(biword, new ArrayList<>());
//            }
//            index.get(biword).add(documentId);
//        }
//    }
//  List<String> queryBiwords = new ArrayList<>();
//  
//   public Map<Integer, List<Integer>> search(String query) {
//    String[] queryWords = query.toLowerCase().split("\\s+");
//  
//    // Clear queryBiwords list for each new search
//    queryBiwords.clear();
//  
//    for (int i = 0; i < queryWords.length - 1; i++) {
//        queryBiwords.add(queryWords[i] + " " + queryWords[i + 1]);
//    }
//
//    Map<Integer, List<Integer>> resultMap = new HashMap<>();
//    for (int docId = 0; docId < documents.size(); docId++) {
//        boolean queryFound = false;
//        List<Integer> positions = new ArrayList<>();
//        String[] documentWords = documents.get(docId).toLowerCase().split("\\s+");
//        int queryIndex = 0; // Track the current index in the queryBiwords list
//        for (int i = 0; i < documentWords.length - 1; i++) {
//            String documentBiword = documentWords[i] + " " + documentWords[i + 1];
//            if (queryBiwords.get(queryIndex).equals(documentBiword)) {
//                positions.add(i);
//                queryIndex++;
//                if (queryIndex == queryBiwords.size()) { // Check if all query biwords found
//                    queryFound = true;
//                    break;
//                }
//            }
//        }
//        if (queryFound) {
//            resultMap.put(docId, positions);
//        }
//    }
//    return resultMap;
//}
//
//    public VBox createBiwordContent(BiwordIndex index, BorderPane root, VBox home) {
//        VBox content = new VBox(10);
//        content.setPadding(new Insets(10));
//
//        TextArea documentsArea = new TextArea();
//        documentsArea.setEditable(false);
//        documentsArea.setWrapText(true);
//        documentsArea.setPrefWidth(400);
//        documentsArea.setPrefHeight(300);
//        StringBuilder documentsText = new StringBuilder("Array of words for each document:\n");
//        for (int i = 0; i < BiwordIndex.documents.size(); i++) {
//            String[] words = BiwordIndex.documents.get(i).toLowerCase().split("\\s+");
//            List<String> biwords = new ArrayList<>();
//            for (int j = 0; j < words.length - 1; j++) {
//                biwords.add(words[j] + " " + words[j + 1]);
//            }
//            documentsText.append("Doc ").append(i + 1).append(": ").append(biwords).append("\n\n");
//        }
//        documentsArea.setText(documentsText.toString());
//
//        TextField queryInput = new TextField();
//        queryInput.setPromptText("Enter query...");
//        queryInput.setPrefWidth(300);
//        queryInput.setPrefHeight(40);
//
//        Button searchButton = new Button("Search");
//        TextArea resultArea = new TextArea();
//        searchButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
//        resultArea.setEditable(false);
//        searchButton.setOnAction(e -> {
//            String query = queryInput.getText();
//            Map<Integer, List<Integer>> result = index.search(query);
//            displaySearchResult(result, resultArea);
//        });
//
//        Button backButton = new Button("⬅ Back");
//        backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
//        backButton.setOnAction(e -> root.setCenter(home));
//        backButton.setPrefWidth(100);
//        backButton.setPrefHeight(40);
//
//        content.getChildren().addAll(documentsArea, queryInput, searchButton, resultArea, backButton);
//        return content;
//    }
//
//  private void displaySearchResult(Map<Integer, List<Integer>> result, TextArea resultArea) {
//    StringBuilder resultText = new StringBuilder();
//    resultText.append("Documents containing the query biwords:\n");
//
//    for (int docId : result.keySet()) {
//        List<Integer> positions = result.get(docId);
//
//        resultText.append("Doc: ").append(docId + 1).append("\t\tPositions: [");
//        boolean positionsExist = false;
//
//        int previousPosition = -1;
//        boolean positionsInSameDoc = true;
//       // System.out.println(positions.size());
//        for (int i = 0; i < positions.size(); i++) {
//            if (i > 0) {
//                resultText.append(", ");
//            }
//            int adjustedPosition = positions.get(i) + 1;
//            resultText.append(adjustedPosition);
//            positionsExist = true;
//
//            if (previousPosition != -1 && previousPosition + 1 != adjustedPosition) {
//                positionsInSameDoc = false;
//            }
//            previousPosition = adjustedPosition;
//        }
//
//        resultText.append("]\n");
//
//        if (!positionsExist) {
//            resultText.append("Not exist");
//        } else if (!positionsInSameDoc) {
//            resultText.append("Positions are in different documents");
//        } else {
//            String fileName = "f" + (docId + 1) + ".txt";
//            resultText.append("File Name: ").append(fileName).append("\n\n");
//        }
//    }
//    resultArea.setText(resultText.toString());
//}
//    public static List<String> documents = new ArrayList<>();
//
//    static {
//        BiwordIndex index = new BiwordIndex();
//        try {
//            for (int i = 1; i <= 10; i++) {
//                String fileName = "f" + i + ".txt";
//                String text = readFile(fileName);
//                documents.add(text);
//                index.addDocument(i - 1, text);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String readFile(String fileName) throws IOException {
//        StringBuilder content = new StringBuilder();
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//        }
//        return content.toString();
//    }
//}

package ir_project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiwordIndex {
    private Map<String, List<Integer>> index;

    public BiwordIndex() {
        index = new HashMap<>();
    }

    public void addDocument(int documentId, String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> biwords = new ArrayList<>();
        for (int i = 0; i < words.length - 1; i++) {
            biwords.add(words[i] + " " + words[i + 1]);
        }
        for (String biword : biwords) {
            if (!index.containsKey(biword)) {
                index.put(biword, new ArrayList<>());
            }
            index.get(biword).add(documentId);
        }
    }

    List<String> queryBiwords = new ArrayList<>();

    public Map<Integer, List<Integer>> search(String query) {
        String[] queryWords = query.toLowerCase().split("\\s+");

        // Clear queryBiwords list for each new search
        queryBiwords.clear();

        for (int i = 0; i < queryWords.length - 1; i++) {
            queryBiwords.add(queryWords[i] + " " + queryWords[i + 1]);
        }

        Map<Integer, List<Integer>> resultMap = new HashMap<>();
        for (int docId = 0; docId < documents.size(); docId++) {
            boolean queryFound = false;
            List<Integer> positions = new ArrayList<>();
            String[] documentWords = documents.get(docId).toLowerCase().split("\\s+");
            int queryIndex = 0; // Track the current index in the queryBiwords list
            for (int i = 0; i < documentWords.length - 1; i++) {
                String documentBiword = documentWords[i] + " " + documentWords[i + 1];
                if (queryBiwords.get(queryIndex).equals(documentBiword)) {
                    positions.add(i);
                    queryIndex++;
                    if (queryIndex == queryBiwords.size()) { // Check if all query biwords found
                        queryFound = true;
                        break;
                    }
                }
            }
            if (queryFound) {
                resultMap.put(docId, positions);
            }
        }
        return resultMap;
    }

    public VBox createBiwordContent(BiwordIndex index, BorderPane root, VBox home) {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TextArea documentsArea = new TextArea();
        documentsArea.setEditable(false);
        documentsArea.setWrapText(true);
        documentsArea.setPrefWidth(400);
        documentsArea.setPrefHeight(300);
        StringBuilder documentsText = new StringBuilder("Array of words for each document:\n");
        for (int i = 0; i < BiwordIndex.documents.size(); i++) {
            String[] words = BiwordIndex.documents.get(i).toLowerCase().split("\\s+");
            List<String> biwords = new ArrayList<>();
            for (int j = 0; j < words.length - 1; j++) {
                biwords.add(words[j] + " " + words[j + 1]);
            }
            documentsText.append("Doc ").append(i + 1).append(": ").append(biwords).append("\n\n");
        }
        documentsArea.setText(documentsText.toString());

        TextField queryInput = new TextField();
        queryInput.setPromptText("Enter query...");
        queryInput.setPrefWidth(300);
        queryInput.setPrefHeight(40);

        Button searchButton = new Button("Search");
        TextArea resultArea = new TextArea();
        searchButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
        resultArea.setEditable(false);
        searchButton.setOnAction(e -> {
            String query = queryInput.getText();
            Map<Integer, List<Integer>> result = index.search(query);
            displaySearchResult(result, resultArea);
        });

        Button backButton = new Button("⬅ Back");
        backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
        backButton.setOnAction(e -> root.setCenter(home));
        backButton.setPrefWidth(100);
        backButton.setPrefHeight(40);

        content.getChildren().addAll(documentsArea, queryInput, searchButton, resultArea, backButton);
        return content;
    }

    private void displaySearchResult(Map<Integer, List<Integer>> result, TextArea resultArea) {
        StringBuilder resultText = new StringBuilder();
        resultText.append("Documents containing the query biwords:\n");

        for (int docId : result.keySet()) {
            List<Integer> positions = result.get(docId);

            resultText.append("Doc: ").append(docId + 1).append("\t\tPositions: [");
            boolean positionsExist = false;

            int previousPosition = -1;
            boolean positionsInSameDoc = true;
            // System.out.println(positions.size());
            for (int i = 0; i < positions.size(); i++) {
                if (i > 0) {
                    resultText.append(", ");
                }
                int adjustedPosition = positions.get(i) + 1;
                resultText.append(adjustedPosition);
                positionsExist = true;

                if (previousPosition != -1 && previousPosition + 1 != adjustedPosition) {
                    positionsInSameDoc = false;
                }
                previousPosition = adjustedPosition;
            }

            resultText.append("]\n");

            if (!positionsExist) {
                resultText.append("Not exist");
            } else if (!positionsInSameDoc) {
                resultText.append("Positions are in different documents");
            } else {
                String fileName = "f" + (docId + 1) + ".pdf";
                resultText.append("File Name: ").append(fileName).append("\n\n");
            }
        }
        resultArea.setText(resultText.toString());
    }

    public static List<String> documents = new ArrayList<>();

    static {
        BiwordIndex index = new BiwordIndex();
        try {
            for (int i = 1; i <= 10; i++) {
                String fileName = "f" + i + ".pdf"; // Assuming PDF files are named as f1.pdf, f2.pdf, etc.
                String text = readFile(fileName);
                documents.add(text);
                index.addDocument(i - 1, text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (PDDocument document = PDDocument.load(new File(fileName))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            content.append(text);
        }
        return content.toString();
    }
}
