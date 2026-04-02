import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Handlers {

    // 🔥 COMMON CORS METHOD
    private static void addCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    // 🔹 ADMIN LOGIN
    public static void adminLogin(HttpExchange exchange) throws IOException {

        addCORS(exchange);

        // 🔥 HANDLE PREFLIGHT
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {

            System.out.println("Admin login request received");

            // ✅ send success response (IMPORTANT)
            String response = "{\"success\": true}";

            sendResponse(exchange, response);
        }
    }

    // 🔹 PARTICIPANT REGISTER
    public static void participantRegister(HttpExchange exchange) throws IOException {

        addCORS(exchange);

        // 🔥 HANDLE PREFLIGHT
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {

            BufferedReader br = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
            );

            StringBuilder body = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                body.append(line);
            }

            System.out.println("Register body: " + body.toString());

            // MongoDB
            MongoDatabase db = MongoDBConnection.getDatabase();
            MongoCollection<Document> users = db.getCollection("users");

            Document user = Document.parse(body.toString());
            users.insertOne(user);

            // ✅ IMPORTANT: success response
            String response = "{\"success\": true}";

            sendResponse(exchange, response);
        }
    }

    // 🔹 COMMON RESPONSE METHOD
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}