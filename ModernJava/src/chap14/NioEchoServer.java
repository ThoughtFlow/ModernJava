package chap14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single-threaded but multi-client-capable echo server
 */
public class NioEchoServer implements Runnable {

    private static final Logger logger = Logger.getLogger(NioEchoServer.class.getName());
    public static final String SHUTDOWN = "shutdown";

    private final InetSocketAddress inetSocketAddress;

    private Selector selector;

    public NioEchoServer(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            throw new NullPointerException("Parameter inetSocketAddress must not be null");
        } else if (inetSocketAddress.isUnresolved()) {
            throw new IllegalArgumentException("Unresolved inetSocketAddress: " + inetSocketAddress);
        } else {
            this.inetSocketAddress = inetSocketAddress;
        }
    }

    public void shutdown() {
        if (this.selector != null && this.selector.isOpen()) {
            logger.info("Shutting down");
            try {
                // closing the selector interrupts the select() call and is used to shutdown the server
                this.selector.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to shut-down. Ignoring.", e);
            }
        }
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(this.inetSocketAddress);
            logger.info("Accepting connections on " + this.inetSocketAddress);

            this.selector = Selector.open();
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            while (this.selector.isOpen()) {
                // block until there is something to select or the selector closed
                this.selector.select();
                if (!this.selector.isOpen()) {
                    break;
                }

                // go through all of the selected keys (i.e. events)
                Iterator<SelectionKey> i = this.selector.selectedKeys().iterator();
                while (i.hasNext()) {
                    SelectionKey selectionKey = i.next();
                    // remove each selected key, so that we don't process it again
                    i.remove();

                    try {
                        // handle each key (event)
                        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                            this.onAccept(selectionKey);
                        }
                        if (selectionKey.isValid() && selectionKey.isReadable()) {
                            this.onRead(selectionKey);
                        }
                        if (selectionKey.isValid() && selectionKey.isWritable()) {
                            this.onWrite(selectionKey);
                        }
                    } catch (IOException e) {
                        logger.log(Level.WARNING,
                                "Encountered an I/O error while processing I/O client requests. Ignoring", e);
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            logger.info("Shut down");
        } catch (IOException e) {
            throw new RuntimeException("Encountered an I/O error. Bailing out.", e);
        }
    }

    private void onAccept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

        // get to the actual client channel
        SocketChannel socketChannel = serverSocketChannel.accept();

        // configure each client channel to also be non-blocking
        socketChannel.configureBlocking(false);

        // register for read-ready events (we don't care about write-ready yet)
        SelectionKey clientSelectionKey = socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        // attach our custom "client" state to the key (as we'll need it later)
        clientSelectionKey.attach(new MessageQueue());
        logger.info("Accepted connection from " + socketChannel.getRemoteAddress());
    }

    private void onRead(SelectionKey selectionKey) throws IOException {
        // get to the client channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        logger.info("Reading from " + socketChannel.getRemoteAddress());

        // allocate a buffer from which to read
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

        // read from the client into this buffer
        // (this is non-blocking as there is data waiting for us)
        int numRead = socketChannel.read(byteBuffer);
        if (numRead == -1) {
            // if we reached the end of the client input, we are done
            logger.info("Disconnecting from " + socketChannel.getRemoteAddress());
            socketChannel.close();
        } else {
            // make the buffer ready for writing
            byteBuffer.flip();

            // enqueue this buffer for later writing
            MessageQueue client = (MessageQueue) selectionKey.attachment();

            // Convert from byte to string
            byte[] bytes = new byte[numRead];
            byteBuffer.get(bytes);
            String message = new String(bytes, 0, numRead);
            message = message.trim();

            logger.info("Received: " + message);
            client.enqueue(message);

            // indicate that we are now also interested in write-ready events
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    private void onWrite(SelectionKey selectionKey) throws IOException {
        // get the messageQueue state
        MessageQueue messageQueue = (MessageQueue) selectionKey.attachment();

        // get to the oldest enqueued buffer
        String string = messageQueue.peek();
        if (string == null) {
            logger.warning("Oops. Nothing to write. Something went wrong!");
        } else {
            // get the messageQueue channel
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            logger.info("Writing " + string);
            logger.info("Writing to " + socketChannel.getRemoteAddress());
            // write the buffer (this is non-blocking as the messageQueue is ready to receive data from us)
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            string = "Received: " + string + "\r\n";
            buffer.put(string.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            if (!buffer.hasRemaining()) {
                // we wrote the entire buffer, let's discard it
                messageQueue.dequeue();
            }
            if (messageQueue.isQueueEmpty()) {
                // there is nothing more to write
                // we are no longer interested in write-ready events
                selectionKey.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("USAGE: NioEchoServer [hostname] <port>");
        } else {
            final String hostname = args.length >= 2 ? args[0] : "localhost";
            final int port = Integer.parseInt(args[args.length >= 2 ? 1 : 0]);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            final NioEchoServer nioEchoServer = new NioEchoServer(inetSocketAddress);
            // start the server in its own thread
            new Thread(nioEchoServer).start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    nioEchoServer.shutdown();
                }
            });
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
}
