package kib.lab7.server.utils;

import kib.lab7.common.abstractions.RequestInterface;
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

public class ConnectionHandlerServer {

    private static final int SELECTION_TIME = 1;
    private final Selector selector;
    private final DatagramChannel datagramChannel;
    private SocketAddress socketAddress;

    public ConnectionHandlerServer(int port) throws IOException {
        datagramChannel = DatagramChannel.open();
        selector = Selector.open();
        datagramChannel.socket().bind(new InetSocketAddress(port));
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector, SelectionKey.OP_READ);
    }

    public RequestInterface listen() throws ClassNotFoundException, IOException {
        if (selector.select(SELECTION_TIME) == 0) {
            return null;
        }
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isReadable()) {
                ByteBuffer packet = ByteBuffer.allocate(datagramChannel.socket().getReceiveBufferSize());
                socketAddress = datagramChannel.receive(packet);
                ((Buffer) packet).flip();
                byte[] bytes = new byte[packet.remaining()];
                packet.get(bytes);
                return Serializer.deserializeRequest(bytes);
            }
        }
        return null;
    }

    public String sendResponse(ResponseInterface response) throws IOException {
        ByteBuffer bufferToSend = Serializer.serializeResponse(response);
        datagramChannel.send(bufferToSend, socketAddress);
        return socketAddress.toString();
    }

    public void closeServer() throws IOException {
        datagramChannel.close();
        selector.close();
    }
}
