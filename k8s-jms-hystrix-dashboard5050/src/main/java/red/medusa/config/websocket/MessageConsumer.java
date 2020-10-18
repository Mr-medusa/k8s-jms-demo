package red.medusa.config.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

public class MessageConsumer {

    private String id;
    private WebSocketSession socketSession;

    public MessageConsumer(String id, WebSocketSession socketSession) {
        this.id = id;
        this.socketSession = socketSession;
    }

    public void sendMsg(String message) throws IOException {
        this.socketSession.sendMessage(new TextMessage(message));
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageConsumer)) return false;
        MessageConsumer that = (MessageConsumer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
