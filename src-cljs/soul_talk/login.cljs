(ns soul-talk.login
  (:require 
  	[domina :refer [by-id value by-class destroy!]]
    [domina.events :refer [listen!]]
    [reagent.core :as reagent :refer [atom]]))


;; 组件化登陆表单
(defn login-component []
  ;; 登陆表单
  [:form#loginForm.form-signin {:action "/login" :method "post"}
    ;; 标题
    [:h1.h3.mb-3.font-weight-normal "Please sign in"]

    ;; Email 标签
    [:label.sr-only "email" "email"]
    ;; Email 输入框
    [:input#email.form-control
      {:type "text" 
       :name "email" 
       :auto-focus true 
       :placeholder "Email Address"}]

    ;; 密码输入框
    [:label.sr-only "password" "password"]
    [:input#password.form-control
      {:type "password" 
       :name "password" 
       :placeholder "password"}]

    ;; “记住我” 复选框
    [:label [:input#rememeber {:type "checkbox"}] "记住我"]

    ;; 提交按钮
    [:input#submit.btn.btn-lg.btn-primary.btn-block {:type "submit" :value "登录"}]

    ;; 版权信息
    [:p.mt-5.mb-3.text-muted "&copy @2018"]])


;; 渲染登陆表单组件，并挂载到 `content` div元素上
(reagent/render
  [login-component] (by-id "content"))


;; 表单提交处理函数：账户和密码不能为空
(defn validate-form []
  (let [email (by-id "email")
        password (by-id "password")]
    (if (and (> (count (value email)) 0) (> (count (value password)) 0))
      true
      (do
        (js/alert "Email 和密码不能为空")
        false))))

;; 为 Form 绑定 onsubmit 处理函数
;; 导出该函数
(defn ^:export init []
  (if (and js/document (.-getElementById js/document))
    (let [login-form (by-id "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))