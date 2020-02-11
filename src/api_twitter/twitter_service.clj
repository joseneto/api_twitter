(ns	api-twitter.twitter-service
  (:require  [api-twitter.db :as db]))

;;Service files for twitters

(defn valid-twitter[twitter-map]
  (and (contains? twitter-map :username)
       (contains? twitter-map :post)
       (not (clojure.string/blank? (twitter-map :username)))
       (not (clojure.string/blank? (twitter-map :post)))
       (<= (count (twitter-map :post)) 280)))

(defn valid-my-twitters [user]
  (not (clojure.string/blank? user)))

(defn valid-view-twitter [user id]
    (and (not (clojure.string/blank? user))
         (not (clojure.string/blank? id))))

(defn twitter-post [content]
  (db/push-twitter content))

(defn my-twitters [user-map]
 (db/list-twitters-user user-map) )

(defn view-twitter [id]
  (db/get-twitter-user id))