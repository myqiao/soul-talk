(ns soul-talk.login
  (:require [domina :as dom]
            [domina.events :as ev]
            [reagent.core :as reagent :refer [atom]]))


;; 密码格式
(def ^:dynamic *password-re* #"^(?=.*\d).{4,128}$")

;; Email 格式
(def ^:dynamic *email-re* #"^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$")


;; 验证 Email 是否为空
(defn validate-email [email]
  (if (re-matches *email-re* (dom/value email))
    true
    false))

;; 验证密码是否为空
(defn validate-password [password]
  (if (re-matches *password-re* (dom/value password))
    true
    false))


;; 验证表单输入
(defn validate-form []
  (let [email (dom/by-id "email")
        password (dom/by-id "password")]
    (if (and (validate-email email) (validate-password password))
      true
      (do
        (js/alert "email和密码不能为空")
        false))))

;; 如果验证不成功，则在输入框上增加样式；
;; 如果验证成功，则移除样式
(defn validate-invalid [input-id vali-fun]
  (if-not (vali-fun input-id)
    (dom/add-class! input-id "is-invalid")
    (dom/remove-class! input-id "is-invalid")))


;; 组件化登陆表单
(defn login-component []
  ;; 登陆表单
  [:form#loginForm.form-signin {:action "/login" :method "post"}
    ;; 标题
    [:h1.h3.mb-3.font-weight-normal "Please sign in"]

    ;; Email 部分
    [:div.form-group
      ;; Email 标签
      [:label.sr-only "email" "email"]
      ;; Email 输入框
      [:input#email.form-control
        {:type "text" 
         :name "email" 
         :auto-focus true 
         :placeholder "Email Address"
         ;; 焦点丢失的时候，调用验证函数
         :on-blur #(validate-invalid (dom/by-id "email") validate-email)}] 
      ;; 错误提示信息
      [:div.invalid-feedback "无效的 Email"]] 

    ;; 密码部分
    [:div.form-group
      ;; 密码输入框
      [:label.sr-only "password" "password"]
      [:input#password.form-control
        {:type "password" 
         :name "password" 
         :placeholder "password"
         ;; 焦点丢失的时候，调用验证函数
         :on-blur    #(validate-invalid (dom/by-id "password") validate-password)}]
      ;; 错误提示信息
      [:div.invalid-feedback "无效的密码"]] 


    ;; “记住我” 复选框
    [:div.form-group.form-check
      [:input#rememeber.form-check-input {:type "checkbox"}]
      [:label "记住我"]]

    ;; 错误信息
    [:div#error]

    ;; 提交按钮
    [:input#submit.btn.btn-lg.btn-primary.btn-block {:type "submit" :value "登录"}]

    ;; 版权信息
    [:p.mt-5.mb-3.text-muted "&copy @2018"]])






;; 为 Form 绑定 onsubmit 处理函数
;; 导出该函数
(defn ^:export init []
  ;; 渲染登陆表单组件，并挂载到 `content` div元素上
  (reagent/render
    [login-component] (dom/by-id "content"))
  
  (if (and js/document (.-getElementById js/document))
    (let [login-form (dom/by-id "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))