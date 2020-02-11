(ns api-twitter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [api-twitter.user-service :as user-service]
            [api-twitter.twitter-service :as twitter-service]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]))

;;Handle routes, using api wrap

(defn encode-json	[content & [status]]
	 {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string	content)
   :throw-exceptions false})

(defroutes app-routes
  (GET "/" [] "Welcome API Twitter")
  
  (POST "/register" request 
    (if	(user-service/valid-register	(:body request))
      (if (user-service/register-not-exist? (:body request))
        (encode-json (user-service/register (:body request)) 201)
        (encode-json {:message "Username or email already exist!"}	422))
      (encode-json {:message "Invalid request"}	422)))
  
  (POST "/sign" request 
    (if (user-service/valid-sign (:body request))
      (encode-json (user-service/sign (:body request)) 200)
      (encode-json {:message	"Invalid request"}	422)))

  (POST "/twitter" request
    (if (twitter-service/valid-twitter (:body request))
      (if (not (user-service/register-not-exist? (:body request)))
          (encode-json (twitter-service/twitter-post (:body request)) 201)
          (encode-json {:message "Username invalid"}	422))
        (encode-json {:message	"Invalid request"}	422)))  
  
  (GET "/twitter/:user" [user]
    (if (twitter-service/valid-my-twitters user)
      (encode-json (twitter-service/my-twitters user), 200)
      (encode-json {:message	"Invalid request"}	422)))
  
  (GET "/twitter/:user/status/:id" [user id]
      (if (twitter-service/valid-view-twitter user id)
        (encode-json (twitter-service/view-twitter id), 200)
        (encode-json {:message	"Invalid request"}	422)))
     
     (route/not-found "Sorry! Not Found. Are you Lost?"))


(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
