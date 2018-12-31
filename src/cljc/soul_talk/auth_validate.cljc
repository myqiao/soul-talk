(ns soul-talk.auth-validate)

;; 密码格式
(def ^:dynamic *password-re* #"^(?=.*\d).{4,128}$")
;; Email 格式
(def ^:dynamic *email-re* #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$")


;; 验证 Email 是否为空
(defn validate-email [email]
  (if (re-matches *email-re* email);;修改，参数变为文本，而不是 HTML 元素
    true
    false))

;; 验证密码是否为空
(defn validate-password [password]
  (if (re-matches *password-re* password);;修改，参数变为文本，而不是 HTML 元素
    true
    false))