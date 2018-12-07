(defproject soul-talk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 
                 ;; Ring 库
                 [ring "1.7.1"]
                 ;; 基于 Ring 的 Response工具库
                 [metosin/ring-http-response "0.9.1"]]
  
  
  
  
  :main soul-talk.core
  
  
  :profiles {
        :user {
            :dependencies []
            :plugins [[lein-ancient "0.6.15"]]}})
