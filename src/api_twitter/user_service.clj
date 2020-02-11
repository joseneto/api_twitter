(ns	api-twitter.user-service
  (:require  [api-twitter.db :as db]))

;;Service file for users

(defn load-user [content]
  (db/find-user content))

(defn valid-register [user-register]
  (and (contains? user-register	:username)
       (contains? user-register :email)
       (contains? user-register	:pass_hash)
       (not (clojure.string/blank? (user-register :username)))
       (not (clojure.string/blank? (user-register :email)))
       (not (clojure.string/blank? (user-register :pass_hash)))))

(defn valid-sign [user-sign]
  (or (contains? user-sign :username)
      (contains? user-sign :email)
      (not (clojure.string/blank? (user-sign :username)))
      (not (clojure.string/blank? (user-sign :email)))
      (and (contains? user-sign	:pass_hash)
      (not (clojure.string/blank? (user-sign :pass_hash))))))

(defn register-not-exist? [count-register]
  (= (db/count-register count-register) 0))

(defn register [content]
    (db/save content))

(defn- value-equals? [my-map value2]
  (= (:pass_hash my-map) value2))

(defn eval-login [user-map pass_hash]
  (if (value-equals? user-map pass_hash)
    user-map
    []))

(defn sign [content]
  (eval-login (load-user content) (content :pass_hash) ))



  