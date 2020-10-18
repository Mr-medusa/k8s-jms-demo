package red.medusa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import red.medusa.service.HystrixService;

@RestController
public class HystrixController {

    private final HystrixService hystrixService;
    public HystrixController(HystrixService hystrixService) {
        this.hystrixService = hystrixService;
    }

    /**
     * 即使有各种问题也成功调用
     */
    @GetMapping(value = "/success")
    public String success() {
        return hystrixService.成功测试();
    }


    /**
     * 慢网络/峰值 - 延时降级
     */
    @GetMapping(value = "/fallback")
    public String fallback() {
        return hystrixService.超时降级();
    }


    /**
     * 快速失败 - 熔断
     */
    @GetMapping(value = "/circuit")
    public String circuit() throws Exception {
        return hystrixService.异常降级熔断();
    }

}


