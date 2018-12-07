(ns soul-talk.core
  (:require 
    [ring.adapter.jetty :as jetty]
    [ring.util.http-response :as resp]
    [ring.middleware.reload :refer [wrap-reload]]))

(defn home-handle [request]
  ;; 这里简化了代码
  (resp/ok (str "<html><body><body>your IP isss："
                (:remote-addr request) 
                "</body></html>")))

;; 自定义中间件：加入不缓存头信息
(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))


(def app
  (-> home-handle
      wrap-nocache
      wrap-reload))


(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))