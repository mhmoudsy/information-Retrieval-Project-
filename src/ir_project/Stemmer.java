package ir_project;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.tartarus.snowball.ext.PorterStemmer;

public class Stemmer {
    private static final Map<String, String> LEMMA_RULES = new HashMap<>();
    static {

        LEMMA_RULES.put("are", "be");
    }

    private static final Set<String> STOP_WORDS = new HashSet<>();
    static {
        STOP_WORDS.add("a");
        STOP_WORDS.add("an");
        STOP_WORDS.add("and");
        STOP_WORDS.add("as");
        STOP_WORDS.add("at");
        STOP_WORDS.add("be");
        STOP_WORDS.add("but");
        STOP_WORDS.add("by");
        STOP_WORDS.add("for");
        STOP_WORDS.add("if");
        STOP_WORDS.add("in");
        STOP_WORDS.add("into");
        STOP_WORDS.add("is");
        STOP_WORDS.add("it");
        STOP_WORDS.add("no");
        STOP_WORDS.add("not");
        STOP_WORDS.add("of");
        STOP_WORDS.add("on");
        STOP_WORDS.add("or");
        STOP_WORDS.add("such");
        STOP_WORDS.add("that");
        STOP_WORDS.add("the");
        STOP_WORDS.add("their");
        STOP_WORDS.add("then");
        STOP_WORDS.add("there");
        STOP_WORDS.add("these");
        STOP_WORDS.add("they");
        STOP_WORDS.add("this");
        STOP_WORDS.add("to");
        STOP_WORDS.add("was");
        STOP_WORDS.add("will");
        STOP_WORDS.add("with");
    }

    private static PorterStemmer stemmer = new PorterStemmer();

    public static String stemAndLemmatizeWord(String word) {
        String cleanedWord = removeDotsAndMarks(word);
        if (STOP_WORDS.contains(cleanedWord.toLowerCase(Locale.ROOT))) {
            return null; // Return null if it's a stop word
        }
        String stemmedWord = stemWord(cleanedWord);
        String lemmatizedWord = lemmatizeWord(cleanedWord);
        return lemmatizedWord != null ? lemmatizedWord : stemmedWord;
    }

    private static String removeDotsAndMarks(String word) {
        // Remove dots and marks from the word
        String cleanedWord = word.replaceAll("[.,;:?!]", "");
        // Trim the cleaned word to remove any leading or trailing whitespace
        return cleanedWord.trim();
    }

    private static String stemWord(String word) {
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    private static String lemmatizeWord(String word) {
        for (Map.Entry<String, String> entry : LEMMA_RULES.entrySet()) {
            String pattern = entry.getKey();
            String replacement = entry.getValue();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(word);
            if (m.find()) {
                return m.replaceAll(replacement);
            }
        }
        return null;
    }

    public static String normalizeWord(String word) {
        // Split the input string into words, excluding dots and marks
        String[] words = word.split("[^a-zA-Z0-9]+");
        StringBuilder normalizedWord = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty() && !STOP_WORDS.contains(w.toLowerCase(Locale.ROOT))) {
                normalizedWord.append(w.toLowerCase(Locale.ROOT)).append("\n");
            }
        }
        return normalizedWord.toString().trim();
    }
}
    