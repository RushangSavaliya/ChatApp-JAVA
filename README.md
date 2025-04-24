# ChatApp-JAVA


A simple real-time chat application built in Java, enabling multiple clients to connect to a server and exchange messages. This project demonstrates Java networking (`Socket` and `ServerSocket`), multi-threading, and basic I/O operations.

## Features

- **Multi-Client Support**: Allows simultaneous connections from multiple clients.
- **Real-Time Messaging**: Broadcasts messages to all connected clients instantly.
- **User Commands**:
    - `/quit`: Disconnect gracefully.
- **Threaded Architecture**: Each client connection is managed in a separate thread for efficient communication.

## Prerequisites

- Java Development Kit (JDK) installed.
- Basic understanding of Java and networking concepts.
- Internet connection for remote server access via `serveo.net`.

## How to Run

### 1. Start the Server

1. Open a terminal and navigate to the project directory.
2. Compile the Java files:
     ```bash
     javac *.java
     ```
3. Start the server:
     ```bash
     java Server
     ```
4. Set up reverse tunneling:
     ```bash
     ssh -R 5005:localhost:5005 serveo.net
     ```

### 2. Start the Client

1. Run the client application:
     ```bash
     java Client
     ```
     (Run this command multiple times to connect multiple clients.)

### Stopping the Application

- Press `Ctrl+C` to terminate the server or client.

