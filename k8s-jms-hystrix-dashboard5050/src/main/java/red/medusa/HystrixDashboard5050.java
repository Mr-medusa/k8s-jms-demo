package red.medusa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableHystrixDashboard
@EnableFeignClients
public class HystrixDashboard5050
{
    public static void main(String[] args) {
            SpringApplication.run(HystrixDashboard5050.class, args);
    }
}