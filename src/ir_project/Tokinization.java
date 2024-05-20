package ir_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class Tokinization {

    public static void processTask2(BorderPane root, VBox home) {
        StringBuilder textBuilder = new StringBuilder();
        try {
            File file = new File("TOKINIZATION.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = textBuilder.toString();

        TextArea contentTextArea = new TextArea(text);
        contentTextArea.setEditable(false); // Ensure text cannot be edited
        
        StringBuilder result = new StringBuilder();
        result.append(analyzeWithAnalyzer(new StandardAnalyzer(Version.LUCENE_41), text, "StandardAnalyzer")).append("\n");//lowercase special carahater
        result.append(analyzeWithAnalyzer(new WhitespaceAnalyzer(Version.LUCENE_41), text, "WhitespaceAnalyzer")).append("\n");
        result.append(analyzeWithAnalyzer(new SimpleAnalyzer(Version.LUCENE_41), text, "SimpleAnalyzer")).append("\n");//lowercase marks deleted special character
        result.append(analyzeWithAnalyzer(new StopAnalyzer(Version.LUCENE_41), text, "StopAnalyzer")).append("\n");

        TextArea resultTextArea = new TextArea(result.toString());
        resultTextArea.setEditable(false); // Ensure text cannot be edited
                    resultTextArea.setPrefSize(300, 200);

        Button backButton = new Button("⬅ Back");
backButton.setStyle("-fx-background-color: #551606; -fx-text-fill: white; -fx-font-weight: 900; -fx-pref-width: 10000px ;-fx-pref-height: 70px;");
backButton.setOnAction(e -> root.setCenter(home));

// Set preferred width and height
backButton.setPrefWidth(100); // Adjust width as needed
backButton.setPrefHeight(40); // Adjust height as needed

        VBox contentBox = new VBox(contentTextArea, resultTextArea, backButton);
        root.setCenter(contentBox);
    }

    private static String analyzeWithAnalyzer(Analyzer analyzer, String text, String analyzerName) {
        StringBuilder result = new StringBuilder();
        try (TokenStream tokenStream = analyzer.tokenStream("field", new StringReader(text))) {
            tokenStream.reset();
            CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                String term = termAttribute.toString();
                result.append(term).append(" , ");
            }

            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return analyzerName + " => [" + result.substring(0, result.length() - 2) + "]";
    }
}
