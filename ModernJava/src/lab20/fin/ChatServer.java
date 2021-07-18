package lab20.fin;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server implementation.
 */
public class ChatServer implements Runnable, Closeable {

    private  static final int DEFAULT_PORT = 8080;
    private static final String USAGE = "USAGE: ChatServer [port] (default port is " + DEFAULT_PORT + ")";
    private static final String BYE_MESSAGE = "BYE";
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    private final ChatRoomManager chatRoomManager = new ChatRoomManager();
    private final InetSocketAddress daemonAddress;
    private Selector selector;

    private final IoFunction<InetSocketAddress, Selector> registerAcceptConnection;
    private final IoFunction<SelectionKey, SocketChannel> acceptConnection;
    private final IoFunction<SelectionKey, String> readFromSocket;
    private final IoBiConsumer<SelectionKey, String> writeToSocket;

    public ChatServer(InetSocketAddress daemonAddress,
                      IoFunction<InetSocketAddress, Selector> registerAcceptConnection,
                      IoFunction<SelectionKey, SocketChannel> acceptConnection,
                      IoFunction<SelectionKey, String> readFromSocket,
                      IoBiConsumer<SelectionKey, String> writeToSocket) {

        if (daemonAddress.isUnresolved()) {
            throw new IllegalArgumentException("Unresolved daemonAddress: " + daemonAddress);
        }

        this.daemonAddress = daemonAddress;
        this.registerAcceptConnection = registerAcceptConnection;
        this.acceptConnection = acceptConnection;
        this.readFromSocket = readFromSocket;
        this.writeToSocket = writeToSocket;
    }

    @Override
    public void close() {
        if (selector != null && selector.isOpen()) {

            logger.info("Shutting down");
            try {
                selector.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to shut-down. Ignoring.", e);
            }
        }
    }

    public void run() {
        try {
            selector = registerAcceptConnection.apply(daemonAddress);

            logger.info("Chat server is ready on port " + daemonAddress);

            while (selector.isOpen()) {
                // This will block until a client connects
                selector.select();
                if (!selector.isOpen()) {
                    break;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    iterator.remove();
                    try {
                        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                            manageNewConnections(selectionKey);
                        }
                        if (selectionKey.isValid() && selectionKey.isReadable()) {
                            managingIncomingMessage(selectionKey);
                        }
                        if (selectionKey.isValid() && selectionKey.isWritable()) {
                            manageOutgoingMessage(selectionKey);
                        }
                    } catch (IOException e) {
                        logger.log( Level.WARNING, "Encountered an I/O error while processing I/O client requests. Ignoring", e);
                    }
                }
            }
        }
        catch (ClosedSelectorException exception) {
            logger.info("Shut down");
        } catch (IOException exception) {
            logger.info("Caught error on close: " + exception);
        }
    }

    private void manageNewConnections(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = acceptConnection.apply(selectionKey);
        SelectionKey clientSelectionKey = socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);

        UserData userData = new UserData(socketChannel, clientSelectionKey);
        sendGreetings(userData);
        clientSelectionKey.attach(userData);
    }

    private void managingIncomingMessage(SelectionKey selectionKey) throws IOException {

        String message = readFromSocket.apply(selectionKey);
        UserData senderUserData = (UserData) selectionKey.attachment();

        if (message != null) {

            if (senderUserData.isUserIdAndTopicSet()) {
                if (BYE_MESSAGE.equalsIgnoreCase(message)) {
                    manageUserLeavingChatRoom(senderUserData);
                }
                else {
                    manageChatMessage(senderUserData, message);
                }
            }
            else {
                manageUserEnteringChatRoom(senderUserData, message);
            }
        }
        else {
            manageUserLeavingChatRoom(senderUserData);
        }
    }

    private void manageOutgoingMessage(SelectionKey selectionKey) throws IOException {

        UserData userData = (UserData) selectionKey.attachment();
        String message = userData.getNextMessage();

        writeToSocket.accept(selectionKey, message);
        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    private void manageUserEnteringChatRoom(UserData enteringUserData, String identification) {
        enteringUserData.setUserIdAndTopic(identification);
        if (enteringUserData.isUserIdAndTopicSet()) {
            chatRoomManager.addToChatRoom(enteringUserData.getChatRoom(), enteringUserData);
            int otherParticipants = chatRoomManager.getParticipants(enteringUserData.getChatRoom()) - 1;

            String welcomeMessage =
                    "Welcome to " + enteringUserData.getChatRoom() + "! There are " + otherParticipants + " other participant(s)\r\n" +
                            "Type " + BYE_MESSAGE + " to exit\r\n";
            sendDirectMessage(enteringUserData, welcomeMessage);

            welcomeMessage = enteringUserData.getUserId() + " has joined the chat room";
            manageChatMessage(enteringUserData,  welcomeMessage);
        }
    }

    private void manageChatMessage(UserData senderUserData, String message) {

        List<UserData> recipients = chatRoomManager.getUserDataForChatRoom(senderUserData.getChatRoom());
        message = message + "\r\n";
        sendMessageToRecipients(senderUserData, message, recipients);
    }

    private void manageUserLeavingChatRoom(UserData leavingUserData) throws IOException {
        chatRoomManager.removeFromChatRoom(leavingUserData.getChatRoom(), leavingUserData);
        String message = leavingUserData.getUserId() + " has left the chat room";
        leavingUserData.getChannel().close();

        List<UserData> recipients = chatRoomManager.getUserDataForChatRoom(leavingUserData.getChatRoom());
        message = message + "\r\n";
        sendMessageToRecipients(leavingUserData, message, recipients);
    }

    private void sendGreetings(UserData userData) throws IOException {
        userData.addMessage("Enter userId & chat room: ");
        userData.getSelectionKey().interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void sendMessageToRecipients(UserData senderUserData, String message, List<UserData> recipients) {
        message = "[" + senderUserData.getUserId() + "] " + message;

        for (UserData nextUserData : recipients) {
            if (nextUserData != senderUserData) {
                sendDirectMessage(nextUserData, message);
            }
        }
    }

    private void sendDirectMessage(UserData destinationUserData, String message) {
        destinationUserData.addMessage(message);
        destinationUserData.getSelectionKey().interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private static void runServer(int port,
                                  IoFunction<InetSocketAddress, Selector> registerAcceptConnection,
                                  IoFunction<SelectionKey, SocketChannel> acceptConnection,
                                  IoFunction<SelectionKey, String> readFromSocket,
                                  IoBiConsumer<SelectionKey, String> writeToSocket) {


        try {
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            final ChatServer chatServer =
                    new ChatServer(inetSocketAddress, registerAcceptConnection, acceptConnection,
                            readFromSocket, writeToSocket);

            // start the server in its own thread
            Thread serverThread = new Thread(chatServer);
            serverThread.start();
            serverThread.join();

        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Consumer<Integer> runServer = port -> {
            runServer(port,
                    SocketFunctions.registerAcceptConnection,
                    SocketFunctions.acceptConnection,
                    SocketFunctions.readFromSocket,
                    SocketFunctions.writeToSocket);
        };

        switch (args.length) {
            case 0 -> runServer.accept(DEFAULT_PORT);
            case 1 -> {
                try {
                    final int port = Integer.parseInt(args[0]);
                    runServer.accept(port);
                }
                catch (NumberFormatException exception) {
                    System.err.println("Please enter a valid number for the port");
                    System.err.println(USAGE);
                }
            }
            default -> System.err.println(USAGE);
        }
    }
}
