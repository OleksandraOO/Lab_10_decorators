package ucu.edu.ua.decorator;

import java.time.Duration;
import java.time.LocalTime;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimedDocument implements Document {
    private Document doc;

    @Override
    public String parse() {
        LocalTime start = LocalTime.now();
        String result = doc.parse();
        LocalTime end = LocalTime.now();
        System.out.println("Time: " + Duration.between(start, end).toMillis() + "ms");
        return result;
    }
    
    public String getGcsPath() {
        return doc.getGcsPath();
    }
}

