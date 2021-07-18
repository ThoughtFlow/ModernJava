package chap14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Simplest Socket server using Java NIO. Will echo back text from a client.
 */
public class IoEchoServer {

    private final static int port = 8080;
    public static final String STOP_SERVER = "Shutdown";
    public static final String END_CHAT = "Bye";

    private static void runServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Listening on port " + port);

            String lastLine = null;

            while (!STOP_SERVER.equalsIgnoreCase(lastLine)) {

                try (Socket clientSocket = serverSocket.accept();) {
                    // Since Java IO is blocking and this program is single threaded, only one client can connect at a time.
                    // This portion would have to be delegated to another thread to make it non-blocking.
                    // Each connection would be assigned to its own thread - not optimal.
                    lastLine = "";
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                        while (!END_CHAT.equalsIgnoreCase(lastLine) && !STOP_SERVER.equalsIgnoreCase(lastLine)) {
                            lastLine = reader.readLine();
                            writer.write("Received: " + lastLine);
                            writer.newLine();
                            writer.flush();
                        }
                    }
                    catch (SocketException e) {
                        // Keep going with new connection in the event of failure.
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        runServer();
    }
}