package red.medusa;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.IOException;
import java.io.InputStreamReader;

public class TestClient {
    public static void main(String[] args) throws ApiException, IOException, ApiException {

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
        Configuration.setDefaultApiClient(client);

        //创建一个api
        CoreV1Api api = new CoreV1Api();

        //打印所有的pod
        V1PodList list = api.listPodForAllNamespaces(null,null,null,null,null,null,null, null,null);

        for (V1Pod item : list.getItems()) {
            System.out.println(item);
        }

        // output:
        //class V1Pod {
        //    apiVersion: null
        //    kind: null
        //    metadata: class V1ObjectMeta { ...

    }
}