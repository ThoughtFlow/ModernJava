package lab20.init;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public interface SocketFunctions {

    /**
     * ***todo Implement me***
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

        throw new IOException("Not implemented");
    };

    /**
     * ***todo Implement me***
     * This function is to accept a new connection.
     *
     * Implement a function that:
     *   - Receives a selection key for the new connection
     *   - Accepts the connection
     *   - Configures it to be non-blocking
     *   - Returns the socket channel attached to the selection key
     */
    IoFunction<SelectionKey, SocketChannel> acceptConnection = selectionKey -> {

        throw new IOException("Not implemented");
    };

    /**
     * ***todo Implement me***
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

        throw new IOException("Not implemented");
    };

    /**
     * ***todo Implement me***
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
        throw new IOException("Not implemented");
    };
}
