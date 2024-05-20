package ir_project;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LuceneIndexer {
    private static final String INDEX_DIRECTORY = "index";

    public void indexFiles(List<File> files) throws IOException {
        // Delete existing index directory if it exists
        File indexDir = new File(INDEX_DIRECTORY);
        if (indexDir.exists()) {
            deleteDirectory(indexDir);
        }

        Directory indexDirectory = FSDirectory.open(indexDir);
        SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_42);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

        try {
            for (File file : files) {
                Document doc = new Document();

                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    // If the file is a PDF, extract text using PDFBox
                    try (PDDocument pdfDocument = PDDocument.load(file)) {
                        PDFTextStripper pdfStripper = new PDFTextStripper();
                        String text = pdfStripper.getText(pdfDocument);
                        doc.add(new Field("contents", text, Field.Store.NO, Field.Index.ANALYZED));
                    }
                } else if (file.getName().toLowerCase().endsWith(".txt")) {
                    // If the file is a txt, read contents as text
                    doc.add(new Field("contents", new FileReader(file)));
                } else {
                    // Handle unsupported file types or skip them
                    continue;
                }

                doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));

                writer.addDocument(doc);
            }
        } finally {
            writer.close(); // Close the writer in a finally block to ensure it's always closed
        }
    }

    private void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}
