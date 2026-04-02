import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("   ROLE-BASED ONLINE QUIZ APPLICATION   ");
        System.out.println("=========================================\n");

        boolean running = true;

        while (running) {
            System.out.println("===== MAIN MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Participant Register");
            System.out.println("3. Participant Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(sc.next().trim());
                switch (choice) {
                    case 1 -> adminMenu();
                    case 2 -> registerParticipant();
                    case 3 -> participantMenu();
                    case 4 -> { running = false; System.out.println("Goodbye!"); }
                    default -> System.out.println("[Error] Enter 1-4.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Please enter a number (1-4).\n");
            }
        }

        MongoDBConnection.close();
        sc.close();
    }

    private static void adminMenu() {
        System.out.print("\nAdmin Username: "); String username = sc.next().trim();
        System.out.print("Admin Password: "); String password = sc.next().trim();

        if (!username.equals("admin") || !password.equals("admin123")) {
            System.out.println("[Auth Error] Invalid admin credentials.\n");
            return;
        }

        Admin admin = new Admin(username, password);
        System.out.println("\nWelcome, Admin " + admin.getUsername() + "!\n");

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("--- ADMIN MENU ---");
            System.out.println("1. Add Question");
            System.out.println("2. View All Questions");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            try {
                int opt = Integer.parseInt(sc.next().trim());
                switch (opt) {
                    case 1 -> admin.addQuestion(sc);
                    case 2 -> admin.viewAllQuestions();
                    case 3 -> { inMenu = false; System.out.println("Admin logged out.\n"); }
                    default -> System.out.println("[Error] Invalid option.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Enter a number.\n");
            }
        }
    }

    private static void registerParticipant() {
        try {
            System.out.print("\nChoose a username: "); String username = sc.next().trim();
            System.out.print("Choose a password: "); String password = sc.next().trim();

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Username and password cannot be empty.");
            }

            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> users = db.getCollection("users");

            if (users.find(new Document("username", username)).first() != null) {
                System.out.println("[Error] Username already exists. Try another.\n");
                return;
            }

            users.insertOne(new Document("username", username)
                    .append("password", password)
                    .append("role", "participant"));
            System.out.println("✔  Registered successfully! You can now login.\n");

        } catch (IllegalArgumentException e) {
            System.out.println("[Input Error] " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("[Error] Registration failed: " + e.getMessage() + "\n");
        }
    }

    private static void participantMenu() {
        try {
            System.out.print("\nUsername: "); String username = sc.next().trim();
            System.out.print("Password: "); String password = sc.next().trim();

            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> users = db.getCollection("users");

            Document found = users.find(new Document("username", username)
                    .append("password", password)
                    .append("role", "participant")).first();

            if (found == null) {
                System.out.println("[Auth Error] Invalid credentials.\n");
                return;
            }

            Participant participant = new Participant(username, password);
            Quiz quiz = new Quiz(sc);
            System.out.println("\nWelcome, " + participant.getUsername() + "!\n");

            boolean inMenu = true;
            while (inMenu) {
                System.out.println("--- PARTICIPANT MENU ---");
                System.out.println("1. Start Quiz");
                System.out.println("2. View My Scores");
                System.out.println("3. Logout");
                System.out.print("Choose: ");
                try {
                    int opt = Integer.parseInt(sc.next().trim());
                    switch (opt) {
                        case 1 -> quiz.startQuiz(participant);
                        case 2 -> quiz.showResult(participant);
                        case 3 -> { inMenu = false; System.out.println("Logged out.\n"); }
                        default -> System.out.println("[Error] Invalid option.\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[Input Error] Enter a number.\n");
                }
            }

        } catch (Exception e) {
            System.out.println("[Error] Login failed: " + e.getMessage() + "\n");
        }
    }
}