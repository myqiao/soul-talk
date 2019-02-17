(ns soul-talk.auth-validate
	(:require [bouncer.core :as b]
           [bouncer.validators :as v]))

;; 密码格式
(def ^:dynamic *password-re* #"^(?=.*\d).{4,128}$")
;; Email 格式
(def ^:dynamic *email-re* #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$")


;; 验证 Email 是否为空
(defn validate-email [email]
  (if (and (not (nil? email)) (re-matches *email-re* email));;修改，参数变为文本，而不是 HTML 元素
    true
    false))

;; 验证密码是否为空
(defn validate-password [password]
  (if (and (not (nil? password)) (re-matches *password-re* password));;修改，参数变为文本，而不是 HTML 元素
    true
    false))


(defn login-errors [params]
  (first
    (b/validate
      params
      :email [[v/required :message "email 不能为空"]
              [v/email :message "email 不合法"]]
      :password [[v/required :message "密码不能为空"]
                 [v/min-count 7 :message "密码最少8位"]])))


(defn reg-errors [{:keys [pass-confirm] :as params}]
  (first
    (b/validate
      params
      :email [[v/required :message "email 不能为空"]
              [v/email :message "email 不合法"]]
      :password [[v/required :message "密码不能为空"]
                 [v/min-count 7 :message "密码最少8位"]
                 [= pass-confirm :message "两次密码必须一样"]])))