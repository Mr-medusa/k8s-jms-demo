package red.medusa.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MultiEchoWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(MultiEchoWebSocketHandler.class);

    private final MessageConsumerSet consumerSet;

    public MultiEchoWebSocketHandler(MessageConsumerSet consumerSet) {
        this.consumerSet = consumerSet;
    }

    /**
     * 发送连接
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info(session.getRemoteAddress() + " --- 建立了连接 ---");
        consumerSet.joinConsumers(new MessageConsumer(session.getId(), session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info(session.getRemoteAddress() + " --- 关闭了连接 ---");
        consumerSet.removeConsumer(new MessageConsumer(session.getId(), session));
        logger.info("consumers" + consumerSet.info());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }

}
