import java.io.*;
import java.net.*;

public class MiniHttpServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started on port " + PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();

            System.out.println(
                    "Client connected: "
                            + clientSocket.getInetAddress()
            );

            handleClient(clientSocket);
        }
    }

    private static void handleClient(Socket clientSocket) {

        try (
                Socket socket = clientSocket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                )
        ) {

            String requestLine = reader.readLine();

            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            System.out.println("Request: " + requestLine);

            String[] requestParts = requestLine.split(" ");

            if (requestParts.length != 3) {
                System.out.println("Invalid HTTP request");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];
            String version = requestParts[2];

            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            System.out.println("Version: " + version);

            String line;

            while ((line = reader.readLine()) != null
                    && !line.isEmpty()) {

                System.out.println("Header: " + line);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}