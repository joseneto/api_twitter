(ns	api-twitter.db
  (:import  [com.mongodb WriteResult WriteConcern DBObject]
            org.bson.types.ObjectId
            java.util.Date)
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.json]
            [monger.operators :refer :all])
  )

;;Data base access

(def conn (mg/connect {:host "localhost"}))
(def db (mg/get-db conn "api_twitter_test"))
(def user-coll "users")

(defn save [content]  
  (mc/insert-and-return db user-coll (assoc content :created-at (Date.) :twitters [])))

(defn count-register [content]
 (mc/count db user-coll {$or [{:username (:username content)}
                            {:email (:email content)}]}))

(defn find-user [content]
  (mc/find-one-as-map db user-coll {$or [{:username (:username content)}
                                   {:email (:email content)}]} ["_id" "username" "email" "pass_hash"]))

(defn push-twitter [content]
  (mc/update db user-coll {:username (:username content)} {$push {:twitters (hash-map	:_id (ObjectId.) :twitter (:post content) :created-at (Date.))}})
  true )

(defn list-twitters-user [user]
  (mc/find-one-as-map db user-coll {:username user} ["twitters"] ))

(defn get-twitter-user [oid]
  (let [result (mc/find-maps db user-coll {"twitters._id" (ObjectId. oid)} ["twitters"])]
    (first(:twitters (first result)))))

 
  

(defn clear []
  (mc/remove db user-coll))