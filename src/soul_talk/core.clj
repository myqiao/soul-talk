(ns soul-talk.core
  (:require 
    [clojure.java.io :as io]
    
    [ring.adapter.jetty :as jetty]
    [ring.util.response :refer [redirect]]
    [ring.util.http-response :as resp]
    [ring.middleware.reload :refer [wrap-reload]]
    
    ;; 引入常所用中间件
    [ring.middleware.defaults :refer :all]
    
    ;; 引入路由函数
    [compojure.core :refer [routes GET defroutes POST]]
    [compojure.route :as route]

    ;; 引入模板库
    [selmer.parser :as parser]

    ;; 引入静态资源库中间件
    [ring.middleware.webjars :refer [wrap-webjars]]))


;; 渲染 index.html 页面
(defn home-handle [request]
  (parser/render-file "index.html"  request))


;; 渲染 error.html 404 页面
(defn error-page [error-details]
  {:status (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (parser/render-file "error.html" error-details)})


;; Get 登录页面
(defn login-page [request]
  (parser/render-file "login.html" {}))

;; Post 登录数据
(defn handle-login [email password request]
  (if (and (= email "jiesoul@gmail.com") (= password "12345678"))
  	;; 如果登录成功，则在 Session 中添加信息
    (home-handle (assoc-in request [:session :identity] email))
    ;; (-> (redirect "/") (assoc :session {:identity email}))
    ;; 如果失败，则返回登陆页面，并向页面中传送错误信息
    (login-page (assoc request :error "用户名密码不对"))))

;; 退出登录，清空 Session ，调整到首页
(defn handle-logout [request]
  (do
    (assoc request :session {})
    (redirect "/")))


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

    ;; 登录路由，Get 和 POST=============
    (GET "/login" request (login-page request))
    (POST "/login" [email password :as req] (handle-login email password req))

    ;; 退出登录路由==========
    (GET "/logout" request (handle-logout request))

    ;; 注意这里：路由可以接受一个函数
    (route/not-found error-page)))  


(def app
  (-> app-routes  ;; 这里改为路由返回的 Handler
      (wrap-nocache)
      (wrap-reload)
      ;; 静态资源中间件
      (wrap-webjars)  ;; 这行添加的

      ;; 常用中间件集合
      ;; 关闭跨域攻击中间件========
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))