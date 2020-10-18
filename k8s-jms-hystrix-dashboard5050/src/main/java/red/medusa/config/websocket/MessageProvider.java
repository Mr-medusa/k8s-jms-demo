package red.medusa.config.websocket;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageProvider {

    private final MessageConsumerSet consumerSet;

    public MessageProvider(MessageConsumerSet consumerSet) {
        this.consumerSet = consumerSet;
    }

    public void sendMessage(String message) throws IOException {
        consumerSet.sendMessage(message);
    }
}


