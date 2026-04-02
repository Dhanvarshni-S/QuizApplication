import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.Scanner;

// CONCEPT: Inheritance - Child Class
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, "admin");
    }

    public void addQuestion(Scanner sc) {
        try {
            System.out.print("Enter question text : ");
            sc.nextLine();
            String text = sc.nextLine().trim();

            if (text.isEmpty()) {
                throw new IllegalArgumentException("Question text cannot be empty.");
            }

            System.out.print("Option A : "); String optA = sc.nextLine().trim();
            System.out.print("Option B : "); String optB = sc.nextLine().trim();
            System.out.print("Option C : "); String optC = sc.nextLine().trim();
            System.out.print("Option D : "); String optD = sc.nextLine().trim();

            System.out.print("Correct answer (A/B/C/D) : ");
            String answer = sc.nextLine().trim().toUpperCase();

            if (!answer.equals("A") && !answer.equals("B")
                    && !answer.equals("C") && !answer.equals("D")) {
                throw new IllegalArgumentException("Answer must be A, B, C, or D.");
            }

            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> col = db.getCollection("questions");

            Document qDoc = new Document("questionText", text)
                    .append("optionA", optA)
                    .append("optionB", optB)
                    .append("optionC", optC)
                    .append("optionD", optD)
                    .append("answer", answer);

            col.insertOne(qDoc);
            System.out.println("✔  Question added successfully!\n");

        } catch (IllegalArgumentException e) {
            System.out.println("[Input Error] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Error] Could not add question: " + e.getMessage());
        }
    }

    public void viewAllQuestions() {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> col = db.getCollection("questions");

            long count = col.countDocuments();
            if (count == 0) {
                System.out.println("No questions found in the database.");
                return;
            }

            System.out.println("\n========== ALL QUESTIONS (" + count + ") ==========");
            int idx = 1;
            try (MongoCursor<Document> cursor = col.find().iterator()) {
                while (cursor.hasNext()) {
                    Document d = cursor.next();
                    System.out.println("\nQ" + idx++ + ": " + d.getString("questionText"));
                    System.out.println("   A) " + d.getString("optionA"));
                    System.out.println("   B) " + d.getString("optionB"));
                    System.out.println("   C) " + d.getString("optionC"));
                    System.out.println("   D) " + d.getString("optionD"));
                    System.out.println("   ✔ Answer: " + d.getString("answer"));
                }
            }
            System.out.println("===========================================\n");

        } catch (Exception e) {
            System.out.println("[Error] Could not fetch questions: " + e.getMessage());
        }
    }
}