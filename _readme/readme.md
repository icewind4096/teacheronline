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
      + npm install jquery@2.1.3 安装jQuery的2.1.3版本f
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
6. 修改调用服务器地址 util\request.js
```javascript
const service = axios.create({
  baseURL: 'http://localhost:8180', //此处为目标服务器地址
  timeout: 12000 // 请求超时时间
})
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

## Nuxt使用
>客户端渲染
1. 异步数据获取, 在前端服务器执行, asyncData()在所有方法之前就被建立，类似于类的静态方法，对象不被Create，就已经存在，只能调用一个API
```javascript
  asyncData(page) {
    return teacherApi.get(page.route.params.id).then(response => {
      console.log(response.data)
      return {
        teacher: response.data.items.teacher,
        courseList: response.data.items.courseList
      }
    })
  }
```
2. 同步数据获取, 在前端服务器执行, async asyncData() wait, 可以调用多个API
```javascript
  async asyncData(page) {                                                           //区别1. 多了 async关键字
    const response = await teacherApi.get(page.route.params.id)                     //区别2. 多了 await关键字
    teacher: response.data.items.teacher,
    const response = await teacherApi.getCourseByTeacheId(page.route.params.id)     //区别2. 多了 await关键字
    courseList: response.data.items.courseList
```
## querystring 
>URL参数拼接工具
```javascript
      const queryObject = {
        subjectParentId: 10
        subjectId: 20
        buyCountSort: 1
      }
      const queryString = querystring.stringify(queryObject)
      此处 queryString = "subjectParentId=10&subjectId=20&buyCountSort=1"   
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
###打包
>如果使用了mybatis,spring默认打包是不包含mapper.xml文件的，所以要在pom.xml中添加包含xml的例外，以便项目打包时把xml文件也打包在内。
```xml
    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>
```

##mybatis
###使用代码生成器产生框架代码
>具体参考对应模块\src\test目录下得CodeGenerator.java

##redis
###安装
> 安装GCC编译器，否则编译出错
> yum install -y gcc g++ gcc-c++ make
> 安装tcl， 否则运行redis make test命令可能会失败  
> sudo yum install tcl
1. wget http://download.redis.io/releases/redis-5.0.7.tar.gz
2. tar -zvxf redis-5.0.7.tar.gz
3. mv /root/redis-5.0.7 /usr/local/redis
4. cd /usr/local/redis
5. make   -> 此处为编译
6. make PREFIX=/usr/local/redis install ->此处为安装
7. 调试阶段 关闭防火墙 systemctl stop firewalld
8. 调试阶段 开机禁用防火墙 systemctl disable firewalld
7. 编辑redis.conf 
8. 注释 bind 127.0.0.1
9. 关闭保护模式 protected-mode = no
10. 运行 src/redis-server redis.conf
11. 修改redis数据库文件 xxx.rdb的权限, 例如 chmod 777 dump.rdb

###基本用法
#### redis基本数据类型     
    string(字符串)  
    list(链表)
    set(无序集合)  
    sorted set(有序集合)  
    hash(Hash表)
#### redis服务/客户端启动  
    ./redis-server                               //正常服务模式启动 port= 6379   
    ./redis-cli                                  //客户端启动   
    ./redis-cli shutdown                         //关闭服务   
#### 修改redis.conf文件中
    port=xxxx                                    //指定默认启动端口  
    requirepass password                         //指定密码
    ./redis-server ..redis.conf                  //指定配置文件启动服务
    ./redis-server --port 6380                   //指定端口启动 port= 6380
    ./redis-cli -p 6380                          //客户端指定端口启动
    ./redis-cli -p 6380 shutdown                 //关闭服务
    ./redis-cli -p 6379 -h 127.0.0.1             //连接指定ip port的redis服务
    ./redis-cli -p 6379 -h 127.0.0.1 shutdown    //关闭指定ip port的redis服务
    ./redis-cli -p 6380 -a password              //使用密码连接
+ redis基本命令
   + info                                         //系统信息
   + select ${number}                             //选择DB
   + flushdb                                      //清除当前选择的db数据
   + flushall                                     //清除全部db数据
   + ping                                         //回音，返回pong
   + dbsize                                       //当前db大小
   + save                                         //使redis数据持久化
   + quit                                         //退出redis-cli连接
   + clear                                        //清除屏幕
   + monitor                                      //查看日志
+ redis键命令
   + keys *                                       //显示当前db中的全部键
   + set key data                                 //设置一个键值对
    + set test 测试数据
   + del key                                      //删除一个键值对
        + del test        
   + exists key                                    //判断一个键值是否存在
        + exists test                                  //存在返回1 不存在返回0
   + ttl key                                       //time to level 查看键的剩余生存时间, 单位为秒, 如果返回值为-1，表示无过期时间, -2表示键不存在
        + ttl test
   + expire test time                             //设置键的生存时间，单位秒
        + expire test 10  
   + type key                                     //返回键的类型
   + randomkey                                    //随机键
   + rename oldKey newKey                         //把oldkey替换为newkey, 如果newKey存在于db中，则newKey会覆盖db中存在的键值
        + rename oldTest newTest
   + renameNX oldKey newKey                       //nx的命令，都带条件判断, 把oldkey替换为newkey,  如果newKey存在于db中，则rename不成功
        renameNX oldTest newTest
6. redis-string
   + setex key sec value                          //setex(set expire)  时间单位为秒
        + setex c 100 c
   + psetex key msec value                        //setex(set expire)  时间单位为毫秒
        + psetex d 10000 d
   + getrange key start end                       //取value，从start开始 end结束 以0开始
        + set country china
   + getrange country 0 2                        //返回 chi
   + getset key value                             //先get后set
        + set a a
        + setget a aa                                 //返回a
   + mset key1 value1 key2 value2 key3 value3     //批量设置键值对
        + mset a a1 b b1 c c1
   + mget key1 key2 key3                          //批量取得多个值
        + mget a b c
   + setNx key value                              //先判断，如果Key存在于db中，则set不成功
   + msetNx key value                             //批量设置 先判断，如果Key存在于db中，则set不成功 必须全部不存在（原子操作）
   + setlen value                                 //返回key对应的value的长度
   + incr key                                     //如果key对应的是数值，则把key对应的value加1
   + decr key                                     //如果key对应的是数值，则把key对应的value减1
   + incrby key step                              //如果key对应的是数值，则把key对应的value加step个
   + decrby key step                              //如果key对应的是数值，则把key对应的value减step个
   + append key appendValue                       //把key对应的value拼接上appendValue
6. redis-hash
   a. hset map key value                           //设置一个hash key是map 值是 key value的键值对  
        hset map name wangjian   
   b. hexists key key1                             //返回为key的hash中的key1是否存在  
   b. hget key key1                                //返回为key的hash中的key1对应的值  
   c. hgetall map                                  //返回全部的hash内的key和value  
   d. hkeys key                                    //返回全部的key对应的hash中的key值  
   e. hvals key                                    //返回全部的key对应的hash中的value值      
   f. hlen key                                     //返回key对应的hash中的数量  
   g. hmget key key1 key2                          //返回key对应的hash中的key1 key2对应的值  
   h. hmset key key1 value1 key2 value2            //设置key对应的hash中的key1 key2对应的 value1 value2  
   i. hdel key key1 key2                           //删除key对应的hash中的key1 key2  
   i. hsetnx key key1 value1                       //批量设置 先判断，如果Key对应的hash存在于db中，并且key1存在,  则set不成功（原子操作）  
7. redis-list(允许出现重复值, 以stack方式存放，先放的在最后)  
   a. lpush key value1 value2 value3 .. valuen     //批量设置名称为key的list中的value值  
   b. llen key                                     //返回名称为key的list的长度  
   c. lrange key start end                         //返回名称为key的list的单元从start开始到end结束的值 0为起始  
   d. lset key pos value                           //设置名称为key的list的第pos个单元的值为value  
   e. lindex key pos                               //返回名称为key的list的第pos个单元的值  
   f. lpop key                                     //移除名称为key的list的第1个单元  
   f. rpop key                                     //移除名称为key的list的最后1个单元  
8. redis-set(不允许出现重复值)  
   a. sadd key value1 value2 ... valuen            //批量添加名称为key的set中的value值, 如果value已经存在，不添加，不存在的value值会继续添加，不会报错  
   b. scard key                                    //返回名称为key的set的数量  
   c. smembers key                                 //返回名称为key的set的成员  
   d. sdiff key1 key2                              //返回名称为key1的set对于key2的set的不同点  
   e. sinter key1 key2                             //返回名称为key1的set和key2的set的交集  
   f. sunion key1 key2                             //返回名称为key1的set和key2的set的并集  
   g. srandomember key number                      //返回名称为key的set中随机Number个元素  
   h. sismember key value                          //返回名称为key的set中, value是不是其成员元素   1存在，0不存在  
   i. srem key value1, value2 .. valuen            //移除名称为key的set中, value1, value2, ... valuen  
   j. spop key                                     //移除名称为key的set中的一个随机value，并返回改value  
9. redis-card(有序，并且允许出现重复值， 数据以键值对方式存放)  
   a. zadd key value1 key1 value2 key2 ... valuen keyn//批量添加名称为key的card中的key value  
   b. zcard key                                    //返回名称为key的card中的元素数量  
   c. zscore key key1                              //返回名称为key的card中的键值为key1的元素  
   d. zcount key rang0 rang1                       //返回名称为key的card中的值的区间在rang0到rang1的元素  
   d. zrank key key1                               //返回名称为key的card中的key值对应的位置索引值 以0起始  
   e. zincrby key number key1                      //把名称为key的card中的key1值对应的value值+number  
   f. zrange key index0 index1                     //返回名称为key的card中，index0-index1区间中元素的key  
   f. zrange key index0 index1 withscores          //返回名称为key的card中，index0-index1区间中元素的key和value  

10. redis分布式配置  
    1. 建立两个redis服务  
    2. 运行redis_1服务，使用默认配置  
    3. 修改redis_2服务配置， 修改端口为6380  
       a. linux  
       vim redis.conf  
       b. window  
       edit redis.window.conf  
    4. 运行redis_2服务   
       redis-server redis.window.conf  
    5. 运行redis_1客户端  
       redis-cli -p 6379  
    6. 运行redis_2客户端  
       redis-cli -p 6380  

###安装依赖
```xml
        <!-- spring boot redis 缓存 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- lecttuce 缓存连接池 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
```
###项目中配置redis
```yaml
在spring节点下配置
spring:
  redis:
    host: 192.168.0.101
    port: 6379
    database: 0
    password: 123456
    lettuce:
      pool:
        max-active: 20 #最大连接数，负值表示无限制 默认=8
        max-wait: -1 #最大阻塞等待时间，负值表示无限制 默认=-1
        max-idle: 8 #最大空闲连接数，默认=8
        min-idle: 0 #最小空闲连接数，默认=8
```
###springboot中使用redis
####编程方式使用
>参考com.windvalley.guli.service.cms.controller.api中的set/get/delete   
>一定要修改redis的默认序列化方式，具体参考service_base/redisconfig.java
####注解方式使用
>首先配置Cache，包括过期时间、数据序列化
```java
    //Cache配置文件
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间600秒
                .entryTtl(Duration.ofSeconds(600))
                //配置序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                //如果数据是NULL，不存入缓存
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();

        return redisCacheManager;
    }
```
>在service_base/redisconfig.java的配置类中，添加@EnableCaching注解
>在业务服务中，添加
> value 和 key 可以参看下图
> ![](resource\redisKeyIndex.png) 
> index类似于group,下面可以存储许多诸如 index:a/index:b/index:c都归结于index这个group  
> 参考ADService.java里面的listByAdTypeId业务方法实现了一下效果  
> @Cacheable(value = "index", key = "'listByAdTypeId'") 的意思就是如果Redis缓存中的index组下面含有一个key等于listByAdTypeId的数据，就直接取出来
> 如果没有就去数据库里面把数据取出来，同时放到index组下面，key值叫listByAdTypeId
> 切记!!key的值，必须保证在系统里为一个唯一值，要不会在redis里面被覆盖
 
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
####阿里云短信服务
#####配置
###### 签名配置  
###### 模板配置
    
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