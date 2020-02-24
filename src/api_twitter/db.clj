(ns	api-twitter.db
  (:refer-clojure :exclude [select find sort])
  (:import  [com.mongodb WriteResult WriteConcern DBObject ReadPreference]
            org.bson.types.ObjectId
            java.util.Date)
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.json]
            [monger.query :refer :all]
            [monger.conversion :refer :all]
            
            [monger.operators :refer :all])
  )

;;Data base access

(def conn (mg/connect {:host "localhost"}))
(def db (mg/get-db conn "api_twitter_test"))
(def user-coll "users")

(defn- get-user-oid [user-name]
  (:_id (mc/find-one-as-map db user-coll {:userName user-name} ["_id"])))

(defn save-user [content]  
  (let [user-registred (mc/insert-and-return db user-coll (assoc content :created_at (Date.) :twitters []))] 
    {:message "success" :_id (:_id user-registred)}))

(defn count-register [content]
 (mc/count db user-coll {$or [{:userName (:user-name content)}
                            {:email (:email content)}]}))

(defn find-user [content]
  (mc/find-one-as-map db user-coll {$or [{:userName (:user-name content)}
                                                {:email (:email content)}]} ["_id" "userName" "email" "passHash"]))

(defn push-twitter [content]
  (let [oid-generate (ObjectId.)]
  (mc/update db user-coll {:userName (:user-name content)} {$push {:twitters (hash-map	:_id oid-generate :post (:post content) :created_at (Date.)
                                                                                       :likes [] :retweets [] :replys [])}})
  {:message "success" :_id oid-generate} ))

(defn push-like-twitter [user-name user-twitter twitter-oid]
  (mc/update db user-coll {"userName" user-twitter "twitters._id" (ObjectId. twitter-oid)} {$push {"twitters.$.likes" (hash-map :userName_id (get-user-oid user-name) :created_at (Date.))}})
  {:message "success"}  )

(defn pull-like-twitter [user-name user-twitter twitter-oid]
  (mc/update db user-coll {"userName" user-twitter "twitters._id" (ObjectId. twitter-oid) "twitters.likes.userName_id"  (get-user-oid user-name) },
                  { $pull { "twitters.$.likes"  { :userName_id (get-user-oid user-name) }}});  
  {:message "success"})

(defn count-twitters-user [user-name]
(let [result (mc/aggregate db user-coll [{$match {"userName" user-name}} {$project {:count {$size "$twitters"}}}] :cursor {:batch-size 0})] 
  (if ( > (count result) 0)
    (:count (first result))
    0
    )))

(defn list-twitters-user [user skip]
  
  (let [count (- (count-twitters-user user))
        pagination (if (> (+ count skip) 0 ) 
                     0
                     (+ count skip))
        result (if (< pagination 0) 
                   (mc/find-one-as-map db user-coll {:userName user} {"twitters"  {$slice [pagination , 10]}})
                   {:twitters '()})]
    
    (:twitters result)    
    ))
 
(defn get-twitter-user [twitter-oid]
  (let [result (mc/find-one-as-map db user-coll { "twitters" {$elemMatch {"_id" (ObjectId. twitter-oid)}}})]
    (first (:twitters result))))

(defn get-twitter-likes [twitter-oid]
  (let [result (get-twitter-user twitter-oid )]
    (:likes result) ))

(defn get-user-like-in-twitter [user-name twitter-oid]
  (let [result  (get-twitter-likes twitter-oid)]
    (first (filter #(and (=(:userName_id %) (get-user-oid user-name))) result)
         )))

(defn clear []
  (mc/remove db user-coll))