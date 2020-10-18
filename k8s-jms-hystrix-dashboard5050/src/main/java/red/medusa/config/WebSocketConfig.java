package red.medusa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import red.medusa.config.websocket.MessageProvider;
import red.medusa.config.websocket.MultiEchoWebSocketHandler;
import red.medusa.service.FeignService;

import java.io.IOException;

/**
 * <p>
 * nginx 配置
 * <p>
 * server {
 * listen       80;
 * server_name  xx.com;
 * charset utf-8;
 * <p>
 * location / {
 * proxy_pass http://127.0.0.1:3000;
 * proxy_set_header Host $host;
 * proxy_http_version 1.1;
 * proxy_set_header Upgrade $http_upgrade;
 * proxy_set_header Connection "upgrade";
 * proxy_set_header X-Real-IP $remote_addr;
 * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
 * proxy_connect_timeout 60;
 * proxy_read_timeout 600;
 * proxy_send_timeout 600;
 * }
 * <p>
 * error_page   500 502 503 504  /50x.html;
 * location = /50x.html {
 * root   html;
 * }
 * }
 */
@Configuration
@EnableWebSocket
@EnableScheduling
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private FeignService feignService;

    /**
     * Multi Echo
     */
    @Autowired
    private MessageProvider provider;

    @Autowired
    private MultiEchoWebSocketHandler socketHandler;

    /**
     * proxy_http_version 1.1;
     * proxy_set_header Upgrade $http_upgrade;
     * proxy_set_header Connection "upgrade";
     * <p>
     * 其中第一行是告诉 nginx 使用HTTP/1.1通信协议,这是 websoket 必须要使用的协议。
     * 第二行和第三行告诉 nginx,当它想要使用 WebSocket 时,响应 http升级请求。
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/echo").withSockJS();
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduled_fixedDelay() {
        try {
            // success
            provider.sendMessage(feignService.success());
            // fallback
            provider.sendMessage(feignService.fallback());
            // circuit
            provider.sendMessage(feignService.circuit());

        } catch (IOException ignored) {
        }
    }
}
