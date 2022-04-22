package kib.lab7.common.util.client_server_communication;

import kib.lab7.common.abstractions.RequestInterface;
import kib.lab7.common.abstractions.ResponseInterface;
import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
import kib.lab7.common.util.client_server_communication.responses.CommandResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public final class Serializer {

    private Serializer() {
    }

    public static ByteBuffer serializeResponse(ResponseInterface response) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytes);
        oos.writeObject(response);
        oos.flush();
        ByteBuffer bufferToSend = ByteBuffer.wrap(bytes.toByteArray());
        oos.close();
        bytes.close();
        return bufferToSend;
    }

    public static ByteBuffer serializeRequest(RequestInterface request) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytes);
        oos.writeObject(request);
        oos.flush();
        ByteBuffer bufferToSend = ByteBuffer.wrap(bytes.toByteArray());
        oos.close();
        bytes.close();
        return bufferToSend;
    }

    public static RequestInterface deserializeRequest(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        RequestInterface request = (RequestInterface) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return request;
    }

    public static ResponseInterface deserializeResponse(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ResponseInterface response = (ResponseInterface) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return response;
    }
}
