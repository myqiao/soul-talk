(ns soul-talk.core
  (:require 
    [ring.adapter.jetty :as jetty]
    [ring.util.http-response :as resp]))

(defn home-handle [request]
  ;; 这里简化了代码
  (resp/ok (str "<html><body><body>your IP is"
                (:remote-addr request) 
                "</body></html>")))

(defn -main []
  (jetty/run-jetty home-handle  {:port 3000 :join? false}))