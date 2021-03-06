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
    [compojure.route :as route]))

(defn home-handle [request]
  (io/resource "index.html"))

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
    (route/not-found "<h1>Page not found</h1>")))


(def app
  (-> app-routes  ;; 这里改为路由返回的 Handler
      (wrap-nocache)
      (wrap-reload)
      
      ;; 插入常用中间件
      (wrap-defaults site-defaults)))


(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))