(ns	api-twitter.user-service
  (:require  [api-twitter.db :as db]))

;;Service file for users

(defn load-user [content]
  (db/find-user content))

(defn valid-register [user-register]
  (and (contains? user-register	:user-name)
       (contains? user-register :email)
       (contains? user-register	:pass-hash)
       (not (clojure.string/blank? (user-register :user-name)))
       (not (clojure.string/blank? (user-register :email)))
       (not (clojure.string/blank? (user-register :pass-hash)))))

(defn valid-sign [user-sign]
  (or (contains? user-sign :user-name)
      (contains? user-sign :email)
      (not (clojure.string/blank? (user-sign :user-name)))
      (not (clojure.string/blank? (user-sign :email)))
      (and (contains? user-sign	:pass-hash)
      (not (clojure.string/blank? (user-sign :pass-hash))))))

(defn register-not-exist? [count-register]
  (= (db/count-register count-register) 0))

(defn register [content]
    (db/save-user {:userName (:user-name content) :email (:email content) :passHash (:pass-hash content)}))

(defn- value-equals? [value1 value2]
  (= value1 value2))

(defn eval-login [user-map pass-hash]
  (if user-map
    (if (value-equals? (user-map :passHash) pass-hash)
      user-map
      [])
    []))

(defn sign [content]
  (eval-login (load-user content) (content :pass-hash) ))



  