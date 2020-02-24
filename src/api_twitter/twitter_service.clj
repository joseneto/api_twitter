(ns	api-twitter.twitter-service
  (:require  [api-twitter.db :as db]))

;;Service files for twitters

(defn- valid-oid [oid]
  (re-matches #"^[0-9a-fA-F]{24}$" oid))

(defn valid-twitter[twitter-map]
  (and (contains? twitter-map :user-name)
       (contains? twitter-map :post)
       (not (clojure.string/blank? (twitter-map :user-name)))
       (not (clojure.string/blank? (twitter-map :post)))
       (<= (count (twitter-map :post)) 280)))

(defn valid-my-twitters [user]
  (not (clojure.string/blank? user)))

(defn valid-view-twitter [id]
    (and (not (clojure.string/blank? id))
         (valid-oid id)))

(defn view-twitter [id]
  (if-let [ result (db/get-twitter-user id)]
    result
    {:message "Twitter does not exist"}))

(defn valid-like [user-like]
  
  (and (contains? user-like :user-name)
       (contains? user-like :user-twitter)
       (contains? user-like :twitter-oid)
       (not (clojure.string/blank? (user-like :user-name)))
       (not (clojure.string/blank? (user-like :user-twitter)))
       (not (clojure.string/blank? (user-like :twitter-oid)))
       (valid-oid (user-like :twitter-oid))
       (view-twitter (user-like :twitter-oid))))

(defn twitter-post [content]
  (db/push-twitter content))

(defn my-twitters [user-name skip]
 (db/list-twitters-user user-name skip) )

(defn like-twitter [like-map]
  
  (if (not (db/get-user-like-in-twitter (:user-name like-map) (:twitter-oid like-map)) )
    (db/push-like-twitter (:user-name like-map) (:user-twitter like-map) (:twitter-oid like-map))
    (db/pull-like-twitter (:user-name like-map) (:user-twitter like-map) (:twitter-oid like-map)))
  )