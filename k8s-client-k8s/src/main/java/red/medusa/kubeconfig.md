### 创建配置文件
#### 创建集群服务信息
```bash
 kubectl config set-cluster kubernetes-cluster \
--certificate-authority=ca.pem \
--embed-certs=true \
--server=https://192.168.50.111:7443 \
--kubeconfig=./admin.kubeconfig
```
#### 为该用户设置凭证
```bash
# 既可以使用证书认证 admin:linux用户
kubectl config set-credentials admin --client-certificate=admin.crt --client-key=admin.key --embed-certs=true --kubeconfig=./admin.kubeconfig
--------------------------------------
YOUR_TOKEN=$(k get secret kubernetes-dashboard-admin-token-trrgj -n kube-system -o jsonpath={.data.token} | base64 -d)
# 也可以使用Token认证
kubectl config set-credentials admin --token=$YOUR_TOKEN --kubeconfig=./admin.kubeconfig
```
#### 绑定上下文
```bash
k config set-context kubernetes-context --cluster=kubernetes-cluster --user=admin --kubeconfig=./admin.kubeconfig
```
#### 激活上下文
```bash
kubectl config use-context kubernetes-context --kubeconfig=./admin.kubeconfig
```
### 绑定角色
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-node
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- apiGroup: rbac.authorization.k8s.io
  kind: User
  name: admin
```  