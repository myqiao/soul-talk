(ns soul-talk.core
  (:require 
    [clojure.java.io :as io]
    
    [ring.adapter.jetty :as jetty]
    [ring.util.http-response :as resp]
    [ring.middleware.reload :refer [wrap-reload]]
    
    ;; 引入常所用中间件
    [ring.middleware.defaults :refer :all]
    
    ;; 引入路由函数
    [compojure.core :refer [routes GET]]
    [compojure.route :as route]

    ;; 引入模板库
    [selmer.parser :as parser]

    ;; 引入静态资源库中间件
    [ring.middleware.webjars :refer [wrap-webjars]]))


;; 渲染 index.html 页面，并传送数据到 :ip 参数
(defn home-handle [request]
  (parser/render-file "index.html"  {:ip (:remote-addr request)}))


;; 渲染 error.html 404 页面
(defn error-page [error-details]
  {:status (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (parser/render-file "error.html" error-details)})


;; 自定义中间件：加入不缓存头信息
(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))


;; 创建路由规则，每个规则使用不同的 Handler
;; 最终返回的是一个普通的 Handler
(def app-routes
  (routes
    (GET "/" request (home-handle request))
    (GET "/about" [] (str "这是关于我的页面"))
    ;; 注意这里：路由可以接受一个函数
    (route/not-found error-page)))  


(def app
  (-> app-routes  ;; 这里改为路由返回的 Handler
      (wrap-nocache)
      (wrap-reload)
      ;; 静态资源中间件
      (wrap-webjars)  ;; 这行添加的
      ;; 常用中间件集合
      (wrap-defaults site-defaults)))


(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))