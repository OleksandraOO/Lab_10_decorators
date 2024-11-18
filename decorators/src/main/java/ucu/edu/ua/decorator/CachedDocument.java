package ucu.edu.ua.decorator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CachedDocument implements Document {
    private Document document;

    // Getter for the GCS path of the document
    public String getGcsPath() {
        return document.getGcsPath();
    }

    @Override
    public String parse() {
        // Check local SQLite cache for the parsed document
        String cached = DBConnection.getInstance().getDocument(document.getGcsPath());
        if (cached != null) {
            // Cache hit: Return cached result
            System.out.println("Cache hit for: " + getGcsPath());
            return cached;
        } else {
            // Cache miss: Parse using Google Cloud Vision
            System.out.println("Cache miss for: " + getGcsPath());
            String parsed = document.parse();

            // Store parsed result in the cache
            DBConnection.getInstance().createDocument(getGcsPath(), parsed);

            return parsed;
        }
    }
}