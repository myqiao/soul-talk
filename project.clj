(defproject soul-talk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 
                 ;; Ring 库
                 [ring "1.7.1"]
                 ;; 基于 Ring 的 Response工具库
                 [metosin/ring-http-response "0.9.1"]
                 ;; 常用中间件集合
                 [ring/ring-defaults "0.3.2"]
                 
                 ;; 路由库
                 [compojure "1.6.1"]]
  
  
  ;; 基于 Lein 的 Ring 插件
  :plugins [[lein-ring "0.12.4"]]

  ;; 插件不通过 main 函数启动，只需要指定一个入口 Handler
  :ring {:handler soul-talk.core/app}
  
  ;; 不使用插件的时候，程序仍然从 main 函数启动
  :main soul-talk.core
  
  
  :profiles {
        :user {
            :dependencies []
            :plugins [[lein-ancient "0.6.15"]]}})
