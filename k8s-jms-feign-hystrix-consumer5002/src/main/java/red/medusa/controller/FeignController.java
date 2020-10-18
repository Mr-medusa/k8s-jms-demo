package red.medusa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import red.medusa.service.FeignService;

@RestController
public class FeignController {

    private final FeignService feignService;
    public FeignController(FeignService feignService) {
        this.feignService = feignService;
    }

    /**
     * 即使有各种问题也成功调用
     */
    @GetMapping(value = "/success")
    public String success() {
        return feignService.success();
    }

    /**
     * 降级
     */
    @GetMapping(value = "/fallback")
    public String fallback() {
        return feignService.fallback();
    }

    /**
     * 熔断
     */
    @GetMapping(value = "/circuit")
    public String circuit() throws Exception {
        return feignService.circuit();
    }



}




























