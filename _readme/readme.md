https://blog.csdn.net/abc465200/article/details/107493629

#Windvalley 项目说明

##前端

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
  1. 在调用项目的POM里配置依赖
  ```html
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
  ```  
  2. 启动类中添加注解
   ```java
     @EnableFeignClients
   ```  
  