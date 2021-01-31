#Windvalley 项目说明

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


+ 1
  + abc
    
+ 2
+ 3
