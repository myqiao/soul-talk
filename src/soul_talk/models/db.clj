(ns soul-talk.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec 
	{ :dbtype "sqlite"
	  :classname "org.sqlite.JDBC"
	  :subprotocol "sqlite"
	  :subname "db.sqlite"
	  :dbname "db.sqlite"})

;; 使用 JDBC 查询语句，返回 3*5
(defn test-db []
  (sql/query db-spec "select 3*5 as result"))


(defn save-user! [user]
  (sql/insert! db-spec :users user))

(defn select-user [id]
  (sql/query db-spec ["SELECT * FROM users where email = ? " id]))

(defn select-all-users []
  (sql/query db-spec ["SELECT * from users"]))