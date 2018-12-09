# 开发分支

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



