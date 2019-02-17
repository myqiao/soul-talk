(ns soul-talk.core
  (:require 
    [clojure.java.io :as io]
    
    [ring.adapter.jetty :as jetty]
    [ring.util.response :as res :refer [redirect]]
    [ring.util.http-response :as resp]
    [ring.middleware.reload :refer [wrap-reload ]]
    [ring.middleware.session :refer [wrap-session]]
    
    [soul-talk.auth-validate :as auth-validate]
    
    ;; 引入常所用中间件
    [ring.middleware.defaults :refer :all]
    
    ;; 引入路由函数
    [compojure.core :refer [routes GET defroutes POST]]
    [compojure.route :as route]

    ;; 引入模板库
    [selmer.parser :as parser]

    ;; 引入静态资源库中间件
    [ring.middleware.webjars :refer [wrap-webjars]]
    
    ;; 支持 JSON 格式的中间件
    [ring.middleware.format :as wrap-format]

    [soul-talk.routes.auth :refer [auth-routes]]))


;; 渲染 index.html 页面
(defn home-handle [request]
  (parser/render-file "index.html"  request))


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
  (-> (routes auth-routes app-routes) 
    
      (wrap-nocache)
      (wrap-reload)
      ;; 静态资源中间件
      (wrap-webjars)  ;; 这行添加的
      
      (wrap-format/wrap-restful-format :formats [:json-kw])

      (wrap-session)  ;;加入这个

      ;; 常用中间件集合
      ;; 关闭跨域攻击中间件========
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))