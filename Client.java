import java.io.BufferedReader; // Provide method (.readLine()) to read input from the server
import java.io.IOException;
import java.io.InputStreamReader; // Convert byte code into readable text
import java.io.PrintWriter; // Convert readable text into byte code
import java.net.Socket; // Create a socket connection and connect to the server

public class Client {

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("serveo.net", 5005); // Create a socket connection on port 5005
            BufferedReader serverReader = new BufferedReader(
                // Create a buffered reader for reading from the server
                new InputStreamReader(socket.getInputStream()) // getInputStream gives byte code from server
            );
            PrintWriter serverWriter = new PrintWriter(
                socket.getOutputStream(), // getOutputStream gives byte code to server
                true
            );
            BufferedReader userInput = new BufferedReader(
                new InputStreamReader(System.in) // Takes raw input from the user
            );
        ) {
            System.out.println("Connected to server.");

            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String message;
                            while (
                                (message = serverReader.readLine()) != null // Until Server disconnects
                            ) {
                                System.out.println(message);
                            }
                        } catch (IOException e) {
                            System.out.println("Disconnected from server.");
                        }
                    }
                }
            ).start(); // Start the thread to immediately read messages from the server

            String input;
            while ((input = userInput.readLine()) != null) { // Read user input until user disconnects
                serverWriter.println(input); // Send user input to server
                if (input.equalsIgnoreCase("/quit")) {
                    System.out.println("Disconnected from server.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
