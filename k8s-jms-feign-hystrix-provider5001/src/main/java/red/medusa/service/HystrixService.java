package red.medusa.service;

public interface HystrixService {

    String 成功测试();
    /**
     * 降级
     */
    String 超时降级();

    /**
     * 熔断
     */
    String 异常降级熔断() throws Exception;
}
