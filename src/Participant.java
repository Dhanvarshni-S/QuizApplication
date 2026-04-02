import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

// CONCEPT: Inheritance - Child Class
public class Participant extends User {

    private int score = 0;

    public Participant(String username, String password) {
        super(username, password, "participant");
    }

    public int  getScore()          { return score; }
    public void setScore(int score) { this.score = score; }

    public void saveResult(int score, int total) {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> col = db.getCollection("results");

            Document result = new Document("username", username)
                    .append("score", score)
                    .append("total", total)
                    .append("timestamp", new java.util.Date());

            col.insertOne(result);

        } catch (Exception e) {
            System.out.println("[Error] Could not save result: " + e.getMessage());
        }
    }

    public void viewMyResults() {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> col = db.getCollection("results");

            Document filter = new Document("username", username);
            long count = col.countDocuments(filter);

            if (count == 0) {
                System.out.println("No quiz results found for " + username + ".");
                return;
            }

            System.out.println("\n====== RESULTS FOR: " + username + " ======");
            int attempt = 1;
            try (MongoCursor<Document> cursor = col.find(filter).iterator()) {
                while (cursor.hasNext()) {
                    Document d = cursor.next();
                    System.out.printf("Attempt %d  →  Score: %d / %d%n",
                            attempt++, d.getInteger("score"), d.getInteger("total"));
                }
            }
            System.out.println("======================================\n");

        } catch (Exception e) {
            System.out.println("[Error] Could not fetch results: " + e.getMessage());
        }
    }
}