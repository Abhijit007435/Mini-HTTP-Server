import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

            clientSocket.close();
        }
    }
}