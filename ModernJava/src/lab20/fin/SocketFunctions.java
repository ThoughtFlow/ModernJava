package lab20.fin;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface SocketFunctions {

    /**
     * This function is for the registration to receive new connections.
     *
     * Implement a function that:
     *   - Receives a socket address,
     *   - Opens a ServerSocketChannel
     *   - Configures it to be non-blocking
     *   - Binds the received socket address to the ServerSocketChannel
     *   - Creates the selector
     *   - Registers the selector accept new connections
     *   - Returns the selector
     */
    IoFunction<InetSocketAddress, Selector> registerAcceptConnection = socketAddress -> {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(socketAddress);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        return selector;
    };

    /**
     * This function is to accept a new connection.
     *
     * Implement a function that:
     *   - Receives a selection key for the new connection
     *   - Accepts the connection
     *   - Configures it to be non-blocking
     *   - Returns the socket channel attached to the selection key
     */
    IoFunction<SelectionKey, SocketChannel> acceptConnection = selectionKey -> {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        return socketChannel;
    };

    /**
     * This function is to read from the socket.
     *
     * Implement a function that:
     *   - Receives a selection key for the new connection
     *   - Allocates a ByteBuffer of 2048 bytes
     *   - Reads from the socket channel into the byte buffer
     *   - Flips the buffer
     *   - Places the byte buffer inside a string
     *   - Returns the string
     */
    IoFunction<SelectionKey, String> readFromSocket = selectionKey -> {

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

    /**
     * This BiConsumer is to write to the socket.
     *
     * Implement a BiConsumer that:
     *   - Receives a selection key and string to send
     *   - Allocates a ByteBuffer of 2048 bytes
     *   - Puts the string inside the buffer
     *   - Flips the buffer
     *   - Places the byte buffer inside a string
     *   - Returns the string
     */
    IoBiConsumer<SelectionKey, String> writeToSocket = (selectionKey, message) -> {

        ByteBuffer buffer = ByteBuffer.allocate(2048);
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    };
}
