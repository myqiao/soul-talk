(ns soul-talk.login
  (:require [domina :as dom]
            [domina.events :as ev]
            [reagent.core :as reagent :refer [atom]]
            ;; 引入共享代码
            [soul-talk.auth-validate :as validate]
            ;; 引入 Ajax 支持
            [ajax.core :as ajax :refer [POST]]))


(def login-data (atom {:email "" :password ""}))


;; 如果验证不成功，则在输入框上增加样式；
;; 如果验证成功，则移除样式
;; 这个函数，输入框失去焦点的时候被调用
(defn validate-invalid [input vali-fun]
  (if-not (vali-fun (.-value input))  ;; 验证函数传入文本，而不是 HTML 元素
    (dom/add-class! input "is-invalid")
    (dom/remove-class! input "is-invalid")))


;; Ajax 成功后调用，打印响应数据
(defn handler-ok [response]
  (.log js/console (str "response: " response)))

;; Ajax 失败后调用，显示错误
(defn handler-error [{:keys [status message] :as resp}]
  (js/alert "error"))


(defn login! []
  (ajax/POST
    "/login"
    {:format        :json
     :headers       {"Accept" "application/transit+json"}
     :params        @login-data
     :handler       handler-ok
     :error-handler handler-error}))


;; 这个函数提交的时候被调用，类客户端验证输入格式是否正确
(defn validate-form []
  ;; 注意这里的变化
  ;; 数据不再是从元素中直接读取，而是从 JSON 数据中读取
  (if (and (validate/validate-email (:email @login-data))
           (validate/validate-password (:password @login-data)))

    ;; 注意这里的变化：之前验证成功返回 true ，则表单可以提交
    ;; 现在是调用 login! 函数，利用 ajax 从后台读取据，不提交也不刷新页面
    (login!)
    (do
      (js/alert "email和密码不合法")
      false)))



;; 组件化登陆表单
(defn login-component []
  [:div.container
   ;; 登陆表单
   [:div#loginForm.form-signin
    ;; 标题
    [:h1.h3.mb-3.font-weight-normal.text-center "Please sign in"]

    ;; Email 部分
    [:div.form-group
     ;; Email 标签
     [:label "Email address"]
     ;; Email 输入框
     [:input#email.form-control
      {:type        "text"
       :name        "email"
       :auto-focus  true
       :placeholder "Email Address"
       ;; 焦点丢失的时候，调用验证函数
       :on-change   (fn [e]
                      (let [d (.. e -target)]
                        (swap! login-data assoc :email (.-value d))
                        (validate-invalid d validate/validate-email)))}]
     ;; 错误提示信息
     [:div.invalid-feedback "无效的 Email"]]


    ;; 密码部分
    [:div.form-group
     [:label "Password"]
     ;; 密码输入框
     [:input#password.form-control
      {:type        "password"
       :name        "password"
       :placeholder "password"
       ;; 焦点丢失的时候，调用验证函数
       ;; 之前的代码仅仅将元素和要使用的函数传给 validate-invalid
       ;; 现在还需要向 JSON 中添加数据
       ;; 此外，之前直接通过 id 获取组件 ，现在则是通过事件对象 e 获得组件
       :on-change     (fn [e]
                      (let [d (.-target e)]
                        (swap! login-data assoc :password (.-value d))
                        (validate-invalid d validate/validate-password)))}]
     ;; 错误提示信息
     [:div.invalid-feedback "无效的密码"]]

    ;; “记住我” 复选框
    [:div.form-group.form-check
     [:input#rememeber.form-check-input {:type "checkbox"}]
     [:label "记住我"]]

    ;; 错误信息
    [:div#error.invalid-feedback]

    ;; 提交按钮
    [:input#submit.btn.btn-lg.btn-primary.btn-block
     {:type  "button"
      :value "登录"
      :on-click #(validate-form)}]

    ;; 版权信息
    [:p.mt-5.mb-3.text-muted "&copy @2018"]]])



;; 渲染登陆表单组件，并挂载到 `content` div元素上
(defn load-page []
  (reagent/render
    [login-component]
    (dom/by-id "content")))


(defn ^:export init []
  (if (and js/document
           (.-getElementById js/document))
    (load-page)))

