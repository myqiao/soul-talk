(ns user
  (:require [soul-talk.models.db :as db :refer [db-spec]]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as rag-repl]))

;; ragtime 的迁移配置
(def config
  {
  	;; 数据库
  	:datastore  (jdbc/sql-database db-spec)
  	;; 迁移目录
  	:migrations (jdbc/load-resources "migrations")})