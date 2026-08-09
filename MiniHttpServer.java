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

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    )
            );

            String line;

            while ((line = reader.readLine()) != null
                    && !line.isEmpty()) {

                System.out.println(line);
            }

            clientSocket.close();
        }
    }
}