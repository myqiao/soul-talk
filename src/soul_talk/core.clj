(ns soul-talk.core
  (:require 
    [ring.adapter.jetty :as jetty]
    [ring.util.http-response :as resp]))

(defn home-handle [request]
  ;; 这里简化了代码
  (resp/ok (str "<html><body><body>your IP is"
                (:remote-addr request) 
                "</body></html>")))

;; 自定义中间件：加入不缓存头信息
(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))



(defn -main []
  (jetty/run-jetty 
    (wrap-nocache home-handle)  
    {:port 3000 :join? false}))