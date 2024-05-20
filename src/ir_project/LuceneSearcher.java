package ir_project;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

public class LuceneSearcher {
    private static final String INDEX_DIRECTORY = "index";

public Map<String, Boolean> search(String searchQuery) throws IOException, ParseException {
    Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIRECTORY).toFile());
    IndexReader reader = DirectoryReader.open(indexDirectory);
    IndexSearcher searcher = new IndexSearcher(reader);

    Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_42);
    QueryParser queryParser = new QueryParser(Version.LUCENE_42, "contents", analyzer);

    // Parse the search query
    Query query = queryParser.parse(searchQuery);

    // Search the index
    TopDocs topDocs = searcher.search(query, 10);

    // Create a map to store search results
    Map<String, Boolean> searchResults = new HashMap<>();

    // Iterate over the search results
    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        int docId = scoreDoc.doc;
        Document document = searcher.doc(docId);
        String filename = document.get("filename");
        searchResults.put(filename, true); // Indicate presence of match
    }

    // Close the reader
    reader.close();

    return searchResults;
}
//private int countOccurrences(String fieldValue, String searchQuery) {
//    if (fieldValue == null) {
//        return 0; // Return 0 occurrences if fieldValue is null
//    }
//
//    String[] words = fieldValue.split("\\s+"); // Split the content into words
//    int count = 0;
//    for (String word : words) {
//        if (word.equalsIgnoreCase(searchQuery)) { // Check for case-insensitive match
//            count++;
//        }
//    }
//    return count;
//}
}