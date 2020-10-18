package red.medusa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServer3003 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer3003.class, args);
    }
}
