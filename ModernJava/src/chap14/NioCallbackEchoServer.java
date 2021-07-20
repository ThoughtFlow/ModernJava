package chap14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Echo server implementation with Async completion handlers
 */
public class NioCallbackEchoServer implements Runnable {
    private static final Logger logger = Logger.getLogger(NioCallbackEchoServer.class.getName());
    private static final String SHUTDOWN = "shutdown";

    private final InetSocketAddress inetSocketAddress;
    private AsynchronousServerSocketChannel serverChannel;

    public NioCallbackEchoServer(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            throw new IllegalArgumentException("Parameter inetSocketAddress must not be null");
        } else if (inetSocketAddress.isUnresolved()) {
            throw new IllegalArgumentException("Unresolved inetSocketAddress: " + inetSocketAddress);
        } else {
            this.inetSocketAddress = inetSocketAddress;
        }
    }

    public void shutdown() {
        if (this.serverChannel != null && this.serverChannel.isOpen()) {
            logger.info("Shutting down");
            try {
                this.serverChannel.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to shut-down. Ignoring.", e);
            }
        }
    }

    @Override
    public void run() {
        try {
            this.serverChannel = AsynchronousServerSocketChannel.open();
            this.serverChannel.bind(this.inetSocketAddress);
            logger.info("Accepting connections on " + this.inetSocketAddress);
            this.serverChannel.accept(null, new AcceptCompletionHandler(serverChannel));
        } catch (IOException e) {
            throw new RuntimeException("Encountered an I/O error. Bailing out.", e);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("USAGE: NioCallbackEchoServer [hostname] <port>");
        } else {
            final String hostname = args.length >= 2 ? args[0] : "localhost";
            final int port = Integer.parseInt(args[args.length >= 2 ? 1 : 0]);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            final NioCallbackEchoServer nioEchoServer = new NioCallbackEchoServer(inetSocketAddress);
            nioEchoServer.run();
            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                if ((line = reader.readLine()) == null) {
                    System.out.println("Press CTRL+C to shutdown this server");
                    break; // don't shut down
                } else if (SHUTDOWN.equals(line)) {
                    nioEchoServer.shutdown();
                    break;
                }
            }
        }
    }

    /**
     * This class handles the socket accept event.
     */
    private static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private final AsynchronousServerSocketChannel serverSocketChannel;

        private AcceptCompletionHandler(AsynchronousServerSocketChannel serverSocketChannel) {
            this.serverSocketChannel = serverSocketChannel;
        }

        @Override
        public void completed(final AsynchronousSocketChannel clientChannel, Void att) {

            // accept the next connection
            serverSocketChannel.accept(null, this);
            logger.info("Accepted connection");

            // handle this connection
            ByteBuffer buffer = ByteBuffer.allocate(2048);

            logger.info("Initial read");
            clientChannel.read(buffer, buffer, new ReadCompletionHandler(clientChannel));
        }

        @Override
        public void failed(Throwable exc, Void att) {
            if (exc instanceof AsynchronousCloseException) {
                logger.info("Shut down");
            } else {
                logger.log(Level.WARNING, "Failed on accept", exc);
            }
        }
    }

    /**
     * This class handles the socket read event.
     */
    private static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final AsynchronousSocketChannel clientChannel;
        private final MessageQueue queue;

        private ReadCompletionHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
            this.queue = new MessageQueue();
        }

        @Override
        public void completed(Integer numRead, ByteBuffer buffer) {

            if (numRead == -1) {
                try {
                    logger.info("Closing");
                    clientChannel.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed on close", e);
                }
            } else {
                logger.info("Read " + numRead + " bytes. Initiating write.");
                buffer.flip();

                // Convert from byte to string
                byte[] bytes = new byte[numRead];
                buffer.get(bytes);
                String message = new String(bytes, 0, numRead);
                message = message.trim();

                logger.info("Received: " + message);
                queue.enqueue("Echo back: " + message + "\r\n");

                clientChannel.write(buffer, queue, new WriteCompletionHandler(clientChannel));
                buffer = ByteBuffer.allocate(2048);
                logger.info("Next read");
                clientChannel.read(buffer, buffer, this);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer byteBuffer) {
            logger.log(Level.WARNING, "Failed on read", exc);
        }
    }

    /**
     * This class handles the socket write event.
     */
    private static class WriteCompletionHandler implements CompletionHandler<Integer, MessageQueue> {

        private final AsynchronousSocketChannel clientChannel;

        public WriteCompletionHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer bytesWritten, MessageQueue queue) {

            // Start here

            if (queue.peek() != null) {
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                String message = queue.dequeue();
                buffer.put(message.getBytes());
                buffer.flip();
                clientChannel.write(buffer, queue, this);
                logger.info("Sent " + message);
            }
            else {
                logger.info("Done writing " + bytesWritten + " bytes");
            }
        }

        @Override
        public void failed(Throwable exc, MessageQueue attachment) {
            logger.log(Level.WARNING, "Failed on write", exc);
        }
    }
}
