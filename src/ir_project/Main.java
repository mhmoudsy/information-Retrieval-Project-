package ir_project;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.lucene.queryparser.classic.ParseException;

public class Main extends Application {
        private Stage stage;

    private TextArea resultTextArea;
private LuceneIndexer luceneIndexer;
private LuceneSearcher luceneSearcher;
 //private TextProcessor textProcessor;
    @Override
    public void start(Stage primaryStage) throws IOException {
        BiwordIndex biwordIndex = new BiwordIndex();
        PositionIndex positionIndex = new PositionIndex();
         luceneIndexer = new LuceneIndexer(); // Initialize LuceneIndexer
         luceneSearcher=new LuceneSearcher();
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50));
        Label titleLabel = new Label("Search Engine Application");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
        titleLabel.setPadding(new Insets(10, 0, 20, 0));
//        root.setTop(titleLabel);

        root.setStyle("-fx-background-color:   #333638;");

        VBox home = new VBox(20);
        home.setPadding(new Insets(25));
        home.setFillWidth(true);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double buttonWidth = screenWidth * 0.2;

        Button biwordButton = createStyledButton("Biword Index");
        biwordButton.setPrefWidth(buttonWidth);
        biwordButton.setOnAction(e -> {
            VBox biwordContent = biwordIndex.createBiwordContent(biwordIndex, root, home);
            root.setCenter(biwordContent);
        });

        Button process2Button = createStyledButton("Positional Index");
        process2Button.setPrefWidth(buttonWidth);
        process2Button.setOnAction(e -> {
            VBox positionContent = positionIndex.createPositionContent(positionIndex, root, home);
            root.setCenter(positionContent);
        });

        Button process3Button = createStyledButton("Tokenization analyzing");
        process3Button.setPrefWidth(buttonWidth);
        process3Button.setOnAction(e -> Tokinization.processTask2(root, home));

        Button process4Button = createStyledButton("Incidence matrix-Inverted Matrix");
        process4Button.setPrefWidth(buttonWidth);
        process4Button.setOnAction(e -> {
            VBox matrixContent = createMatrixContent(root, home);
            root.setCenter(matrixContent);
        });

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setVisible(false);
         
        Button process5Button = createStyledButton("Wildcard Search");
        process5Button.setPrefWidth(buttonWidth);
        process5Button.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Pattern Input");
            dialog.setHeaderText("Enter the pattern:");
            dialog.showAndWait().ifPresent(pattern -> {
                List<String> searchResults = WildcardSearchHandler.performWildcardSearch(pattern);
                if (!searchResults.isEmpty()) {
                    StringBuilder message = new StringBuilder("Search Results:\n");
                    for (String word : searchResults) {
                        message.append(word).append("\n");
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wildcard Search Results");
                    alert.setHeaderText(null);
                    alert.setContentText(message.toString());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wildcard Search Results");
                    alert.setHeaderText(null);
                    alert.setContentText("No results found for the given pattern.");
                    alert.showAndWait();
                }
            });
        });

      Button indexFilesButton = createStyledButton("Index Files -- (UPLOAD FILES) ↑ ");
        indexFilesButton.setPrefWidth(buttonWidth);
       indexFilesButton.setOnAction(e -> {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Files for Indexing");
    List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

    if (selectedFiles != null && !selectedFiles.isEmpty()) {
        try {
            luceneIndexer.indexFiles(selectedFiles);
            showAlert(Alert.AlertType.INFORMATION, "Indexing Complete", "Files indexed successfully.");
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle exception appropriately
        }
    }
});

        Button searchButton = createStyledButton("Search");
        searchButton.setPrefWidth(buttonWidth);
        searchButton.setOnAction(e -> search());
        
    Label wildcardLabel = new Label("Indexer-Searcher:");
   wildcardLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");

    wildcardLabel.setPadding(new Insets(10, 0, 10, 0));
    
          Button stemmingFromFileButton = createStyledButton("Stemming & Normalization -- (UPLOAD FILES) ↑ ");
    stemmingFromFileButton.setPrefWidth(buttonWidth);
    stemmingFromFileButton.setOnAction(e -> performStemmingFromFile(primaryStage));
    
//         Button normalizationFromFileButton = createStyledButton("Normalization from File");
//        normalizationFromFileButton.setPrefWidth(buttonWidth);
//        normalizationFromFileButton.setOnAction(e -> performNormalizationFromFile());
//
//        Button lemmatizationFromFileButton = createStyledButton("Lemmatization from File");
//        lemmatizationFromFileButton.setPrefWidth(buttonWidth);
//        lemmatizationFromFileButton.setOnAction(e -> performLemmatizationFromFile());
 Label Stemming = new Label("Tokenization & Stemming ");
        Stemming.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: #FFFFFF;");
        Stemming.setPadding(new Insets(10, 0, 20, 0));
    
        home.getChildren().addAll( titleLabel,process4Button, biwordButton, process2Button, process5Button,wildcardLabel,indexFilesButton,searchButton,Stemming,process3Button,stemmingFromFileButton);
        root.setCenter(home);

        Scene scene = new Scene(root, 500, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Search index ");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private VBox createMatrixContent(BorderPane root, VBox home) {
        MatrixGUI matrixGUI = new MatrixGUI();
        return matrixGUI.createMatrixContent(root, home);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
        return button;
    }
//    private void indexFiles() {
//        FileChooser fileChooser = new FileChooser();
//fileChooser.setTitle("Select Files for Indexing");
//List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
//        if (selectedFiles != null) {
//            for (File file : selectedFiles) {
//                try {
//                    luceneIndexer.indexFile(file);
//                } catch (Exception e) {
//                    e.printStackTrace(); // Handle exception appropriately
//                }
//            }
//            showAlert(Alert.AlertType.INFORMATION, "Indexing Complete", "Files indexed successfully.");
//        }
//    }

   private void search() {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Search");
    dialog.setHeaderText("Enter the word to search:");
    dialog.setContentText("Word:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(searchWord -> {
        try {
            Map<String, Boolean> searchResults = luceneSearcher.search(searchWord); // Handle exception appropriately
// Display logic
if (!searchResults.isEmpty()) {
    StringBuilder message = new StringBuilder("the word '" + searchWord + "' exists in :"+ "\n");
    int totalFiles = searchResults.size();
    message.append(totalFiles).append(" Files").append("\n\n");
    for (String filename : searchResults.keySet()) {
        message.append(filename).append("\n");
    }
    showAlert(Alert.AlertType.INFORMATION, "Search Results", message.toString());
} else {
    showAlert(Alert.AlertType.INFORMATION, "Search Results", "No occurrences found for the word '" + searchWord + "' in the same folder.");
}

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    });
}   
  
// private void performStemmingFromFile() {
//    FileChooser fileChooser = new FileChooser();
//    fileChooser.setTitle("Select Text File for Stemming");
//    File selectedFile = fileChooser.showOpenDialog(null);
//
//    if (selectedFile != null) {
//        try {
//            StringBuilder stemmedText = new StringBuilder();
//            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] words = line.split("\\s+");
//                for (String word : words) {
//                    String stemmedWord = Stemmer.stemWord(word);
//                    stemmedText.append(word).append(" --> ").append(stemmedWord).append("\n");
//                }
//            }
//            reader.close();
//
//            // Create a new stage to display the results
//            Stage resultStage = new Stage();
//            TextArea resultTextArea = new TextArea(stemmedText.toString());
//            resultTextArea.setEditable(false);
//            resultTextArea.setWrapText(true);
//            Scene scene = new Scene(resultTextArea, 400, 300);
//            resultStage.setScene(scene);
//            resultStage.setTitle("Stemming Results");
//            resultStage.show();
//
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read the selected file.");
//        }
//    }
//}
// 
   
   // Update the performStemmingFromFile method to perform stemming and lemmatization
private void performStemmingFromFile(Stage primaryStage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Text File for Stemming");
    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
        try {
            ObservableList<StemmingResult> stemmingResults = FXCollections.observableArrayList();
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    String stemmedAndLemmatizedWord = Stemmer.stemAndLemmatizeWord(word);
                    String normalizedWord = Stemmer.normalizeWord(word);
                    stemmingResults.add(new StemmingResult(word, stemmedAndLemmatizedWord, normalizedWord));
                }
            }
            reader.close();

            TableView<StemmingResult> table = new TableView<>();
            TableColumn<StemmingResult, String> originalWordCol = new TableColumn<>("Original Word");
            originalWordCol.setCellValueFactory(new PropertyValueFactory<>("originalWord"));
            TableColumn<StemmingResult, String> stemmedWordCol = new TableColumn<>("Stemming/Lemmatization");
            stemmedWordCol.setCellValueFactory(new PropertyValueFactory<>("stemmedAndLemmatizedWord"));
            TableColumn<StemmingResult, String> normalizedWordCol = new TableColumn<>("Normalization");
            normalizedWordCol.setCellValueFactory(new PropertyValueFactory<>("normalizedWord"));

            table.setItems(stemmingResults);
            table.getColumns().addAll(originalWordCol, stemmedWordCol, normalizedWordCol);

            table.prefWidthProperty().bind(primaryStage.widthProperty());
            table.prefHeightProperty().bind(primaryStage.heightProperty());

            VBox root = new VBox(table);
            Stage resultStage = new Stage();
            resultStage.setScene(new Scene(root, 600, 400));
            resultStage.setTitle("Stemming/Lemmatization Results");
            resultStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

   
   
   
   
   
public class StemmingResult {
    private final SimpleStringProperty originalWord;
    private final SimpleStringProperty stemmedAndLemmatizedWord;
    private final SimpleStringProperty normalizedWord;

    public StemmingResult(String originalWord, String stemmedAndLemmatizedWord, String normalizedWord) {
        this.originalWord = new SimpleStringProperty(originalWord);
        this.stemmedAndLemmatizedWord = new SimpleStringProperty(stemmedAndLemmatizedWord);
        this.normalizedWord = new SimpleStringProperty(normalizedWord);
    }

    public String getOriginalWord() {
        return originalWord.get();
    }

    public String getStemmedAndLemmatizedWord() {
        return stemmedAndLemmatizedWord.get();
    }

    public String getNormalizedWord() {
        return normalizedWord.get();
    }
}
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
