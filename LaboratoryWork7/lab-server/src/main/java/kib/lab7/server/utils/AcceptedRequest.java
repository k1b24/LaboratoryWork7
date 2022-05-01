package kib.lab7.server.utils;

import kib.lab7.common.abstractions.RequestInterface;

import java.net.SocketAddress;

public class AcceptedRequest {

    private final RequestInterface recievedRequest;
    private final SocketAddress socketAddress;


    public AcceptedRequest(RequestInterface recievedRequest, SocketAddress socketAddress) {
        this.recievedRequest = recievedRequest;
        this.socketAddress = socketAddress;
    }

    public RequestInterface getRecievedRequest() {
        return recievedRequest;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
}
