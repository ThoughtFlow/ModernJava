package chap14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simplest Socket server using Java NIO. Will echo back text from a client.
 */
public class IoEchoServer {

    private final static int port = 8080;
    public static final String STOP_SERVER = "Stop";
    public static final String END_CHAT = "Bye";

    private static void runServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("Listening on port " + port);

            String lastLine = "";

            while (!lastLine.equalsIgnoreCase(STOP_SERVER)) {
                lastLine = "";
                try (Socket clientSocket = serverSocket.accept();) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                        while (!lastLine.equalsIgnoreCase(END_CHAT) && !lastLine.equalsIgnoreCase(STOP_SERVER)) {
                            lastLine = reader.readLine();
                            writer.write("Received: " + lastLine);
                            writer.newLine();
                            writer.flush();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        runServer();
    }
}