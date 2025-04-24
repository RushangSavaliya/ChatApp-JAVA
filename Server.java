import java.io.BufferedReader; // Give method like .readLine() to read lines from the client
import java.io.IOException;
import java.io.InputStreamReader; // Convert bytes to characters
import java.io.PrintWriter; // Convert characters to bytes
import java.net.ServerSocket; // Open connection to client
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private final CopyOnWriteArrayList<ClientHandler> clients =
        new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        new Server().start(5005);
    }

    public void start(int port) { // Connect new clients and handle messages
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);

            while (true) {
                Socket socket = serverSocket.accept(); // Wait for a client to connect
                ClientHandler handler = new ClientHandler(socket, this); // Reference to current class so it can access all methods of this class
                clients.add(handler);
                new Thread(handler).start(); // Start a new thread to handle the client
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public void broadcast(String message) { // Broadcast message to all clients
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    public void remove(ClientHandler client) { // close ClientHandler thread and remove client
        clients.remove(client); // Remove Client's handler thread from list of clients
    }
}

class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your name:");
            name = in.readLine();
            server.broadcast(name + " joined the chat.");

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("/quit")) {
                    break;
                }
                server.broadcast(name + ": " + msg);
            }
        } catch (IOException e) {
            System.out.println("Connection lost: " + socket.getInetAddress());
        } finally {
            server.remove(this);
            server.broadcast(name + " left the chat.");
            try {
                socket.close();
            } catch (IOException ignored) {
                ignored.printStackTrace(); // We can't handle this anymore
            }
        }
    }

    public void send(String message) { // Send message to client
        out.println(message);
    }
}
