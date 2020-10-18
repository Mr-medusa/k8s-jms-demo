package red.medusa.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "K8S-JMS-FEIGN-HYSTRIX-CONSUMER5002")
public interface FeignService
{
    @GetMapping(value = "success")
    String success();

    @GetMapping(value = "fallback")
    String fallback();

    @GetMapping(value = "circuit")
    String circuit();
}
