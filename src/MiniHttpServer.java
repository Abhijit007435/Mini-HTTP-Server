import java.io.*;
import java.net.*;
import java.nio.file.*;

public class MiniHttpServer {

    private static final int PORT = 8080;
    private static final String ROOT_DIRECTORY = "static";

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started on port " + PORT);

        while (true) {

            Socket clientSocket = serverSocket.accept();

System.out.println(
        "Client connected: "
                + clientSocket.getInetAddress()
);

Thread clientThread = new Thread(
        () -> handleClient(clientSocket)
);

clientThread.start();
        }
    }

    private static void handleClient(Socket clientSocket) {

        try (
                Socket socket = clientSocket;

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

                OutputStream outputStream =
                        socket.getOutputStream()
        ) {

            String requestLine = reader.readLine();

            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            System.out.println("Request: " + requestLine);

            String[] requestParts = requestLine.split(" ");

            if (requestParts.length != 3) {
                sendResponse(
                        outputStream,
                        "400 Bad Request",
                        "text/plain",
                        "Bad Request"
                );
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            String headerLine;

            while ((headerLine = reader.readLine()) != null
                    && !headerLine.isEmpty()) {

                System.out.println("Header: " + headerLine);
            }

            if (!method.equals("GET")) {
                sendResponse(
                        outputStream,
                        "405 Method Not Allowed",
                        "text/plain",
                        "Only GET is supported"
                );
                return;
            }

            if (path.equals("/")) {
                path = "/index.html";
            }

            Path filePath = Paths.get(
                    ROOT_DIRECTORY + path
            );

            if (!Files.exists(filePath)
                    || !Files.isRegularFile(filePath)) {

                sendResponse(
                        outputStream,
                        "404 Not Found",
                        "text/plain",
                        "File Not Found"
                );

                return;
            }

            byte[] fileContent = Files.readAllBytes(filePath);

            String contentType = getContentType(filePath);

            sendResponse(
                    outputStream,
                    "200 OK",
                    contentType,
                    fileContent
            );

        } catch (IOException e) {
            System.out.println(
                    "Client error: " + e.getMessage()
            );
        }
    }

    private static void sendResponse(
            OutputStream outputStream,
            String status,
            String contentType,
            String body) throws IOException {

        sendResponse(
                outputStream,
                status,
                contentType,
                body.getBytes()
        );
    }

    private static void sendResponse(
            OutputStream outputStream,
            String status,
            String contentType,
            byte[] body) throws IOException {

        String headers =
                "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        outputStream.write(headers.getBytes());
        outputStream.write(body);
        outputStream.flush();
    }

    private static String getContentType(Path filePath) {

        String fileName =
                filePath.toString().toLowerCase();

        if (fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".css")) {
            return "text/css";
        }

        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        if (fileName.endsWith(".txt")) {
            return "text/plain";
        }

        if (fileName.endsWith(".png")) {
            return "image/png";
        }

        if (fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        return "application/octet-stream";
    }
}