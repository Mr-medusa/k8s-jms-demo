package red.medusa.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;

@Component
public class MessageConsumerSet {

    private static Logger logger = LoggerFactory.getLogger(MessageConsumerSet.class);

    private static HashSet<MessageConsumer> consumers = new HashSet<>();

    public synchronized void joinConsumers(MessageConsumer consumer) {
        consumers.add(consumer);
    }

    public synchronized void removeConsumer(MessageConsumer consumer) {
        consumers.remove(consumer);
    }

    public void sendMessage(String message) throws IOException{
        for (MessageConsumer consumer : consumers) {
            try {
                consumer.sendMsg(message);
            } catch (IOException e) {
                removeConsumer(new MessageConsumer(consumer.getId(), null));
                logger.info("sendMessage Error and consumer size = " + consumers.size());
                throw new IOException("sendMessage Error");
            }
        }
    }

    public String info() {
        return "consumer closed session and consumer size = " + consumers.size();
    }
}


