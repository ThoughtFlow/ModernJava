package lab20.fin;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerFunctional implements Runnable, Closeable {

    private static final String USAGE = "USAGE: ChatServer port";
    private static final String BYE_MESSAGE = "BYE";
    private static final Logger logger = Logger.getLogger(ChatServerFunctional.class.getName());

    private final ChatRoomManager chatRoomManager = new ChatRoomManager();
    private final InetSocketAddress daemonAddress;
    private Selector selector;

    private final IoFunction<InetSocketAddress, Selector> daemonProcessor;
    private final IoFunction<SelectionKey, SocketChannel> newConnectionManager;
    private final IoBiFunction<SocketChannel, SelectionKey, SelectionKey> readRegistrator;
    private final IoFunction<SelectionKey, String> messageReader;
    private final IoBiConsumer<SelectionKey, String> messageWriter;

    public ChatServerFunctional(InetSocketAddress daemonAddress,
                                IoFunction<InetSocketAddress, Selector> daemonProcessor,
                                IoFunction<SelectionKey, SocketChannel> newConnectionManager,
                                IoBiFunction<SocketChannel, SelectionKey, SelectionKey> readRegistrator,
                                IoFunction<SelectionKey, String> messageReader,
                                IoBiConsumer<SelectionKey, String> messageWriter) {

        if (daemonAddress.isUnresolved()) {
            throw new IllegalArgumentException("Unresolved daemonAddress: " + daemonAddress);
        }

        this.daemonAddress = daemonAddress;
        this.daemonProcessor = daemonProcessor;
        this.newConnectionManager = newConnectionManager;
        this.readRegistrator = readRegistrator;
        this.messageReader = messageReader;
        this.messageWriter = messageWriter;
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

        logger.info("Chat server is ready on port " + daemonAddress);
        try {
            selector = daemonProcessor.apply(daemonAddress);

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

        SocketChannel socketChannel = newConnectionManager.apply(selectionKey);
        SelectionKey clientSelectionKey = readRegistrator.apply(socketChannel, selectionKey);

        UserData userData = new UserData(socketChannel, clientSelectionKey);
        sendGreetings(userData);
        clientSelectionKey.attach(userData);
    }

    private void managingIncomingMessage(SelectionKey selectionKey) throws IOException {

        String message = messageReader.apply(selectionKey);
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

        messageWriter.accept(selectionKey, message);
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

    public static void main(String[] args) {

        IoFunction<InetSocketAddress, Selector> daemonProcessor = daemonAddress -> {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(daemonAddress);

                Selector selector = Selector.open();

                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

                return selector;
        };

        IoFunction<SelectionKey, SocketChannel> newConnectionManager = selectionKey -> {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            return socketChannel;

        };

        IoBiFunction<SocketChannel, SelectionKey, SelectionKey> readRegistrator = (socketChannel, selectionKey) -> socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);

        IoFunction<SelectionKey, String> messageReader = selectionKey -> {
            ByteBuffer senderBuffer = ByteBuffer.allocate(2048);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            int bytesRead = socketChannel.read(senderBuffer);

            String message = null;
            if (bytesRead > 0) {
                senderBuffer.flip();

                byte[] bytes = new byte[bytesRead];
                senderBuffer.get(bytes);
                message = new String(bytes, 0, bytesRead);
                message = message.trim();
                senderBuffer.clear();
            }

            return message;
        };

        IoBiConsumer<SelectionKey, String> messageWriter = (selectionKey, message) -> {
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            selectionKey.interestOps(SelectionKey.OP_READ);
        };

        if (args.length != 1) {
            System.err.println(USAGE);
        }
        else {
            try {
                final int port = Integer.parseInt(args[0]);
                final InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
                final ChatServerFunctional chatServer = new ChatServerFunctional(inetSocketAddress, daemonProcessor, newConnectionManager, readRegistrator, messageReader, messageWriter);

                // start the server in its own thread
                Thread serverThread = new Thread(chatServer);
                serverThread.start();
                serverThread.join();
            }
            catch (NumberFormatException exception) {
                System.err.println("Please enter a valid number for the port");
                System.err.println(USAGE);
            }
            catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}
