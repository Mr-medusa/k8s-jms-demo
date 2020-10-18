package red.medusa;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;

@SpringBootApplication
public class Main {


    @Configuration
    @RestController
    public static class InfoController {

        @RequestMapping("/")
        public String info() throws Exception {
            //直接写config path
            String kubeConfigPath = "admin.kubeconfig";
            // String kubeConfigPath = "kubelet.kubeconfig";

            //加载k8s, config
            ApiClient client = ClientBuilder.kubeconfig(
                    KubeConfig.loadKubeConfig(
                            new InputStreamReader(ClassLoader.getSystemResource(kubeConfigPath).openStream()
                            )
                    )
            ).build();

            //将加载config的client设置为默认的client
            io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);

            //创建一个api
            CoreV1Api api = new CoreV1Api();

            //打印所有的pod
            V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

            return list.getKind();

        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
