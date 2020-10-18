package red.medusa.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import red.medusa.service.HystrixService;

@Service
public class HystrixServiceImpl implements HystrixService {



    /**
     * 即使有各种问题也成功调用
     */
    @HystrixCommand
    public String 成功测试() {
        StringBuilder sb = new StringBuilder()
                .append("成功测试: ");
        return resultForCircuit(result(sb),simulateFailure()).toString();

    }

    /**
     * 降级
     */
    @HystrixCommand(
            fallbackMethod = "timeOutHandler", commandProperties = {
            // 5秒外响应进入降级处理
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "300")
    })
    public String 超时降级() {
        return result(new StringBuilder().append("超时测试: ")).toString();
    }

    // 降级方法
    public String timeOutHandler() {
        return "FALLBACK: 耗时超过300ms,降级此次请求!";
    }


    /**
     * 熔断
     */
    @HystrixCommand(fallbackMethod = "timeOutOrExceptionHandler", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 熔断时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),// 失败率达到多少后跳闸
    })
    public String 异常降级熔断() throws Exception {
        StringBuilder sb = new StringBuilder().append("熔断测试: ");
        result(sb);

        double random = simulateFailure();

        resultForCircuit(sb, random);

        if (random < 0.30) {
            throw new Exception("random failure processing UserAccount network response");
        }

        return sb.toString();
    }

    // 降级方法
    public String timeOutOrExceptionHandler() {
        return "CIRCUIT BREAK: 此次请求失败,成功率小于30%,熔断且降级!";
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++      模拟故障因素            +++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public StringBuilder result(StringBuilder sb) {
        int s = slowNetwork();
        int f = fivePercentFail();
        sb.append("SN: ").append(s);
        sb.append(" ,DE: ").append(f);
        sb.append(" ,total: ").append(s + f).append("ms");
        return sb;
    }


    public StringBuilder resultForCircuit(StringBuilder sb, double random) {

        sb.append(" ,RSP: ").append((int)(random * 100)).append("%\n");
        return sb;
    }

    /**
     * 模拟 - 200毫秒内随机数的慢网络
     */
    public int slowNetwork() {
        // 模拟慢网络所需时间
        try {
            int millions = (int) (Math.random() * 200) + 1;
            Thread.sleep(millions);
            return millions;
        } catch (InterruptedException e) {
            return 0;
        }

    }

    /**
     * 模拟 - 流量峰值时有50%的几率造成延时
     */
    public int fivePercentFail() {
        if (Math.random() > 0.50) {
            try {
                int millions = (int) (Math.random() * 300) + 25;
                Thread.sleep(millions);
                return millions;
            } catch (InterruptedException e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 模拟 - 30%会导致失败
     */
    public double simulateFailure() {
        return Math.random();
    }
}
