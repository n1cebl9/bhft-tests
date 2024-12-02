package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.WebSocketInterface;
import jakarta.websocket.*;
import lombok.Getter;
import lombok.SneakyThrows;
import pojo.Message;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ClientEndpoint
public class TodoAppWebSocketClient implements WebSocketInterface<Message> {

    private final URI uri;
    private Session session;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Getter private final List<Message> messageList = new ArrayList<>();

    @SneakyThrows
    public TodoAppWebSocketClient(String path) {
        uri = new URI(path);
    }

    @SneakyThrows
    public void openSession() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
    }

    @SneakyThrows
    @OnMessage
    public void processMessage(String value) {
        System.out.println("received message: " + value);

        Message message = objectMapper.readValue(value, Message.class);
        messageList.add(message);
    }

    @SneakyThrows
    public void closeSession() {
        if(session != null && session.isOpen()){
            session.close();
        }
    }

}