(ns soul-talk.routes.auth
  (:require [soul-talk.models.db :as db]
            [ring.util.http-response :as resp]
            [buddy.hashers :as hashers]
            [taoensso.timbre :as log]
            [clj-time.core :as t]
            [soul-talk.auth-validate :refer [reg-errors login-errors]]
            [compojure.core :refer [routes POST GET]]
            [selmer.parser :as parser]))


(defn login! [{:keys [session]} user]
  (if (login-errors user)
    (resp/precondition-failed {:result :error})
    (try
      (let [db-user (db/select-user (:email user))]
        (if (or
              (nil? db-user)
              (not= (hashers/check (:password db-user) (hashers/encrypt (:password user)))))
          (resp/precondition-failed
            {:result :error
             :message "email或密码错误,登录失败"})
          (do
            (assoc user :last-time (t/now))
            (-> {:result :ok}
                (resp/ok)
                (assoc :session (assoc session :identity (:email user)))))))
      (catch Exception e
        (do
          (log/error e)
          (resp/internal-server-error
            {:result :error
             :message "发生内部错误，请联系管理员"}))))))

(defn logout! [request]
  (do
    (assoc request :session {})
    (resp/file-response "/")))




(defn register! [{:keys [session]} user]
  (if (reg-errors user)
    ;; 验证不通过，返回错误
    (resp/precondition-failed {:result :error})
    ;; 验证通过
    (try
      ;; 查找用户是否存在
      (if-let [temp-user (> (count (db/select-user (:email user))) 0)]
        ;; 已经存在
        (resp/precondition-failed {:result :error
                                   :message "email 已被注册"})
        ;; 注册新用户
        (do
          (db/save-user!
            (-> user
                (dissoc :pass-confirm)
                (update :password hashers/encrypt)))
          ;; 返回
          (-> {:result :ok}
              (resp/ok))))
      (catch Exception e
        (do
          ;;(log/error e)
          (resp/internal-server-error
            {:result :error
             :message "发生内部错误，请联系管理员"}))))))


;; 创建一条路由规则
(def auth-routes
  (routes
    (GET "/register" req (parser/render-file "register.html" req))
    (POST "/register" req (register! req (:params req)))
    (GET "/login" request (parser/render-file "login.html" request))
    (POST "/login" req (login! req (:params req)))
    (GET "/logout" request (logout! request))))