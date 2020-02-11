(ns api-twitter.handler-unit-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [api-twitter.db :as db]
            [api-twitter.user-service :as user-service]
            [api-twitter.twitter-service :as twitter-service]
            [api-twitter.utils-test :refer :all]
            [api-twitter.handler :refer :all]))

;;Basic unit tests

(facts "checking main route"
  (let [response (app (mock/request :get "/"))]
    
    (fact "cheking respose"
          (:status response) => 200)
    
    (fact "cheking body"
          (:body response) => "Welcome API Twitter"))    
)

(facts "invalid route"
  (let [response (app (mock/request :get "/invalid"))]
    
    (fact "not-found route"
          (:status response) => 404)
    
    (fact "user message"
          (:body response) => "Sorry! Not Found. Are you Lost?"))
)

(facts "user register"
       (db/clear)
       (against-background (user-service/register {:username "admin" :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}) => 
                           {:id 1 :username "admin" :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"})
       (let [response
             (app (-> (mock/request :post "/register")
                      (mock/json-body {:username "admin" :email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"})))]
         (fact "status response is 201" 
               (:status response) => 201)
         (fact "return json plus id"
               (:body response) => "{\"id\":1,\"username\":\"admin\",\"email\":\"admin@upnid.com\",\"pass_hash\":\"@gd67grdfbf87fhb87b872dh27\"}")))


(facts "sign with username"
       (against-background (user-service/sign {:username "admin" :pass_hash "@gd67grdfbf87fhb87b872dh27"}) => 
                           {:id 1 :username "admin" :email "admin@upnid.com"})
       (let [response
               (app (-> (mock/request :post "/sign")
                        (mock/json-body {:username "admin" :pass_hash "@gd67grdfbf87fhb87b872dh27"})))]
         (fact "status response is 200" 
               (:status response) => 200)
         (fact "return json plus id"
               (:body response) => "{\"id\":1,\"username\":\"admin\",\"email\":\"admin@upnid.com\"}")))

(facts "sign with email"
       (against-background (user-service/sign {:email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"}) => 
                           {:id 1 :username "admin" :email "admin@upnid.com" })
       (let [response
               (app (-> (mock/request :post "/sign")
                        (mock/json-body {:email "admin@upnid.com" :pass_hash "@gd67grdfbf87fhb87b872dh27"})))]
         (fact "status response is 200" 
               (:status response) => 200)
         (fact "return json plus id"
               (:body response) => "{\"id\":1,\"username\":\"admin\",\"email\":\"admin@upnid.com\"}")))

(facts "send twitter"
       (db/clear)
       (against-background (twitter-service/twitter-post {:username "admin" :post (twitter-text 100)}) =>
                           {:username "admin" :post (twitter-text 100)})
       (let [response
             (app (-> (mock/request :post "/twitter")
                      (mock/json-body {:username "admin" :post (twitter-text 100)})))]
         (fact "status response is 201"
               (:status response) => 201)
         ))

(facts "my twitters"      
       (against-background (twitter-service/my-twitters "admin") => [])
       (let [response
             (app (-> (mock/request :get "/twitter/admin")))]
         (fact "status response is 200"
               (:status response) => 200))
       
(facts "view twitter"      
       (against-background (twitter-service/view-twitter "984729odasjda") => {:username "admin" :post (twitter-text 100)})
       (let [response
             (app (-> (mock/request :get "/twitter/admin/status/984729odasjda")))]
         (fact "status response is 200"
               (:status response) => 200))))