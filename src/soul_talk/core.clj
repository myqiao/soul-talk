(ns soul-talk.core
  (:require [ring.adapter.jetty :as jetty]))

(defn home-handle [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<html><body>your IP is "
              (:remote-addr request)
              "</body></html>")})

(defn -main []
  (jetty/run-jetty home-handle  {:port 3000 :join? false}))