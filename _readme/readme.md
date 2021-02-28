https://blog.csdn.net/abc465200/article/details/107493629

#Windvalley 项目说明

##前端

###软件安装
+ VSCode  
    + 安装插件
        + Chinese Language
        + ESLint
        + Live Server
        + Node.js Modules Intellisense
        + Vetur
        + VueHelper
    + 设置
        Emment: Trigger Expansion On Tab = True
+ Node.js

###NPM项目
>package.json为项目文件
+ 修改npm配置
  + 查看默认配置信息
    npm config list
  + 修改默认仓库
    npm config set registry http://registry.npm.taobao.org
+ 建立项目
    + npm init 初始化一个NPM项目, 
    + npm init -y 初始化一个NPM项目,所有需要确认的全部同意      
+ 安装依赖
    + npm install 直接导入一个npm项目
    + npm install 你需要安装的依赖包
      + npm install jquery 安装jQuery的最新版本
      + npm install jquery@2.1.3 安装jQuery的2.1.3版本
    + npm install -g webpack 安装打包模块
    + npm install -g webpack-cli 安装打包模块
    + npm install -D style-loader css-loader 安装css打包

###前端组件三要素
>main.js引用App.vue和src/router/index.js,根据路由配置，App.vue显示对应的页面内容

![login.png](resource\页面组件层次图.png)

1. 入口js: src/main.js
   
   1. 所有组件的入口
      ```javascript
      import App from './App'
      ```
   2. 所有路由入口
      ```javascript
      import router from './router'
      ```
2. 入口页面: scr/app.vue

   ```javascript
   <template>
     <div id="app">
        //此处为路由出口，也就是路由到哪里，这里就显示路由对应的页面
       <router-view/>
     </div>
   </template>
   
   <script>
   export default {
     name: 'App'
   }
   </script>
   ```
   
3. 路由: src/router/index.js
      
###项目开发流程
1. 创建路由(router  src/router)
2. 创建API(API  src/api)
   ```javascript
        resolve: {
            extensions: ['.js', '.vue', '.json'],
            alias: {
                '@': resolve('src')
            }
        }
   ```
   ```javascript
    用于异步请求数据, 封装了axios 
    @->src目录, 在webpack.base.config定义了别名，参见上一个代码段
    import request from '@/utils/request'

    export function list() {
        return request({
            url: '/admin/edu/teacher/list',
            method: 'post'
       })
    }

   //current size 为路径参数  searchCondition为表单参数，键值对方式
    export function pageList(current, size, searchCondition) {
         return request({
            url: `/admin/edu/teacher/list/${current}/${size}`,
            params: searchCondition,
            method: 'post'
         })
   }
   
    ```
>使用调用时，要分清params和Data传参方式的不同
   > 1. paramater 使用的是键值对的方式
   > 2. data使用的是JSON格式，后端必须使用RequestBody接收参数   
   
3. 创建页面组件(view  src/views)
   >由两部分构成
   >> ``` 1. <template></template> ```
   > 
   >> ``` 2. <script></script> ```
   
4. 传参方式
   + json方式传递, 使用data标记后面为一json字符串方式，后端使用requestbody接收json
```javascript
   //teacher为JSON方式字符串
  save(teacher) {
    return request({
      url: `/admin/edu/teacher/save`,
      data: teacher,
      method: 'post'
    })
  }
```
   + paramaters方式传递, 使用键值对或者url带形参方式
```javascript
   //current size 为路径参数  searchCondition为表单参数，键值对方式
    export function pageList(current, size, searchCondition) {
         return request({
            url: `/admin/edu/teacher/list/${current}/${size}`,
            params: searchCondition,
            method: 'post'
         })
   }
```

5. 路由跳转,简单说就是页面跳转到/teacher/list
```javascript
    this.$router.push({ path: '/teacher/list' })
```

6. 使用组件
```javascript
<script>
    
import Info from '@/views/course/components/Info.vue' //导入使用的组件
import Chapter from '@/views/course/components/chapter/Index.vue' //导入使用的组件
import Publish from '@/views/course/components/Publish.vue' //导入使用的组件

export default {
  components: { Info, Chapter, Publish },  //注册使用的组件
  data() {
    return {
      active: 1,
      courseId: null //  当前编辑课程id
    }
  }
}
</script>
```
##webpack打包产生最终代码
>webpack打包流程，已经基本得编译流程，后续继续添加
1. 由package.json文件中的"dev":"webpack-dev-server --inline --progress --config build/webpack.dev.conf.js",，找到webpack.dev.conf.js文件
2. 由webpack.dev.conf.js中的const baseWebpackConfig = require('./webpack.base.conf'),找到./webpack.base.conf
3. 由./webpack.base.conf文件中的entry标识，找到项目的入口文件./src/main.js
```javascript
module.exports = {
  context: path.resolve(__dirname, '../'),
  entry: {
    app: './src/main.js'
  },
  output: {
    path: config.build.assetsRoot,
    filename: '[name].js',
    publicPath:
      process.env.NODE_ENV === 'production'
        ? config.build.assetsPublicPath
        : config.dev.assetsPublicPath
  }
```
4. 由webpack.dev.conf.js中的plugins，发现main.js需要注入的主文件，输入为index.html,输出为index.html,title可以修改为需要的题头标签
```javascript
  plugins: [
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: 'index.html',
      inject: true,
      favicon: resolve('favicon.ico'),
      title: '风之谷后台管理系统-题头'
    })
  ]
```

>##与前端项目的对接
>前端使用的是vue-element-template实现，生产环境的环境修改，可以参考开发环境的修改步骤

1. 修改网站标题为Windvalley
   * 修改src\views\login\index.vue中,替换标题为Windvalley
```html
<!--
<h3 class="title">vue-admin-template</h3>
-->
<h3 class="title">windvalley</h3>
```
![login.png](resource\login.png)

2. 修改默认语言为中文, 修改src\main.js
```js
//import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
```   
3. 修改与后端数据的对接，实施步骤如下
    1. 修改config目录下dev.env.js文件，由原先使用Mock数据转向使用本地后端数据
```json
//  module.exports = merge(prodEnv, {
//  NODE_ENV: '"development"',
//  BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"',
//}

module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',
    BASE_API: '"http://127.0.0.1:8080"',
}

```    
##定义VUE路由方式
1. 在view目录下建立模板文件，必须以.vue为扩展名
   

    +view
       +teacher
            form.vue
            list.vue
    模板文件中只可以存在一个根元素,简单来说就是最外层只能有一个<div>标签

2. 修改router目录下的index.js文件，进行路由配对   
```javascript
  // 讲师管理
  {
    // 分项菜单->主菜单 讲师管理
    path: '/teacher',
    component: Layout,
    redirect: '/teacher/list',
    name: 'Teacher',
    meta: { title: '讲师管理' },
    // 分项菜单->子菜单，以下有3个子菜单项，编辑讲师隐藏了，所以只能看到2个菜单项
    children: [
      {
        path: 'list',
        name: 'TeacherList',
        component: () => import('@/views/teacher/list'),
        meta: { title: '讲师列表' }
      },
      {
        path: 'create',
        name: 'TeacherCreate',
        component: () => import('@/views/teacher/form'),
        meta: { title: '添加讲师' }
      },
      {
        path: 'edit/:id',
        name: 'TeacherEdit',
        component: () => import('@/views/teacher/form'),
        meta: { title: '编辑讲师' },
        hidden: true
      }
    ]
  },
```
3. 样式说明
```css
    //增加右边页面和左边菜单处的间隙, 如果没有，则紧贴右边菜单
    <div class="app-container">
```
##小坑点
+ 强制刷新 
  ```javascript
  this.$forceUpdate()
  ```
+ :xxx = V-BIND:xxx 用于绑定数据和元素属性
+ this.$parent.xx 访问父组件的xxx

##后端

##安装mysql
1. 下载mysql免安装版
2. 解压
3. 在解压目录中新建my.ini文件
```text
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8 

[mysqld]
#设置3306端口
port = 3306 
# 设置mysql的安装目录
basedir=D:\\softnew\\MYSQL\\mysql-5.7.20-winx64
# 允许最大连接数
max_connections=200
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB

[client]
port=3306
default-character-set=utf8
```
4. mysqld --initialize-insecure 初始化mysql，必须用次命令，此时密码默认为空 
5. mysqld -install 安装mysql 
6. net start mysql 启动mysql服务
7. mysql -uroot -p 登陆
8. alter user 'root'@'localhost' identified by '你想要的密码';改密码

##spring boot
###配置要点
+ 如果需要在一个有数据源使用的多服务项目中，使用一个无数据源的项目，使用如下注解 
  ```java
  @SpringBootApplication(exclude = DataSourceAutoConfiguration.class
  ```
###自定义异常处理
>实现捕获异常以后，统一由自定义异常处理，实现精细化异常提示
+ 编写自定义异常类WindvalleyException，代码位于service_base.src.main.java.excepiton.WindvalleyException
+ 在阿里云oss上传模块中，捕获异常后重新抛出为自定义异常 代码位于service_oss.src.main.java.oss.controller.admin.FileController
+ 在控制器切面处统一处理自定义异常，代码位于service_base.src.main.java.handler.GlobalExceptionHandler



###阿里云服务

####VOD 阿里云视频服务
#####上传视频
+ 安装上传SDK
  mvn install:install-file -Dfile=aliyun-sdk-oss-3.1.0 -DgroupId=com.aliyun -DartifactId=aliyun-sdk-oss -Dversion=3.1.0 -Dpackaging=jar

#####客户端播放
######使用H5方式
```javascript
    <script>
        var player = new Aliplayer({
        id: 'J_prismPlayer',
        width: '100%',
        autoplay: true,
        //播放方式一: 支持播放地址播放,此播放优先级最高
        //可以在阿里云点播控制台里可以直接获得 
        //例如 https://outin-903e387f775e11eba1fa00163e1c35d5.oss-cn-shanghai.aliyuncs.com/sv/54d1b653-177e16c628b/54d1b653-177e16c628b.mp4?Expires=1614399228&OSSAccessKeyId=LTAIrkwb21KyGjJl&Signature=5d0Dmx0uvxlWZ743lrmZheQFxDo%3D
        source : '播放url',

        //播放方式二：点播用户推荐
        //可以在阿里云点播控制台里可以直接获得
        //基础配置中的ID
        vid : '1e067a2831b641db90d570b6480fbc40',
        //
        playauth : 'ddd',
        cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png',
        encryptType:1, //当播放私有加密流时需要设置。

        //播放方式三：仅MPS用户使用
        vid : '1e067a2831b641db90d570b6480fbc40',
        accId: 'dd',
        accSecret: 'dd',
        stsToken: 'dd',
        domainRegion: 'dd',
        authInfo: 'dd',
        //播放方式四：使用STS方式播放
        vid : '1e067a2831b641db90d570b6480fbc40',
        accessKeyId: 'dd',
        securityToken: 'dd',
        accessKeySecret: 'dd',
         region:'cn-shanghai',//eu-central-1,ap-southeast-1
        },function(player){
            console.log('播放器创建好了。')
       });
    </script>
```

####oss 云储存服务
#####安装
+ 方式一：在Maven项目中加入依赖项在Maven工程中使用OSS Java SDK，只需在pom.xml中加入相应依赖即可。以3.10.2版本为例，在<dependencies>中加入如下内容：
```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.10.2</version>
</dependency>
```
+.方式二：在Intellij IDEA项目中导入JAR包,以3.10.2版本为例，步骤如下：
>   1. 下载Java SDK 开发包。
>   2. 解压该开发包。
>   3. 将解压后文件夹中的文件aliyun-sdk-oss-3.10.2.jar以及lib文件夹下的所有JAR文件拷贝到您的项目中。
>   4. 在Intellij IDEA中选择您的工程，右键选择File > Project Structure > Modules > Dependencies > + > JARs or directories 。 选中拷贝的所有JAR文件，导入到External Libraries中 

#####基础样例
>具体参考阿里云手册

###注册服务中心
####Nacos (spring cloud Alibaba)
>Nacos = spring cloud eureka + spring cloud config + spring cloud bus

+ 安装
   1. 注意必须要设置JAVA_HOME的路径，否则一定运行不起来   

+ 服务注册
   1. 引入依赖
      service_edu与service_oss都需要微服务注册，把依赖放入父节点Service的POM当中
      ```html
        <!-- 服务注册 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

      ```            
   2. 添加配置信息
      ```yaml
        cloud:
          nacos:
            discovery:
              server-addr: http://localhost:8848
      ```
   3. 启动类中添加注解
      ```java
        @EnableDiscoveryClient
      ```  
   4.  坑点   
        ```yaml
        application:
          name: service-edu   //此处名字不可以包含下划线，否则会出现莫名其妙的问题
        ```
+ 服务调用
  + 调用端
    + 在调用项目的POM里配置依赖
      ```html
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-openfeign</artifactId>
            </dependency>
        </dependencies>
      ```  
    + 启动类中添加注解
       ```java
         @EnableFeignClients
       ```
    + 创建远程调用方法
      ```java
        @Service
        @FeignClient("service-oss") //微服务注册到注册中心的名字
        public interface IOssFileService {
            @ApiOperation(value = "测试微服务调用")
            @PostMapping("/admin/oss/file/test")    //必须是完整的远程调用地址
              R test();
        }
      ```
  + 被调用端(服务端)
    + 创建调用接口
+ 负载均衡
  + 配置多实例
    + 选择需要提供多实例的项目
    + 点击Edit Configurations
    + 点击Copy Configurations
    + VM Option添加  -DServer.port=8181 修改服务端口为8181,实现多实例
  + 均衡策略
    + 默认为轮询策略，交替访问
    + 轮询方法参考Ribbon说明
  + 配置均衡策略
    在调用端配置
    ```yaml
    service-oss:
      ribbon:
        NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #此处配置为随机策略
    ```
+ Ribbon重试机制
  + 重试配置
    ```yaml
        ribbon:
            MaxAutoRetries: 0 #同一实例最大重试次数,不包括首次调用，默认为0
            MaxAutoRetriesNextServer: 1 #其他实例最大重试次数,不包括首次所选择的server，默认为1
            OkToRetryOnAllOperations: true # 如果使用的是get方法，此值不管是否设置，都会执行一次重试，如果设置为post方法且设置为false时，则不会重试
            ConnectTimeout: 5000 #连接建立的超时时长，默认1秒
            ReadTimeout: 5000 #处理请求的超时时长，默认1秒    
            坑点:如果只有一台服务器，他会默认自己本身为下一台服务器，进行一次重试，
            这个策略会在每台服务器上执行每个完整过程
            如果集群里面有2台服务器，MaxAutoRetries=1， MaxAutoRetriesNextServer = 2，执行流程如下
            A: 执行一次正常 执行一次重试 
            B: 执行一次正常 执行一次重试 MaxAutoRetriesNextServer 第一次
            A: 执行一次正常 执行一次重试 MaxAutoRetriesNextServer 第二次
            一共调用6次, 切记切记
    ```
+ 并发
  + 工具 JMeter
  + 修改Tomcat并发数
    ```yaml
        server:
          port: 8080
          tomcat:
            max-threads: 10   #tomcat最大并发修改为10，默认值为200
    ```
  + 容错方案
    + 隔离
    + 超时
    + 限流
    + 熔断 
    + 降级
  + 选用方案
    + 使用阿里的sentinel组件
        + 基本概念
          + 资源
          + 规则
        + 功能
          + 流量控制
            + 保证自己不被上级服务压垮
          + 熔断降级
            + 保证自己不被下游服务拖垮
          + 系统负载保护
            + 保证外界环境良好 (CPU RAM 网络)
        + 控制台
            + 启动控制台
                ```java
                    java -jar sentinel-dashboard-1.7.0.jar
                ```                 
            + 用户名 sentinel
            + 密码 sentinel
        + 客户端集成
            ```xml
                <!-- 服务组件 -->
                    <dependency>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
                    </dependency>
            ```
        + 添加测试方法
            ```java
                @PostMapping("message1")
                public String message1(){
                return "message1";
                }
            
                @PostMapping("message2")
                public String message2(){
                return "message2";
                }
            ```
        + 应用配置
            ```xml
                spring:
                    cloud:
                        sentinel:
                            transport:
                                port: 8081 #与控制台交流的端口， 任意一个未使用的端口都可以
                                dashboard: localhost:8080 # 制定控制台服务地址
            ```
        + 系统整合配置
            ```xml
                feign:
                    sentinel:
                        enabled: true
            ```
###Easy excel
  + 依赖
    ```xml
    <dependencies>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>easyexcel</artifactId>
            <version>2.1.7</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.5</version>
        </dependency>

        <dependency>
            <groupId>org.apache.xmlbeans</groupId>
            <artifactId>xmlbeans</artifactId>
            <version>3.1.0</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
    ```
###事务处理
+ 必须在mybatis配置类中，允许事务
```java
@EnableTransactionManagement //必须在mybatis配置类中，允许事务
@Configuration
@MapperScan("com.windvalley.guli.service.*.mapper")
public class MybatisPlusConfig {
    /*
    分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
```
+ 在对应的Service开启事务注解
```java
    @Transactional(rollbackFor = Exception.class) //只要发生异常就回滚
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存 cursor
        Course course = saveCourseInfoByFormInfo(courseInfoForm);

        //保存 cursor description
        saveCourseDescByFormInfo(course.getId(), courseInfoForm);

        return course.getId();
    }
```