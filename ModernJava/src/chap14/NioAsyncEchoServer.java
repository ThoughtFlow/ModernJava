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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NioAsyncEchoServer implements Runnable {
    private static final Logger logger = Logger.getLogger(NioAsyncEchoServer.class.getName());
    private static final String SHUTDOWN = "shutdown";

    private final InetSocketAddress inetSocketAddress;
    private AsynchronousServerSocketChannel serverChannel;

    public NioAsyncEchoServer(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            throw new NullPointerException("Parameter inetSocketAddress must not be null");
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
            this.serverChannel.accept(null, new ReadCompletionHandler(serverChannel));
        } catch (IOException e) {
            throw new RuntimeException("Encountered an I/O error. Bailing out.", e);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("USAGE: NioAsyncEchoServer [hostname] <port>");
        } else {
            final String hostname = args.length >= 2 ? args[0] : "localhost";
            final int port = Integer.parseInt(args[args.length >= 2 ? 1 : 0]);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            final NioAsyncEchoServer nioAsyncEchoServer = new NioAsyncEchoServer(inetSocketAddress);
            nioAsyncEchoServer.run();

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                if ((line = reader.readLine()) == null) {
                    System.out.println("Press CTRL+C to shutdown this server");
                    break; // don't shut down
                } else if (SHUTDOWN.equals(line)) {
                    nioAsyncEchoServer.shutdown();
                    break;
                }
            }
        }
    }

    private static class ReadCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, ByteBuffer> {

        private final AsynchronousServerSocketChannel serverChannel;

        public ReadCompletionHandler(AsynchronousServerSocketChannel serverChannel) {
            this.serverChannel = serverChannel;
        }

        public void completed(final AsynchronousSocketChannel clientChannel, ByteBuffer att) {
            // accept the next connection
            serverChannel.accept(null, this);
            logger.info("Accepted connection");
            // handle this connection
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            clientChannel.read(buffer, buffer, new ConnectionHandler(clientChannel));

        }

        public void failed(Throwable exc, ByteBuffer att) {
            if (exc instanceof AsynchronousCloseException) {
                logger.info("Shut down");
            } else {
                logger.log(Level.WARNING, "Failed on accept", exc);
            }
        }
    };

    private static class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final AsynchronousSocketChannel clientChannel;

        public WriteCompletionHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer bytesWritten, ByteBuffer message) {

            ByteBuffer buffer = ByteBuffer.allocate(2048);
            buffer.put(message);

            if (buffer.hasRemaining()) {
                logger.info("Wrote " + bytesWritten + " bytes. Scheduling write of " + buffer.remaining() + " more.");
                clientChannel.write(buffer, message, this);
            } else {
                logger.info("Done writing " + bytesWritten + " bytes");
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            logger.log(Level.WARNING,"Failed on write", exc);
        }
    };

    private static class ConnectionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final AsynchronousSocketChannel clientChannel;

        public ConnectionHandler(AsynchronousSocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void completed(Integer numRead, ByteBuffer message) {
            if (numRead == -1) {
                try {
                    logger.info("Closing");
                    clientChannel.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed on close", e);
                }
            } else {
                logger.info("Read " + numRead + " bytes. Initiating write.");
                ByteBuffer buffer = ByteBuffer.allocate(2048);
                buffer.flip();
                clientChannel.write(buffer, message, new WriteCompletionHandler(clientChannel));

                buffer = ByteBuffer.allocate(2048);
                logger.info("Next read");
                clientChannel.read(buffer, message, this);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer byteBuffer) {
            logger.log(Level.WARNING, "Failed on read", exc);
        }
    };

}
