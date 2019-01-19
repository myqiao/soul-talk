(defproject soul-talk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies [
                 ;; 基础库
                 [org.clojure/clojure "1.10.0"]
                 
                 ;; Ring 库
                 [ring "1.7.1"]
                 ;; 基于 Ring 的 Response工具库
                 [metosin/ring-http-response "0.9.1"]
                 ;; 常用中间件集合
                 [ring/ring-defaults "0.3.2"]
                 
                 ;; 路由库
                 [compojure "1.6.1"]

                 ;; 模板库
                 [selmer "1.12.5"]

                 ;; 前端静态资源库
                 [ring-webjars "0.2.0"]
				         [org.webjars/jquery "3.3.1-1"]
				         [org.webjars/bootstrap "4.2.1"]
				         [org.webjars/popper.js "1.14.6"]
                 [org.webjars/font-awesome "5.6.3"]
                
                 ;; 启用 ClojureScript 支持
                 [org.clojure/clojurescript "1.10.439"]

                 ;; ClojureScript 版的 jQuery
                 [domina "1.0.3"]

                 ;; Reagent 库
                 [reagent "0.8.1"]
                 ;; Reagent 工具库
                 [reagent-utils "0.3.2"]

                 ;; 引入 Ajax 支持
                 [cljs-ajax "0.8.0"]
                 
                 ;; 支持 JSON 格式的中间件
                 [ring-middleware-format "0.7.3"]

                 ;; 增加数据库功能
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.xerial/sqlite-jdbc "3.25.2"]

                 ;; 数据库迁移
                 [ragtime "0.8.0"]]

  
  
  :plugins [
      ;; 基于 Lein 的 Ring 插件
      [lein-ring "0.12.4"]
      ;; Cljsbuild 编译器插件
      [lein-cljsbuild "1.1.7" :excludes [[org.clojure/clojure]]]
      ;; figwheel 环境插件
      [lein-figwheel "0.5.17-SNAPSHOT"]]

  
  ;; Ring 插件不通过 main 函数启动，只需要指定一个入口 Handler
  :ring {:handler soul-talk.core/app}
  
  
  ;; 不使用插件的时候，程序仍然从 main 函数启动
  ;; 启用 ClojureScript 之后，要关闭预编译 AOT
  ;; :main ^:skip-aot soul-talk.core
  
  
  ;; 指定源文件和资源文件路径
  :source-paths ["src" "src/cljc"]
  :resource-paths ["resources"]

  ;; 为 figwheel 指定 CSS 路径
  :figwheel {:css-dirs ["resources/public/css"]}

  ;; 设置自动清理路径
  :clean-targets ^{:protect false} [
    :target-path 
      ;; 下面的路径根据 cljsbuild 配置查找
      [:cljsbuild :builds :dev :compiler :output-dir]
      [:cljsbuild :builds :dev :compiler :output-to]]


  ;; 设置 cljsbuild 编译器参数
  :cljsbuild {
    :builds {
      ;; 开发环境
      :dev {
        ;; 源代码目录
        :source-paths ["src-cljs" "src/cljc"] 
        ;; 开启 figwheel                    
        :figwheel     true                             
        :compiler {
          ;; 主命名空间
          :main                   soul-talk.core  
          ;; 依赖文件路径
          :asset-path             "js/out"    
          ;; 最终输出的文件        
          :output-to              "resources/public/js/main.js"   
          ;; 临时文件输出路径
          :output-dir             "resources/public/js/out"
          ;; 不优化    
          :optimizations          :none
          ;; 源代码
          :source-map-timestamp   true  
          ;; 打印格式               
          :pretty-print           true}}}}      


  :profiles {
        :dev {
            :source-paths ["env/dev/clj"]
            :plugins [[lein-ancient "0.6.15"]]
            :dependencies [[ring/ring-devel "1.7.1"]]}}

)
