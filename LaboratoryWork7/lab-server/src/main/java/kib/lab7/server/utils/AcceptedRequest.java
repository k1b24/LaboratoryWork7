package kib.lab7.server.utils;


import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class AcceptedRequest {

    private final ByteBuffer recievedBytes;
    private final SocketAddress socketAddress;


    public AcceptedRequest(ByteBuffer recievedBytes, SocketAddress socketAddress) {
        this.recievedBytes = recievedBytes;
        this.socketAddress = socketAddress;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public ByteBuffer getRecievedBytes() {
        return recievedBytes;
    }
}
