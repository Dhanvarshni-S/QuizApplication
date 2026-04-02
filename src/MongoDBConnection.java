import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
public class MongoDBConnection {
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "quizDB";

    private MongoDBConnection() {}

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("[DB] Connected to MongoDB successfully.");
            } catch (Exception e) {
                System.out.println("[DB ERROR] Could not connect: " + e.getMessage());
                throw new RuntimeException("MongoDB connection failed.", e);
            }
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("[DB] MongoDB connection closed.");
        }
    }
}