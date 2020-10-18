package red.medusa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServer4001 {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer4001.class, args);
    }
}
