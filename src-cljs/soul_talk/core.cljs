(ns soul-talk.core)

;; 表单提交处理函数：账户和密码不能为空
(defn validate-form []
  (let [email (.getElementById js/document "email")
        password (.getElementById js/document "password")]
    (if (and (> (count (.-value email)) 0) (> (count (.-value password)) 0))
      true
      (do (js/alert "email和密码不能为空")
          false))))

;; 为 Form 绑定 onsubmit 处理函数
;; 导出该函数
(defn ^:export init []
  (if (and js/document (.-getElementById js/document))
    (let [login-form (.getElementById js/document "loginForm")]
      (set! (.-onsubmit login-form) validate-form))))