package motioncut2;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class URLShortener {
    private static final String BASE_URL = "http://short.ly/";
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private Map<String, String> urlMap;
    private Map<String, String> reverseUrlMap;
    private static final String FILE_NAME = "url_mappings.txt";

    public URLShortener() {
        urlMap = new HashMap<>();
        reverseUrlMap = new HashMap<>();
        loadMappingsFromFile();
    }

    // Load URL mappings from a file
    private void loadMappingsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    urlMap.put(parts[0], parts[1]);
                    reverseUrlMap.put(parts[1], parts[0]);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing mappings found, starting fresh.");
        }
    }

    // Save URL mappings to a file
    private void saveMappingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving URL mappings to file.");
        }
    }

    // Function to validate long URLs
    private boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    // Function to shorten a URL
    public String shortenURL(String longURL) throws InvalidURLException {
        if (!isValidURL(longURL)) {
            throw new InvalidURLException("Invalid URL format: " + longURL);
        }

        if (reverseUrlMap.containsKey(longURL)) {
            return BASE_URL + reverseUrlMap.get(longURL);
        }

        String shortURL = generateShortURL();
        while (urlMap.containsKey(shortURL)) {
            shortURL = generateShortURL();
        }

        urlMap.put(shortURL, longURL);
        reverseUrlMap.put(longURL, shortURL);
        saveMappingsToFile(); // Save every time a new mapping is created

        return BASE_URL + shortURL;
    }

    // Function to expand a short URL to its original form
    public String expandURL(String shortURL) {
        String key = shortURL.replace(BASE_URL, "");
        return urlMap.getOrDefault(key, "URL not found");
    }

    // Generate a random short URL string
    private String generateShortURL() {
        StringBuilder shortURL = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = (int) (Math.random() * CHARACTERS.length());
            shortURL.append(CHARACTERS.charAt(index));
        }
        return shortURL.toString();
    }

    // Custom exception class for invalid URLs
    public static class InvalidURLException extends Exception {
        public InvalidURLException(String message) {
            super(message);
        }
    }
}
