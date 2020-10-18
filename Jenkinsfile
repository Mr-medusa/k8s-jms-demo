//git的url地址
def git_address = "http://gitlab.medusa.com/k8s-ms/k8s-jms-demo.git"
//git凭证ID
def git_auth = "ef6f9dc4-44e0-45b2-bbd4-cdbd1960547d"
//镜像的版本号
def tag = "1.0"
//Harbor的url地址
def harbor_url = "hub.medusa.com"
//镜像库项目名称
def harbor_project_name = "tensquare_secret"
//Harbor的登录凭证ID
def harbor_auth = "afcd2fc8-2442-414f-85d2-f0078962ef3b"

def k8s_auth = "fa445b8e-bc05-40c1-9dff-9194b24f9f7f"

// harbor 仓库凭证
// k create secret docker-registry harbor-auth-secret --docker-server=hub.medusa.com \
// --docker-username=admin --docker-password=Harbor12345 --docker-email=harbor@email.com
def secret_name = "harbor-auth-secret"

podTemplate(label: 'jenkins-slave', cloud: 'kubernetes',
        containers: [
                containerTemplate(name: 'jnlp', image: "hub.medusa.com/library/jenkins-jnlp-maven:v3.0"),
                containerTemplate(name: 'docker', image: "docker:stable", ttyEnabled: true, command: 'cat')
        ],
        volumes: [
                hostPathVolume(mountPath: '/var/run/docker.sock',
                        hostPath: '/var/run/docker.sock'),
                nfsVolume(mountPath: '/usr/local/apache-maven/repo',
                        serverAddress: 'kms200.host.com',
                        serverPath: '/data/nfs-volume/maven'),
        ]
) {
    node("jenkins-slave") {
        // 第一步
        stage('拉取代码') {
            checkout([$class: 'GitSCM', branches: [[name: 'master']], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_address}"]]])
        }
        // 第二步
        stage('代码编译') {
            //编译并安装公共工程
            //sh "mvn -f tensquare_common clean install"
            echo "启动zipkin"
            kubernetesDeploy configs: "dependencies/deploy.yml", kubeconfigId: "${k8s_auth}"
        }
        // 第三步
        stage('构建镜像,部署项目') {
            //把选择的项目信息转为数组
            def selectedProjects = "${project_name}".split(',')
            for (int i = 0; i < selectedProjects.size(); i++) {
                //取出每个项目的名称
                def currentProjectName = selectedProjects[i]
                //定义镜像名称
                def imageName = "${currentProjectName}:${tag}"
                //编译,构建本地镜像
                sh "mvn -f ${currentProjectName} clean package dockerfile:build"
                container('docker') {
                    //给镜像打标签
                    sh "docker tag ${imageName} ${harbor_url}/${harbor_project_name}/${imageName}"
                    //登录Harbor,并上传镜像
                    withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
                        //登录
                        sh "docker login -u ${username} -p ${password} ${harbor_url}"
                        //上传镜像
                        sh "docker push ${harbor_url}/${harbor_project_name}/${imageName}"
                    }
                    //删除本地镜像
                    sh "docker rmi -f ${imageName}"
                    sh "docker rmi -f ${harbor_url}/${harbor_project_name}/${imageName}"
                }

                /**
                 * 部署到 k8s
                 */
                def deploy_image_name = "${harbor_url}/${harbor_project_name}/${imageName}"

                // 整理项目下定义的 deploy.yaml 并交付到k8s
                sh """
                    sed -i 's#\$IMAGE_NAME#${deploy_image_name}#' ${currentProjectName}/deploy.yml
                    sed -i 's#\$SECRET_NAME#${secret_name}#' ${currentProjectName}/deploy.yml
                """

                // kubeconfigId 凭证: kubernates config
                kubernetesDeploy configs: "${currentProjectName}/deploy.yml", kubeconfigId: "${k8s_auth}"
            }
        }

    }

}



















