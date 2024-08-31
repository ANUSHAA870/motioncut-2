package motioncut2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        URLShortener urlShortener = new URLShortener();
        Scanner scanner = new Scanner(System.in);
        String choice;

        System.out.println("Welcome to the Link Shortener Application!");

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Expand a URL");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter the long URL: ");
                    String longURL = scanner.nextLine();
                    try {
                        String shortURL = urlShortener.shortenURL(longURL);
                        System.out.println("Shortened URL: " + shortURL);
                    } catch (URLShortener.InvalidURLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "2":
                    System.out.print("Enter the short URL: ");
                    String inputShortURL = scanner.nextLine();
                    String originalURL = urlShortener.expandURL(inputShortURL);
                    System.out.println("Original URL: " + originalURL);
                    break;

                case "3":
                    System.out.println("Exiting the application...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("3"));

        scanner.close();
    }
}

