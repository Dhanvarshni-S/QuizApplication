import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Routes
        server.createContext("/api/admin/login", Handlers::adminLogin);
        server.createContext("/api/participant/register", Handlers::participantRegister);

        server.setExecutor(null); // optional but good practice

        server.start();
        System.out.println("🚀 Server running at http://localhost:8080");
    }
}