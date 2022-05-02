package kib.lab7.server.utils;

import kib.lab7.common.abstractions.ResponseInterface;
import kib.lab7.common.util.client_server_communication.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_READ;

public class ConnectionHandlerServer {

    private static final int SELECTION_TIME = 1;
    private final Selector selector;
    private final DatagramChannel datagramChannel;
    private final SocketAddress serverSocketAddress;

    public ConnectionHandlerServer(int port) throws IOException {
        serverSocketAddress = new InetSocketAddress(port);
        datagramChannel = DatagramChannel.open();
        selector = Selector.open();
        datagramChannel.socket().bind(serverSocketAddress);
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector, OP_READ);
    }

    public AcceptedRequest listen() throws ClassNotFoundException, IOException {
        if (selector.select(SELECTION_TIME) == 0) {
            return null;
        }
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid()) {
                if (key.isReadable()) {
                    ByteBuffer packet = ByteBuffer.allocate(datagramChannel.socket().getReceiveBufferSize());
                    SocketAddress socketAddress = datagramChannel.receive(packet);
                    ((Buffer) packet).flip();
                    byte[] bytes = new byte[packet.remaining()];
                    packet.get(bytes);
                    return new AcceptedRequest(Serializer.deserializeRequest(bytes), socketAddress);
                }
            }
        }
        return null;
    }

    public void sendResponse(ResponseInterface response, SocketAddress socketAddress) throws IOException {
        ByteBuffer bufferToSend = Serializer.serializeResponse(response);
        datagramChannel.send(bufferToSend, socketAddress);
    }

    public void closeServer() throws IOException {
        datagramChannel.close();
        selector.close();
    }
}
