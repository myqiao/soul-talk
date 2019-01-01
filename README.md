# 开发分支

## dev05 - 增加 Ajax 支持

### 提交01：创建分支

### 提交02：完成客户端的 Ajax 支持

主要进行了以下几项修改

- 创建了一个 `login-data` 变量，保存客户端登陆数据，他会通过 Ajax 被发送到服务端
- 之前输入框丢失焦点后，仅仅将输入框和要使用的验证函数传给 `validate-invalid` ；现在还需要向 JSON 变量中添加数据
- 之前输入框丢失焦点后，直接通过 `id` 获取组件 ，现在则是通过事件对象 `e` 获得组件
- 之前点击提交按钮后，`validate-form` 直接从数据框中读取数据进行验证，现在从 JSON 变量中读取数据进行验证
- 之前点击提交按钮，验证成功后，返回 `true` ，然后提交到服务器；现在验证成功后，通过 Ajax 提交数据，页面不刷新

### 提交03：完成服务端的 Ajax 支持

- 添加一个中间件让 Ring 解析 JSON 格式的中间件
- 修改了 `login` 的 POST 路由
- 修改了登录 handler
- 把客户端页面的 `form` 改为 `div` 元素
- 配置 `wrap-format/wrap-restful-format` 参数，让 JSON 数据关键字化


## dev04 - 使用 Clojurescript 进行客户端验证，组件化页面

- 使用原生 DOM 接口进行客户端验证，注意：`login.html` 中两个输入框的 `required` 属性得删除，否则会影响逻辑流程
- 使用 Domina 库操作 DOM
- 将验证代码分离到单独的 login.cljs 中，注意要在 `core.cljs` 引入 `login.cljs`，另外，在页面中应用命名空间的时候要使用下划线
- 使用 Reagent 组件化登录页面
- 之前是点击提交按钮后验证，修改为输入框丢失焦点后就验证。
- 加入正则表达式，对输入文本进行格式验证

## dev03 - 配置 Clojurescript 支持环境，并完成登录功能

- 配置 Clojurescript 支持环境
- 使用 Selmer 模板的继承扩展功能
- 实现登录登出功能，能够显示登录状态

## dev02 - 使用 Selmer 模板和静态资源库

## dev01 - 搭建一个基本的 Clojure Web 程序架构



