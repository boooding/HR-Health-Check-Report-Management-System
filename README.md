# hr-health-check
## 作者
- 后端： 陈智猛
- 前端： 蔡泽楷
## 本地部署
### 环境准备
> - docker: 20.10.5
> - gradle: 6.7.1
> - jdk: 13

**tips: 上面是我本地的版本，如果你的版本也可以运行就没事**

### Linux
> 1. 项目根路径下: `./gradlew clean`
> 2. `./gradlew build`
> 3. `cp server/build/libs/*.jar scripts/`
> 4. `cd scripts/`
> 5. `./test_run_linux.sh db`

### Windows
**git bash 下执行**
> 1. 项目根路径下: `./gradlew clean`
> 2. `./gradlew build`
> 3. `cp server/build/libs/*.jar scripts/`
> 4. `cd scripts/`
> 5. `./test_run_win.sh db`

### 注意
> - 有端口冲突的话自己解决
> - 5 中不加 db 表示使用现有容器